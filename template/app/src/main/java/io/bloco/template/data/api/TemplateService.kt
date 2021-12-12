package io.bloco.template.data.api

import io.bloco.template.domain.models.Contact
import io.bloco.template.domain.models.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TemplateService {
    @GET("contacts")
    suspend fun getContactList(@Query("pageNumber") pageNumber: Int): Response<BaseResponse<List<Contact>>>

    @POST("unstar/{id}")
    suspend fun unstartContact(@Path("id") contactId: Int): Response<BaseResponse<Contact>>

    @POST("star/{id}")
    suspend fun startContact(@Path("id") contactId: Int): Response<BaseResponse<Contact>>


}