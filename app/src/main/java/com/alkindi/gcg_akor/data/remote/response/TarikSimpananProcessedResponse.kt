package com.alkindi.gcg_akor.data.remote.response

import com.google.gson.annotations.SerializedName

data class TarikSimpananProcessedResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: TarikSimpananProcessedData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class TarikSimpananProcessedData(

	@field:SerializedName("createdinfo")
	val createdinfo: Any? = null,

	@field:SerializedName("inct_code")
	val inctCode: Any? = null,

	@field:SerializedName("apr_lon")
	val aprLon: Any? = null,

	@field:SerializedName("trans_date")
	val transDate: String? = null,

	@field:SerializedName("amount")
	val amount: Double? = null,

	@field:SerializedName("apr")
	val apr: Any? = null,

	@field:SerializedName("aprinfo")
	val aprinfo: Any? = null,

	@field:SerializedName("status_smp")
	val statusSmp: Any? = null,

	@field:SerializedName("doc_num")
	val docNum: String? = null,

	@field:SerializedName("txn_amount")
	val txnAmount: Any? = null,

	@field:SerializedName("stp")
	val stp: String? = null,

	@field:SerializedName("mbr_mbrid")
	val mbrMbrid: String? = null,

	@field:SerializedName("tgl_cair")
	val tglCair: Any? = null,

	@field:SerializedName("apr_date")
	val aprDate: Any? = null,

	@field:SerializedName("txn_type")
	val txnType: Any? = null,

	@field:SerializedName("mbr_company")
	val mbrCompany: String? = null,

	@field:SerializedName("apr_by")
	val aprBy: Any? = null,

	@field:SerializedName("modifiedinfo")
	val modifiedinfo: Any? = null,

	@field:SerializedName("doc_date")
	val docDate: String? = null,

	@field:SerializedName("pay_type")
	val payType: Any? = null,

	@field:SerializedName("ket")
	val ket: String? = null,

	@field:SerializedName("status")
	val status: Any? = null
)
