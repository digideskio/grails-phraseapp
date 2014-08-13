package org.grails.plugin.phraseapp

import grails.test.mixin.TestFor
import spock.lang.Shared
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PhraseappService)
class PhraseappServiceSpec extends Specification {


	@Shared
	File tmp = File.createTempFile('spockTest', '')

	def setup() {
		service.grailsApplication.config.grails.plugin.phraseapp = [
				baseDir: tmp.getParent()
		]
	}

	def cleanup() {
	}

	def "phConfig returns the config for Phraseapp Plugin or an exception if not found"() {
		when:
			def cfg = service.getPhConfig()
		then:
			cfg
		when:
			service.grailsApplication.config.grails.plugin.phraseapp = null
			service.getPhConfig()
		then:
			thrown(Exception)
	}

	def "getSnapshotsBaseDirName returns the snapshots/ directory name or an exception if Phraseapp Plugin config not found"() {
		when:
			String dirName = service.getSnapshotsBaseDirName()
		then:
			dirName.endsWith('snapshots/')
		when:
			service.grailsApplication.config.grails.plugin.phraseapp = null
			service.getSnapshotsBaseDirName()
		then:
			thrown(Exception)
	}

	def "getSnapshotsBaseDir returns the snapshots directory file or an exception if Phraseapp Plugin config not found"() {
		when:
			File dirFile = service.getSnapshotsBaseDir()
		then:
			dirFile.name.endsWith('snapshots')
		when:
			service.grailsApplication.config.grails.plugin.phraseapp = null
			service.getSnapshotsBaseDir()
		then:
			thrown(Exception)
	}

	def "snapshotDirByTimestamp returns the snapshots directory file"() {
		Long timestamp = System.currentTimeMillis()
		when:
			File dirFile = service.snapshotDirByTimestamp(timestamp)
		then:
			dirFile.name.endsWith(timestamp.toString())
	}

	def "snapshotDirByTimestamp throws an exception if timestamp is null or  Phraseapp Plugin config not found"() {
		when:
			service.snapshotDirByTimestamp(null)
		then:
			thrown(Exception)
		when:
			service.grailsApplication.config.grails.plugin.phraseapp = null
			service.snapshotDirByTimestamp(System.currentTimeMillis())
		then:
			thrown(Exception)
	}


}
