package io.bloco.template.domain.models

import com.google.gson.annotations.SerializedName

data class Contact(

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("isStarred")
    var isStarred: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null
)