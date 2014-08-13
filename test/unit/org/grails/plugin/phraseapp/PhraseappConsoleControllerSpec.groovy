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
			def phraseappService = mockFor(PhraseappService)
			phraseappService.demand.getSnapshots { -> ['1', '2', '3'] }
			controller.phraseappService = phraseappService.createMock()
		when:
			def model = controller.index()
		then:
			model.snapshots.size() > 0
	}

	@Unroll
	void "update redirects to index page with a message in the flash scope"() {
		given:
			def phraseappService = mockFor(PhraseappService)
			phraseappService.demand.update { -> serviceResponse }
			controller.phraseappService = phraseappService.createMock()
		when:
			controller.update()
		then:
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
			def phraseappService = mockFor(PhraseappService)
			phraseappService.demand.createNewSnapshot { -> serviceResponse }
			controller.phraseappService = phraseappService.createMock()
		when:
			controller.create()
		then:
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
			def phraseappService = mockFor(PhraseappService)
			phraseappService.demand.restoreSnapshot { Long timestamp -> serviceResponse }
			controller.phraseappService = phraseappService.createMock()
		when:
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
			def phraseappService = mockFor(PhraseappService)
			phraseappService.demand.restoreSnapshot(serviceCalls) { Long timestamp -> true }
			controller.phraseappService = phraseappService.createMock()
		when:
			controller.params.id = id
			controller.restore()
		then:
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
			def phraseappService = mockFor(PhraseappService)
			phraseappService.demand.deleteSnapshot { Long timestamp -> serviceResponse }
			controller.phraseappService = phraseappService.createMock()
		when:
			controller.params.id = 1l
			controller.delete()
		then:
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
			def phraseappService = mockFor(PhraseappService)
			phraseappService.demand.deleteSnapshot(serviceCalls) { Long timestamp -> true }
			controller.phraseappService = phraseappService.createMock()
		when:
			controller.params.id = id
			controller.delete()
		then:
			response.redirectedUrl == '/phraseappConsole/index'
			flash.message == flashMessage
			flash.error == flashError
		where:
			id    | serviceCalls | flashMessage       | flashError
			'asd' | 0            | null               | 'Missing or wrong id'
			1l    | 1            | 'Snapshot deleted' | null
	}
}
