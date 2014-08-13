import grails.util.Environment
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.context.support.PluginAwareResourceBundleMessageSource
import org.codehaus.groovy.grails.web.context.GrailsConfigUtils
import org.codehaus.groovy.grails.web.i18n.ParamsAwareLocaleChangeInterceptor
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine
import org.grails.plugin.phraseapp.PhraseappMessageSourceHelper
import org.springframework.web.context.support.ServletContextResourcePatternResolver
import org.springframework.web.servlet.i18n.SessionLocaleResolver

class PhraseappGrailsPlugin {

	private static LOG = LogFactory.getLog(this)

	// the plugin version
	def version = "0.1-SNAPSHOT"
	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "1.3 > *"
	// resources that are excluded from plugin packaging
	def pluginExcludes = [
			"grails-app/views/error.gsp"
	]

	def watchedResources = ['file:./grails-app/controllers/**', 'file:./grails-app/services/**'
	                        , 'file:./grails-app/i18n/**']

	def title = "PhaseApp Plugin Plugin"
	def author = "Tamer Shahin "
	def authorEmail = "tamer.shahin@gmail.com  "
	def description = '''\
	PhraseApp is an external tool really helpful for the translation of i18n labels. It got its own platform and utility
	and it gives the possibility to download bundle file, one for each language you need to translate, that are ready
	to be copied into grails-app/i18n/ folder.
	The purpose of this plugin is to simplify the procedure: with one click the latest translations from all languages
	are downloaded and uploaded right into the live application.
	For more information please read the documentation.
'''

	// URL to the plugin's documentation
	def documentation = "https://github.com/tamershahin/xxxx/blob/master/README.md"
	def issueManagement = [system: "GITHUB", url: "https://github.com/tamershahin/xxx/issues"]
	def scm = [url: "https://github.com/tamershahin/xxxxxxx"]

	def license = "APACHE"


	def doWithSpring = {

		ConfigObject phConfig = application.config.grails?.plugin?.phraseapp
		if (!phConfig) {
			throw new Exception('Phraseapp Plugin config not found!')
		}
		if (!phConfig.baseDir.toString().endsWith('/')) {
			phConfig.baseDir = phConfig.baseDir + '/'
		}

		// find i18n resource bundles and resolve basenames
		//TODO : retrieve all tags to create the set of baseNames, working with messages file only
		//TODO : handling plugins bundles (?)
		Set baseNames = [phConfig.baseDir + 'messages']
		PhraseappMessageSourceHelper.init(phConfig)

		LOG.debug "Creating messageSource with basenames: $baseNames"

		if (Environment.isWarDeployed()) {
			servletContextResourceResolver(ServletContextResourcePatternResolver, ref('servletContext'))
		}

		messageSource(PluginAwareResourceBundleMessageSource) {
			basenames = baseNames.toArray()
			fallbackToSystemLocale = false
			pluginManager = manager
			if (Environment.current.isReloadEnabled() || GrailsConfigUtils.isConfigTrue(application, GroovyPagesTemplateEngine.CONFIG_PROPERTY_GSP_ENABLE_RELOAD)) {
				def cacheSecondsSetting = application?.flatConfig?.get('grails.i18n.cache.seconds')
				cacheSeconds = cacheSecondsSetting == null ? 5 : cacheSecondsSetting as Integer
			}
			if (Environment.isWarDeployed()) {
				resourceResolver = ref('servletContextResourceResolver')
			}
		}

		localeChangeInterceptor(ParamsAwareLocaleChangeInterceptor) {
			paramName = "lang"
		}

		localeResolver(SessionLocaleResolver)
	}

	def onConfigChange = { event ->
		// event.application, event.manager, event.ctx, and event.plugin.
		ConfigObject phConfig = event.application.config.grails?.plugin?.phraseapp
		if (!phConfig) {
			throw new Exception('Phraseapp Plugin config not found!')
		}

		if (phConfig.reloadAtConfigChange) {
			PhraseappMessageSourceHelper.reload(phConfig, event.ctx.messageSource)
		}
	}

	def onShutdown = { event ->
		// TODO Implement code that is executed when the application shuts down (optional)
	}

}

