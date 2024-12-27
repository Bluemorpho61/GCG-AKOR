package com.alkindi.gcg_akor.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

object FormatterAngka {

    fun formatterAngkaRibuan(angka: Int?): String {
        val formattedNilai = NumberFormat.getInstance(Locale("ind", "ID"))
        return formattedNilai.format(angka)
    }

    fun formatterRibuanKeInt(angka: String): Int {
        return angka.replace(".", "").replace(" ", "").replace(",", "").toInt()
    }

    fun formatterAngkaRibuanDouble(angka: Double?): String {
//        val formattedNilai = NumberFormat.getInstance(Locale("ind", "ID"))
//        return formattedNilai.format(angka)
        val symbols = DecimalFormatSymbols(Locale("id", "ID")).apply {
            decimalSeparator = ','
            groupingSeparator = '.'
        }
        val decimalFormat = DecimalFormat("#,##0.00", symbols)
        return decimalFormat.format(angka)
    }

    fun numberFormatterIntUntukHitungAdm(angka: Int?): String {
        val formattedValue = String.format("%,d", angka!!.toLong())
        return formattedValue
    }

    fun numberFormatterFloatUntukHitungAdm(angka: Any?): String {
        val symbols = DecimalFormatSymbols(Locale("id", "ID")).apply {
            decimalSeparator = ','
            groupingSeparator = '.'
        }
        val decimalFormat = DecimalFormat("#,##0.00", symbols)
        return decimalFormat.format(angka)
    }

    fun penghilangNilaiKoma(angka: String?): String {
        return if (angka != null) {
            val number = angka.toDoubleOrNull()
            number?.toInt()?.toString() ?: "0"
        } else {
            "0"
        }
    }

    fun dateFormatForDetail(dateString: String): String {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date ?: "")
    }

}