package org.grails.plugin.phraseapp

class PhraseappConsoleController {

	def messageSource

	def index() {
		List<String> snapshots = messageSource.snapshots
		[snapshots: snapshots]
	}

	//Todo: move all actions calls inside a form and use withForm{} in order to prevent double clicks
	def create() {
		if (messageSource.createNewSnapshot()) {
			flash.message = 'Snapshot created'
		} else {
			flash.error = 'Error while creating the snapshot'
		}
		redirect(action: 'index')
	}

	def restore() {
		if (!params.getLong('id')) {
			flash.error = 'Missing or wrong id'
		} else {
			if (messageSource.restoreSnapshot(params.getLong('id'))) {
				flash.message = 'Snapshot restored'
			} else {
				flash.error = 'Error while restoring the snapshot'
			}
		}
		redirect(action: 'index')
	}

	def delete() {
		if (!params.getLong('id')) {
			flash.error = 'Missing or wrong id'
		} else {
			if (messageSource.deleteSnapshot(params.getLong('id'))) {
				flash.message = 'Snapshot deleted'
			} else {
				flash.error = 'Error while deleting the snapshot'
			}
		}
		redirect(action: 'index')
	}

	def update() {
		if (messageSource.update()) {
			flash.message = 'Translations updated'
		} else {
			flash.error = 'Error while updating translations'
		}
		redirect(action: 'index')
	}
}
