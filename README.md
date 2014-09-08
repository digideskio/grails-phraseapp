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

The plugin can be used throuhgt a console that can be found at this url: 
	http://localhost:8080/${appName}/phraseappConsole.

*Attention: the whole Controller is not secured in any way! be sure to enable the access to this page only to Admins.*

In this very simple console you can find:
* a 'Download' link that triggers the *download* of the latest saved translations from phraseapp, saves them on local file system, and updates all keys in the current server. The old version of each bundle file will be overwritten.
* a 'Create' link that triggers the *creation* of a *snapshot* containing the current state of translations. Since something can go wrong at any time, it's useful to have a save-point where you are sure that all translations were correct. This operation creates a copy of all bundle files present in the configured folder into a subfolder.

Then you have the list of all snapshots previously created, on which you can trigger these two operations:
* 'Restore' a specific snapshot: like a time machine. All files present when the snapshot was created will replace the current ones. If you added another language to the list after the creation of the snapshot it will *not* be replaced. The snapshot will *not* be deleted after this operation.
* 'Delete' a specific snapshot: when it's time to recover some space from the disk.


Release Notes
=============

* 0.1   - released 2014 - this is the first released revision of the plugin.


TODOs
=====

* in the console: [choose which lang to upload, update&snapshot, in dev mode checkbox to update bundles in VCS system]
* handle the propagation of changes to .properties files in all (eventual) production servers 
* in the custom messageSource bean: [choose which lang + which PhraseApp tag + keys white and black lists]
* plugin install script to update application config.groovy
* refine the console layout + make it self-contained
* ideas appreciated

Credits
=======

Thanks to Germán for his review and the Grails community.
If you are using the plugin, please consider writing you opionions and requests.


