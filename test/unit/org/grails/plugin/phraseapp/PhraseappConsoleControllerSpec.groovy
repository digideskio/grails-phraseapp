package org.grails.plugin.phraseapp

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PhraseappConsoleController)
class PhraseappConsoleControllerSpec extends Specification {

	def setup() {
	}

	def cleanup() {
	}

	void "index method returns the list of snapshots"() {
		given:
			def messageSource = Mock(PhraseappResourceBundleMessageSource)
			controller.messageSource = messageSource
		when:
			def model = controller.index()
		then:
			1  * messageSource.getSnapshots() >> ['1', '2', '3']
			model.snapshots.size() > 0
	}

	@Unroll
	void "update redirects to index page with a message in the flash scope"() {
		given:
			def messageSource = Mock(PhraseappResourceBundleMessageSource)
			controller.messageSource = messageSource
		when:
			controller.update()
		then:
			1  * messageSource.update() >> serviceResponse
			response.redirectedUrl == '/phraseappConsole/index'
			flash.message == flashMessage
			flash.error == flashError
		where:
			serviceResponse | flashMessage           | flashError
			true            | 'Translations updated' | null
			false           | null                   | 'Error while updating translations'
	}

	@Unroll
	void "create redirects to index page with a message in the flash scope"() {
		given:
			def messageSource = Mock(PhraseappResourceBundleMessageSource)
			controller.messageSource = messageSource
		when:
			controller.create()
		then:
			1  * messageSource.createNewSnapshot() >> serviceResponse
			response.redirectedUrl == '/phraseappConsole/index'
			flash.message == flashMessage
			flash.error == flashError
		where:
			serviceResponse | flashMessage       | flashError
			true            | 'Snapshot created' | null
			false           | null               | 'Error while creating the snapshot'
	}

	@Unroll
	void "restore redirects to index page with a message in the flash scope"() {
		given:
			def messageSource = Mock(PhraseappResourceBundleMessageSource)
			controller.messageSource = messageSource
		when:
			1  * messageSource.restoreSnapshot(1l) >> serviceResponse
			controller.params.id = 1l
			controller.restore()
		then:
			response.redirectedUrl == '/phraseappConsole/index'
			flash.message == flashMessage
			flash.error == flashError
		where:
			serviceResponse | flashMessage        | flashError
			true            | 'Snapshot restored' | null
			false           | null                | 'Error while restoring the snapshot'
	}

	@Unroll
	void "restore redirects to index page with a message in the flash.error scope if the id is not set"() {
		given:
			def messageSource = Mock(PhraseappResourceBundleMessageSource)
			controller.messageSource = messageSource
		when:
			controller.params.id = id
			controller.restore()
		then:
			serviceCalls  * messageSource.restoreSnapshot(id) >> true
			response.redirectedUrl == '/phraseappConsole/index'
			flash.message == flashMessage
			flash.error == flashError
		where:
			id    | serviceCalls | flashMessage        | flashError
			'asd' | 0            | null                | 'Missing or wrong id'
			1l    | 1            | 'Snapshot restored' | null
	}

	@Unroll
	void "delete redirects to index page with a message in the flash scope"() {
		given:
			def messageSource = Mock(PhraseappResourceBundleMessageSource)
			controller.messageSource = messageSource
		when:
			controller.params.id = 1l
			controller.delete()
		then:
			1  * messageSource.deleteSnapshot(1l) >> serviceResponse
			response.redirectedUrl == '/phraseappConsole/index'
			flash.message == flashMessage
			flash.error == flashError
		where:
			serviceResponse | flashMessage       | flashError
			true            | 'Snapshot deleted' | null
			false           | null               | 'Error while deleting the snapshot'
	}

	@Unroll
	void "delete redirects to index page with a message in the flash.error scope if the id is not set"() {
		given:
			def messageSource = Mock(PhraseappResourceBundleMessageSource)
			controller.messageSource = messageSource
		when:
			controller.params.id = id
			controller.delete()
		then:
			serviceCalls  * messageSource.deleteSnapshot(id) >> true
			response.redirectedUrl == '/phraseappConsole/index'
			flash.message == flashMessage
			flash.error == flashError
		where:
			id    | serviceCalls | flashMessage       | flashError
			'asd' | 0            | null               | 'Missing or wrong id'
			1l    | 1            | 'Snapshot deleted' | null
	}
}
