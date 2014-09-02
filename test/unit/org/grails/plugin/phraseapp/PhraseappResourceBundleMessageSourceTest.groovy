package org.grails.plugin.phraseapp

import org.apache.http.HttpVersion
import org.apache.http.entity.BasicHttpEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicHttpResponse
import org.apache.http.message.BasicStatusLine
import spock.lang.Shared
import spock.lang.Specification

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

	def "importAll retrieves all bundle files using Phraseapp API and stores them in the configured path"() {
		given:
			def locales = ['en', 'it-IT', 'it', 'fr', 'de']
			ConfigObject configObject = new ConfigObject()
			configObject.baseDir = tmp.getParent()
			configObject.authToken = 'xxxxxxxxxxxxxxxxxxxx'
			configObject.locales = locales
			messageSource.setPhConfiguration(configObject)

		and:
			def mockClient = GroovyMock(DefaultHttpClient, global: true, useObjenesis: true)

		when:
			messageSource.importAll()

		then:

			locales.size() * new DefaultHttpClient() >> { mockClient }
			locales.size() * mockClient.execute(_) >> {
				def resp = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, null))
				def entity = new BasicHttpEntity()
				entity.setContent(new FileInputStream(File.createTempFile('fakeEntity', 'tmp')))
				resp.setEntity(entity)
				resp
			}
			notThrown(Exception)
	}

	def "importAll throws an exception if PhraseAPP respond with http status 401"() {
		given:
			def locales = ['en', 'it-IT', 'it', 'fr', 'de']
			ConfigObject configObject = new ConfigObject()
			configObject.baseDir = tmp.getParent()
			configObject.authToken = 'xxxxxxxxxxxxxxxxxxxx'
			configObject.locales = locales
			messageSource.setPhConfiguration(configObject)

		and:
			def mockClient = GroovyMock(DefaultHttpClient, global: true, useObjenesis: true)

		when:
			messageSource.importAll()

		then:
			1 * new DefaultHttpClient() >> { mockClient }
			1 * mockClient.execute(_) >> {
				new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 401, null))
			}
			thrown(Exception)
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
