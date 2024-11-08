package com.alkindi.gcg_akor.utils

import java.text.NumberFormat
import java.util.Locale

object FormatterAngka {

    fun formatterAngkaRibuan(angka: Int): String {
        val formattedNilai = NumberFormat.getInstance(Locale("ind", "ID"))
        return formattedNilai.format(angka)
    }

    fun formatterRibuanKeInt(angka: String): Int {
        return angka.replace(".", "").replace(" ", "").toInt()
    }

    fun formatterAngkaRibuanDouble(angka: Double): String {
        val formattedNilai = NumberFormat.getInstance(Locale("ind", "ID"))
        return formattedNilai.format(angka)
    }

    fun formatterRibuanKeIntDouble(angka: String): Double {
        return angka.replace(".", "").replace(" ", "").toDouble()
    }

}