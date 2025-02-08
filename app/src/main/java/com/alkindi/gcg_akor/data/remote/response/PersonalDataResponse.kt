package com.alkindi.gcg_akor.data.remote.response

import com.google.gson.annotations.SerializedName

data class PersonalDataResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: UserPersonalData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class UserPersonalData(

	@field:SerializedName("createdinfo")
	val createdinfo: Any? = null,

	@field:SerializedName("company_begin")
	val companyBegin: String? = null,

	@field:SerializedName("education")
	val education: Any? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("birth_date")
	val birthDate: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("file_ktp")
	val fileKtp: Any? = null,

	@field:SerializedName("mbrgroup")
	val mbrgroup: String? = null,

	@field:SerializedName("photo_file")
	val photoFile: String? = null,

	@field:SerializedName("bank")
	val bank: String? = null,

	@field:SerializedName("company")
	val company: String? = null,

	@field:SerializedName("termeff")
	val termeff: Any? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("termination")
	val termination: String? = null,

	@field:SerializedName("company_last")
	val companyLast: Any? = null,

	@field:SerializedName("nama_rek")
	val namaRek: Any? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("ktp")
	val ktp: String? = null,

	@field:SerializedName("birth_place")
	val birthPlace: String? = null,

	@field:SerializedName("termreason")
	val termreason: Any? = null,

	@field:SerializedName("file_slip")
	val fileSlip: Any? = null,

	@field:SerializedName("religion")
	val religion: Any? = null,

	@field:SerializedName("mbr_empno2")
	val mbrEmpno2: Any? = null,

	@field:SerializedName("marital_status")
	val maritalStatus: Any? = null,

	@field:SerializedName("file_sk")
	val fileSk: Any? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("norek")
	val norek: String? = null,

	@field:SerializedName("mbrid")
	val mbrid: String? = null,

	@field:SerializedName("modifiedinfo")
	val modifiedinfo: String? = null,

	@field:SerializedName("mbr_position")
	val mbrPosition: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("termtype")
	val termtype: Any? = null,

	@field:SerializedName("mbr_unit")
	val mbrUnit: Any? = null,

	@field:SerializedName("mbr_empno")
	val mbrEmpno: String? = null
)
