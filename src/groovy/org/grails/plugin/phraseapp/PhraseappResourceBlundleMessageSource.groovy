package org.grails.plugin.phraseapp

import groovy.io.FileType
import org.apache.commons.io.FileUtils
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.codehaus.groovy.grails.context.support.PluginAwareResourceBundleMessageSource

/**
 * Created by tamer on 27/07/14.
 */
class PhraseappResourceBlundleMessageSource extends PluginAwareResourceBundleMessageSource {

	private static final Log LOG = LogFactory.getLog(PhraseappResourceBlundleMessageSource.class);

	void setPhConfiguration(ConfigObject phConfiguration) {
		this.phConfiguration = phConfiguration
		if (!phConfiguration.baseDir.toString().endsWith('/')) {
			phConfiguration.baseDir = phConfiguration.baseDir + '/'
		}
	}
	ConfigObject phConfiguration
	final static String SCHEME = 'https'
	final static String HOST = 'phraseapp.com'
	final static String PATH = '/api/v1/locales/'
	final static String QUERY_AUTH = 'auth_token='
	final static String STANDARD_EXTENTION = '.properties'

	public void afterPropertiesSet() throws Exception {

		super.afterPropertiesSet()

		// find i18n resource bundles and resolve basenames
		//TODO : retrieve all tags to create the set of baseNames, working with messages file only
		//TODO : handling plugins bundles (?)
		super.setBasenames(phConfiguration.baseDir + 'messages')

		LOG.debug "Creating messageSource with basenames: ${super.getPluginBaseNames()}"

		if (phConfiguration.reloadAtStartup) {
			importAll()
		}

	}

	void importAll() {
		String authToken = phConfiguration.authToken
		for (String locale in phConfiguration.locales) {

			File currBundleFile = new File(phConfiguration.baseDir + "messages_" + locale.replace('-', '_') + STANDARD_EXTENTION)

			if (!currBundleFile.getParentFile().canWrite()){
				throw new Exception("Cannot write files in  ${currBundleFile.getParentFile().canonicalPath}")
			}

			HttpGet req = new HttpGet(new URI(SCHEME, null, HOST, 443, PATH + locale + STANDARD_EXTENTION, QUERY_AUTH + authToken, null))
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(req)
			switch (response?.statusLine?.statusCode){
				case 200 :
					InputStream inputStream = response.getEntity().getContent()
					FileUtils.copyInputStreamToFile(inputStream, currBundleFile)
					break
				case 401:
					throw new Exception("Auth token not valid: ${response?.statusLine}")
					break
				default:
					LOG.error("Bundle file for ${locale} locale not found in Phraseapp: ${response?.statusLine}. If none is working check the API version.")
			}
		}
	}

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
			File origin = new File(phConfiguration.baseDir)
			destinationDirectory = snapshotDirByTimestamp(System.currentTimeMillis())
			origin.eachFile(FileType.FILES) { File file ->
				if (file?.name?.endsWith(PhraseappResourceBlundleMessageSource.STANDARD_EXTENTION)) {
					FileUtils.copyFileToDirectory(file, destinationDirectory)
				}
			}
			return true
		} catch (Exception e) {
			LOG.error('Error while creating the snapshot folder for translations', e)
			if (destinationDirectory?.canWrite()) {
				destinationDirectory.deleteDir()
			}
		}
		return false
	}

	boolean restoreSnapshot(Long timestamp) {

		try {
			File snapshotDir = snapshotDirByTimestamp(timestamp)
			File origin = new File(phConfiguration.baseDir)

			if (snapshotDir?.canRead() && origin?.canWrite()) {
				snapshotDir.eachFile(FileType.FILES) { File file ->
					if (file?.name?.endsWith(PhraseappResourceBlundleMessageSource.STANDARD_EXTENTION)) {
						FileUtils.copyFileToDirectory(file, origin)
					}
				}
				return true
			}
		} catch (Exception e) {
			LOG.error('Error while deleting the snapshot folder for translations', e)
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
			LOG.error('Error while deleting the snapshot folder for translations', e)
		}
		return false
	}

	boolean update() {
		try {
			importAll()
			clearCache()
		} catch (Exception e) {
			LOG.error('Error while updating translations', e)
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
		"${phConfiguration.baseDir}snapshots/"
	}

//	private ConfigObject getPhConfig() {
//		ConfigObject phConfiguration = grailsApplication.config.grails?.plugin?.phraseapp
//		if (!phConfiguration) {
//			throw new Exception('Phraseapp Plugin config not found!')
//		}
//		phConfiguration
//	}

//	reminder for selective keys update..
//	def importByKeys() {
//
//
//		String authToken = phConfiguration.authToken
//		def result = restService.restGet(phRestUri + '/',  'translation_keys' , [auth_token : authToken])
//		keyList << result.name.unique()
//
//		def translations = restService.restGet(phRestUri + '/translations/',  'fetch_list' , [auth_token : authToken, locale: 'en', 'keys[]': keyList])
//
//		translations.size()
//
//	}
}
