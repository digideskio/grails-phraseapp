import grails.util.Environment
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.web.context.GrailsConfigUtils
import org.codehaus.groovy.grails.web.i18n.ParamsAwareLocaleChangeInterceptor
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine
import org.grails.plugin.phraseapp.PhraseappResourceBundleMessageSource
import org.springframework.web.context.support.ServletContextResourcePatternResolver
import org.springframework.web.servlet.i18n.SessionLocaleResolver

class PhraseappGrailsPlugin {

	private static LOG = LogFactory.getLog(this)

	// the plugin version
	def version = "0.1"
	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "2.0 > *"
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
	PhraseApp.com is a tool that helps the process of translation of all kind of labels used inside any application.
	It can process the .properties file used by Grails (i18n) and export them back in the same format. Phraseapp interface is
	really clear and powerful, it can handle essentially any known language.
	The only problem is that those .properties file have to be manually copied into grails-app/i18n/ folder, once the
	translator complete his work. And this must be done for each language your website need to be translated in.
	Then you have to push the file in your VCS and finally deploy on production.
	The purpose of this plugin is to simplify the procedure: with one click the latest translations from all languages
	are downloaded and made available right into the live application.
	For more information please read the documentation.
'''

	def documentation = "https://github.com/tamershahin/grails-phraseapp/blob/master/README.md"
	def issueManagement = [system: "GITHUB", url: "https://github.com/tamershahin/grails-phraseapp/issues"]
	def scm = [url: "https://github.com/tamershahin/grails-phraseapp"]

	def license = "APACHE"

	def doWithSpring = {

		ConfigObject phConfig = application.config.grails?.plugin?.phraseapp
		if (!phConfig) {
			throw new Exception('Phraseapp Plugin config not found!')
		}

		if (!phConfig.baseDir.toString().endsWith('/')) {
			phConfig.baseDir += '/'
		}

		if (!new File(phConfig.baseDir).canWrite()) {
			throw new Exception("Cannot write files in: ${phConfig.baseDir}")
		}

		if (application.warDeployed) {
			servletContextResourceResolver(ServletContextResourcePatternResolver, ref('servletContext'))
		}

		messageSource(PhraseappResourceBundleMessageSource) {
			phConfiguration = phConfig
			fallbackToSystemLocale = false
			pluginManager = manager
			if (Environment.current.isReloadEnabled() || GrailsConfigUtils.isConfigTrue(application, GroovyPagesTemplateEngine.CONFIG_PROPERTY_GSP_ENABLE_RELOAD)) {
				def cacheSecondsSetting = application?.flatConfig?.get('grails.i18n.cache.seconds')
				cacheSeconds = cacheSecondsSetting == null ? 5 : cacheSecondsSetting as Integer
			}
			if (application.warDeployed) {
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
			event.ctx.messageSource.phConfiguration = phConfig
			event.ctx.messageSource.update()
		}
	}

	def onShutdown = { event ->
		// TODO Implement code that is executed when the application shuts down (optional)
	}

}

