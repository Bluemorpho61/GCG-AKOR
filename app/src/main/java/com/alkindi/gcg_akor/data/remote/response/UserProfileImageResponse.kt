package com.alkindi.gcg_akor.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserProfileImageResponse(

	@field:SerializedName("data")
	val data: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
