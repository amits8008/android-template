package io.bloco.template.data.api

import javax.inject.Inject

class ApiHelper @Inject constructor(private val templateService: TemplateService) {
    suspend fun getContactList(pageNumber: Int) = templateService.getContactList(pageNumber)
    suspend fun unstarContact(contactId: Int) = templateService.unstartContact(contactId)
    suspend fun starContact(contactId: Int) = templateService.startContact(contactId)
}