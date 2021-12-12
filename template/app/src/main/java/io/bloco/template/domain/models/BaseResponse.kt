package io.bloco.template.domain.models

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(

    @field:SerializedName("meta")
    val meta: Meta? = null,

    @field:SerializedName("content")
    val content: T? = null
)



data class Meta(

    @field:SerializedName("pageNumber")
    val pageNumber: Int? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("pageSize")
    val pageSize: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)
