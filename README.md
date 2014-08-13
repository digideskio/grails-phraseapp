Grails PhraseApp Plugin
==================================

PhraseApp is an external tool really helpful for the translation of i18n labels. It got its own platform and utility
	and it gives the possibility to download bundle file, one for each language you need to translate, that are ready
	to be copied into grails-app/i18n/ folder.
	The purpose of this plugin is to simplify the procedure: with one click the latest translations from all languages
	are downloaded and uploaded right into the live application.

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
