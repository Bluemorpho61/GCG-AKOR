package com.alkindi.gcg_akor.data.remote.response

import com.google.gson.annotations.SerializedName

data class AjukanPinjamanResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: DataPengajuanPinjaman? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataPengajuanPinjaman(

	@field:SerializedName("doc_num")
	val docNum: String? = null,

	@field:SerializedName("company")
	val company: String? = null
)
