package com.alkindi.gcg_akor.data.local.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class UserModel(
    val username: String,
    val password: String,
    val isLogin: Boolean
)

@Parcelize
data class SimpTypeWNominal(
    val tipeSimpanan: String,
    val nominal: String
) : Parcelable

@Parcelize
data class PinjDocnum(
    val docNum: String
) : Parcelable

@Parcelize
data class ProcessedTarikSimp(
    val docnum: String?,
) : Parcelable

@Parcelize
data class UserProfile(
    val namaUser: String?,
    val emailUser: String?,
    val noHPUser: String?
) : Parcelable

@Parcelize
data class InputtedBiayaPot(
    val tipePinjaman: String,
    val tipePotongan: String,
    val nomTipePot: String,
    val nomPotPri: String,
    val noAtasan: String,
    val jmlTenor: String,
    val tanggalPencairan: String?,
    val tanggalBonus: String?,
    val nominalPinjaman: String?
) : Parcelable
