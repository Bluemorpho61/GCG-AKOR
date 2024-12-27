package com.alkindi.gcg_akor.data.remote.response

import com.google.gson.annotations.SerializedName

data class HitungAdmPinjamanResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: AdmPinjamanItem? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AdmPinjamanItem(

	@field:SerializedName("tot_pjm")
	val totPjm: Int? = null,

	@field:SerializedName("provisi")
	val provisi: Any? = null,

	@field:SerializedName("simp_khusus")
	val simpKhusus: Int? = null,

	@field:SerializedName("adm")
	val adm: Int? = null,

	@field:SerializedName("asuransi")
	val asuransi: Any? = null,

	@field:SerializedName("angsuran")
	val angsuran: Any? = null,

	@field:SerializedName("batang")
	val batang: Any? = null,

	@field:SerializedName("maksur")
	val maksur: Any? = null,

	@field:SerializedName("total")
	val total: Any? = null,

	@field:SerializedName("minsimp")
	val minsimp: Any? = null,

	@field:SerializedName("jasa")
	val jasa: Any? = null,

	@field:SerializedName("dana_cair")
	val danaCair: Any? = null,

	@field:SerializedName("jas")
	val jas: Any? = null,

	@field:SerializedName("jasa_totpjm")
	val jasaTotpjm: Any? = null
)
