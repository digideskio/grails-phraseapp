package org.grails.plugin.phraseapp

import grails.transaction.Transactional
import groovy.io.FileType
import org.apache.commons.io.FileUtils

@Transactional
class PhraseappService {

	def messageSource
	def grailsApplication

	List<String> getSnapshots() {
		List<String> snapshots = []
		snapshotsBaseDir?.eachDir { file ->
			snapshots << file.name
		}
		snapshots
	}

	boolean createNewSnapshot() {

		File destinationDirectory
		try {
			File origin = new File(phConfig.baseDir)
			destinationDirectory = snapshotDirByTimestamp(System.currentTimeMillis())
			origin.eachFile(FileType.FILES) { File file ->
				if (file?.name?.endsWith(PhraseappMessageSourceHelper.STANDARD_EXTENTION)) {
					FileUtils.copyFileToDirectory(file, destinationDirectory)
				}
			}
			return true
		} catch (Exception e) {
			log.error('Error while creating the snapshot folder for translations', e)
			if (destinationDirectory?.canWrite()) {
				destinationDirectory.deleteDir()
			}
		}
		return false
	}

	boolean restoreSnapshot(Long timestamp) {

		try {
			File snapshotDir = snapshotDirByTimestamp(timestamp)
			File origin = new File(phConfig.baseDir)

			if (snapshotDir?.canRead() && origin?.canWrite()) {
				snapshotDir.eachFile(FileType.FILES) { File file ->
					if (file?.name?.endsWith(PhraseappMessageSourceHelper.STANDARD_EXTENTION)) {
						FileUtils.copyFileToDirectory(file, origin)
					}
				}
				return true
			}
		} catch (Exception e) {
			log.error('Error while deleting the snapshot folder for translations', e)
		}
		return false
	}

	boolean deleteSnapshot(Long timestamp) {
		try {
			File snapshotDir = snapshotDirByTimestamp(timestamp)
			if (snapshotDir?.canWrite()) {
				snapshotDir.deleteDir()
				return true
			}
		} catch (Exception e) {
			log.error('Error while deleting the snapshot folder for translations', e)
		}
		return false
	}

	boolean update() {
		try {
			PhraseappMessageSourceHelper.reload(phConfig, messageSource)
		} catch (Exception e) {
			log.error('Error while updating translations', e)
			return false
		}
		return true
	}


	private File snapshotDirByTimestamp(Long timestamp) {
		if (!timestamp) {
			throw new Exception('Timestamp not specified')
		}
		new File(snapshotsBaseDirName + timestamp.toString())
	}

	private File getSnapshotsBaseDir() {
		new File(snapshotsBaseDirName)
	}

	private String getSnapshotsBaseDirName() {
		"${phConfig.baseDir}snapshots/"
	}

	private ConfigObject getPhConfig() {
		ConfigObject phConfig = grailsApplication.config.grails?.plugin?.phraseapp
		if (!phConfig) {
			throw new Exception('Phraseapp Plugin config not found!')
		}
		phConfig
	}
}
