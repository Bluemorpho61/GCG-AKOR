package com.alkindi.gcg_akor.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailHistoryPinjamanResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<DetailHistoryPinjamanItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DetailHistoryPinjamanItem(

	@field:SerializedName("stl_doc_num")
	val stlDocNum: Any? = null,

	@field:SerializedName("loan_code")
	val loanCode: String? = null,

	@field:SerializedName("inct_code")
	val inctCode: Any? = null,

	@field:SerializedName("tot_pjm")
	val totPjm: Any? = null,

	@field:SerializedName("plafon_am")
	val plafonAm: Any? = null,

	@field:SerializedName("aprinfo")
	val aprinfo: Any? = null,

	@field:SerializedName("doc_num")
	val docNum: String? = null,

	@field:SerializedName("bonus")
	val bonus: Any? = null,

	@field:SerializedName("slip_gaji")
	val slipGaji: Any? = null,

	@field:SerializedName("int_am")
	val intAm: Any? = null,

	@field:SerializedName("mbr_company")
	val mbrCompany: String? = null,

	@field:SerializedName("tot_am")
	val totAm: Any? = null,

	@field:SerializedName("settlement_fee")
	val settlementFee: Any? = null,

	@field:SerializedName("settlement_by")
	val settlementBy: Any? = null,

	@field:SerializedName("dana_cair")
	val danaCair: Any? = null,

	@field:SerializedName("apr")
	val apr: Any? = null,

	@field:SerializedName("jakhu")
	val jakhu: Any? = null,

	@field:SerializedName("settlement_date")
	val settlementDate: Any? = null,

	@field:SerializedName("bonus_date")
	val bonusDate: Any? = null,

	@field:SerializedName("am_adm")
	val amAdm: Any? = null,

	@field:SerializedName("int_type")
	val intType: Any? = null,

	@field:SerializedName("gaji")
	val gaji: Any? = null,

	@field:SerializedName("status_pjm")
	val statusPjm: String? = null,

	@field:SerializedName("granted_by")
	val grantedBy: Any? = null,

	@field:SerializedName("gaji13")
	val gaji13: Any? = null,

	@field:SerializedName("mbr_mbrid")
	val mbrMbrid: String? = null,

	@field:SerializedName("settlement_jasa")
	val settlementJasa: Any? = null,

	@field:SerializedName("approved_by")
	val approvedBy: Any? = null,

	@field:SerializedName("apr_date")
	val aprDate: Any? = null,

	@field:SerializedName("docref")
	val docref: Any? = null,

	@field:SerializedName("modifiedinfo")
	val modifiedinfo: Any? = null,

	@field:SerializedName("pjm_code")
	val pjmCode: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("settlement_amount")
	val settlementAmount: Any? = null,

	@field:SerializedName("curency")
	val curency: Any? = null,

	@field:SerializedName("stl_apr_status")
	val stlAprStatus: Any? = null,

	@field:SerializedName("jasa_totpjm")
	val jasaTotpjm: Any? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("batas_angsuran")
	val batasAngsuran: Any? = null,

	@field:SerializedName("createdinfo")
	val createdinfo: String? = null,

	@field:SerializedName("no_atasan")
	val noAtasan: Any? = null,

	@field:SerializedName("stldesc")
	val stldesc: Any? = null,

	@field:SerializedName("slip_bonus")
	val slipBonus: Any? = null,

	@field:SerializedName("apr_stl_by")
	val aprStlBy: Any? = null,

	@field:SerializedName("asuransi")
	val asuransi: Any? = null,

	@field:SerializedName("settlement")
	val settlement: Any? = null,

	@field:SerializedName("slip_gaji13")
	val slipGaji13: Any? = null,

	@field:SerializedName("proid")
	val proid: Any? = null,

	@field:SerializedName("date_cair")
	val dateCair: Any? = null,

	@field:SerializedName("term")
	val term: Any? = null,

	@field:SerializedName("pay_type")
	val payType: Any? = null,

	@field:SerializedName("accum")
	val accum: Any? = null,

	@field:SerializedName("evm")
	val evm: Any? = null,

	@field:SerializedName("amount")
	val amount: Any? = null,

	@field:SerializedName("provisi")
	val provisi: Any? = null,

	@field:SerializedName("settlement_pokok")
	val settlementPokok: Any? = null,

	@field:SerializedName("simp_khusus")
	val simpKhusus: Any? = null,

	@field:SerializedName("evy")
	val evy: Any? = null,

	@field:SerializedName("svm")
	val svm: Any? = null,

	@field:SerializedName("angsuran")
	val angsuran: Any? = null,

	@field:SerializedName("pot_pribadi")
	val potPribadi: Any? = null,

	@field:SerializedName("lon_desc")
	val lonDesc: Any? = null,

	@field:SerializedName("jasa")
	val jasa: Any? = null,

	@field:SerializedName("max_angsuran")
	val maxAngsuran: Any? = null,

	@field:SerializedName("svy")
	val svy: Any? = null,

	@field:SerializedName("doc_date")
	val docDate: String? = null,

	@field:SerializedName("apr_stl_date")
	val aprStlDate: Any? = null
)
