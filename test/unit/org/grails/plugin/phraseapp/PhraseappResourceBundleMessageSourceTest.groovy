package org.grails.plugin.phraseapp

import org.apache.http.HttpVersion
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicHttpResponse
import org.apache.http.message.BasicStatusLine
import spock.lang.IgnoreRest
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

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

	@Unroll
	@IgnoreRest
	def "importAll"() { // retrieve all bundle files using Phraseapp API and stores them in the configured path
		given:
			ConfigObject configObject = new ConfigObject()
			configObject.baseDir = tmp.getParent()
			configObject.authToken = 'xxxxxxxxxxxxxxxxxxxx'
			configObject.locales = ['en', 'it-IT', 'it', 'fr', 'de']
			messageSource.setPhConfiguration(configObject)

		and:
//			def mockFile = GroovyMock(File, global: true, useObjenesis: true)
//			def mockResponse = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, responseCode, null))
			def mockClient = GroovyMock(DefaultHttpClient, global: true, useObjenesis: true) {
				1 * execute(_) >> { new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, responseCode, null)) }
//				consumeTimes * mockResponse.getEntity() >> { null }
			}

		when:
			messageSource.importAll()

		then:
			interaction {
				1 * new File(_) >> { mockFile }
				1 * new DefaultHttpClient() >> { mockClient }
			}
			if (execptionExpected) {
				thrown(Exception)
			}
		where:
			responseCode | execptionExpected | consumeTimes
			401          | true              | 0
			200          | false             | 1
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

	def "snapshotDirByTimestamp throws an exception if timestamp is null or Phraseapp Plugin config not found"() {
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
