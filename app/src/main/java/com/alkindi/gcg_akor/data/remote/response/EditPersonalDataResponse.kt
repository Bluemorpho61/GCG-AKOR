package com.alkindi.gcg_akor.data.remote.response

import com.google.gson.annotations.SerializedName

data class EditPersonalDataResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: EditUserPersonalData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class EditUserPersonalData(

	@field:SerializedName("Updated data")
	val updatedData: List<String?>? = null,

	@field:SerializedName("ID Member")
	val iDMember: String? = null
)
