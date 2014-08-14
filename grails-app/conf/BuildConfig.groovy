grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		mavenLocal()
		mavenCentral()
	}

	dependencies {
//		test 'cglib:cglib-nodep:2.2' //workaround
	}
	plugins {
		build(":release:3.0.1",
				":rest-client-builder:1.0.3") {
			export = false
		}
		compile ":rest:0.8"


	}
}
