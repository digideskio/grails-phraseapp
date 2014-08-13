package org.grails.plugin.phraseapp

import org.apache.commons.io.FileUtils
import org.apache.commons.logging.LogFactory
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.codehaus.groovy.grails.context.support.PluginAwareResourceBundleMessageSource
import org.springframework.context.support.ReloadableResourceBundleMessageSource

/**
 * Created by tamer on 27/07/14.
 */
class PhraseappMessageSourceHelper {

	private static LOG = LogFactory.getLog(this)

	static ConfigObject phConfiguration
	final static String SCHEME = 'https'
	final static String HOST = 'phraseapp.com'
	final static String PATH = '/api/v1/locales/'
	final static String QUERY_AUTH = 'auth_token='
	final static String STANDARD_EXTENTION = '.properties'

	public static void init(ConfigObject phConfiguration) {
		this.phConfiguration = phConfiguration
		if (phConfiguration.reloadAtStartup) {
			importAll(phConfiguration)
		}
	}

	static void importAll(ConfigObject phConfiguration) {
		String authToken = phConfiguration.authToken
		for (String locale in phConfiguration.locales) {

			File currBundleFile = new File(phConfiguration.baseDir + "messages_" + locale.replace('-', '_') + STANDARD_EXTENTION)

			HttpGet req = new HttpGet(new URI(SCHEME, null, HOST, 443, PATH + locale + STANDARD_EXTENTION, QUERY_AUTH + authToken, null))
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(req)
			if (response.statusLine.statusCode == 200) {
				InputStream inputStream = response.getEntity().getContent()
				FileUtils.copyInputStreamToFile(inputStream, currBundleFile)
			} else {
				LOG.error("Bundle file for ${locale} locale not found in Phraseapp")
			}

		}
	}

	static void reload(ConfigObject phConfiguration, def messageSource) {
		this.phConfiguration = phConfiguration
		importAll(phConfiguration)
		if (messageSource instanceof PluginAwareResourceBundleMessageSource) {
			messageSource.clearCache()
		} else {
			LOG.warn "Bean messageSource is not an instance of ${ReloadableResourceBundleMessageSource.name}. Can't reload"
		}
	}

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
