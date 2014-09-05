Grails PhraseApp Plugin
==================================

PhraseApp.com is a tool that helps the process of translation of all kind of labels used inside any application.
	It can process the .properties file used by Grails (i18n) and export them back in the same format. Phraseapp interface is 
	really clear and powerful, it can handle essentially any known language.
	The only problem is that those .properties file have to be manually copied into grails-app/i18n/ folder, once the 
	translator completes his work. And this must be done for each language your website need to be translated in.
	Then you have to push the file in your VCS and finally deploy on production.
	The purpose of this plugin is to simplify the procedure: with one click the latest translations from all languages
	are downloaded and made available right into the live application.
	For more information please read the documentation.

Installation
------------
Dependency :

    compile ":phraseapp:0.1"

Configuration needed in `grails-app/conf/Config.groovy` file:

	grails {
		plugin{
			phraseapp {
				reloadAtStartup = true
				reloadAtConfigChange = false
				baseDir = '/folder/where/to/save/message/bundles/downloaded/from/phraseapp'
				authToken = 'xxxxxxxxxxxxxxxxxxxx'
				locales = ['en', 'it-IT', 'it' , 'fr', 'de']
			}
		}
	}


Plugin Usage
------------

## The Console ##

go to http://localhost:8080/${appName}/phraseappConsole

Release Notes
=============

* 0.1   - released 2014 - this is the first released revision of the plugin.


TODOs
=====

* in the console: [choose which lang to upload, update&snapshot, in dev mode checkbox to update bundles in VCS system]
* in the helper: [choose which lang + which PA tag + keys white and black lists]
* plugin install script to update application config.groovy
* ideas appreciated

Credits
=======

love you mom.

[redis-cache-plugin]: http://www.grails.org/plugin/cache-redis
[redis-plugin]: http://www.grails.org/plugin/redis
[redis-plugin-example]: https://github.com/grails-plugins/grails-redis#memoization-annotation-keys
[cache-plugin]: http://www.grails.org/plugin/cache
[redis]: http://redis.io
[jedis]: https://github.com/xetorthio/jedis/wiki
[GameTube]: http://www.gametube.org/
[guide on groovy AST]: http://www.christianoestreich.com/2012/02/groovy-ast-transformations-part-1/
