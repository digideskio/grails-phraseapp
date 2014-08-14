package org.grails.plugin.phraseapp

import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by tamer on 14/08/14.
 */
class PhraseappResourceBundleMessageSourceTest extends Specification {

	@Shared
	File tmp = File.createTempFile('spockTest', '')

	PhraseappResourceBundleMessageSource messageSource = new PhraseappResourceBundleMessageSource()
	def setup() {
		ConfigObject configObject = new ConfigObject()
		configObject.baseDir = tmp.getParent()
		messageSource.setPhConfiguration(configObject)
	}

	def cleanup() {
	}


	def "getSnapshotsBaseDirName returns the snapshots/ directory name or an exception if Phraseapp Plugin config not found"() {
		when:
			String dirName = messageSource.getSnapshotsBaseDirName()
		then:
			dirName.endsWith('snapshots/')
		when:
			messageSource.setPhConfiguration(null)
			messageSource.getSnapshotsBaseDirName()
		then:
			thrown(Exception)
	}

	def "getSnapshotsBaseDir returns the snapshots directory file or an exception if Phraseapp Plugin config not found"() {
		when:
			File dirFile = messageSource.getSnapshotsBaseDir()
		then:
			dirFile.name.endsWith('snapshots')
		when:
			messageSource.setPhConfiguration(null)
			messageSource.getSnapshotsBaseDir()
		then:
			thrown(Exception)
	}

	def "snapshotDirByTimestamp returns the snapshots directory file"() {
		Long timestamp = System.currentTimeMillis()
		when:
			File dirFile = messageSource.snapshotDirByTimestamp(timestamp)
		then:
			dirFile.name.endsWith(timestamp.toString())
	}

	def "snapshotDirByTimestamp throws an exception if timestamp is null or  Phraseapp Plugin config not found"() {
		when:
			messageSource.snapshotDirByTimestamp(null)
		then:
			thrown(Exception)
		when:
			messageSource.setPhConfiguration(null)
			messageSource.snapshotDirByTimestamp(System.currentTimeMillis())
		then:
			thrown(Exception)
	}
}
