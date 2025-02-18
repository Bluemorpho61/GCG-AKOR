package com.alkindi.gcg_akor.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alkindi.gcg_akor.data.local.model.ProcessedTarikSimp
import com.alkindi.gcg_akor.data.local.model.SimpTypeWNominal
import com.alkindi.gcg_akor.data.model.ViewModelFactory
import com.alkindi.gcg_akor.databinding.ActivityTarikNominalSimpananBinding
import com.alkindi.gcg_akor.ui.viewmodel.TarikNominalSimpananViewModel
import com.alkindi.gcg_akor.utils.AndroidUIHelper
import com.alkindi.gcg_akor.utils.FormatterAngka
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat
import java.util.Date

class TarikNominalSimpananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTarikNominalSimpananBinding
    private lateinit var userMBRID: String
    private lateinit var tipeTransaksi: String
    private lateinit var extraData: ProcessedTarikSimp

    private val tarikNominalSimpananViewModel: TarikNominalSimpananViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTarikNominalSimpananBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        logicAturNominal()
        getDataFromPreviousActivity()
        checkLoading()
        chipButtonLogic()
        getUserMBRID()
        getTarikSimpananAPIResponse()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.layoutCatatan.setOnClickListener {
            binding.edtCatatan.requestFocus()
            val keybInput = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keybInput.showSoftInput(binding.edtCatatan, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.btnConfirmTarikSimpanan.setOnClickListener {
            confirmTarikSimpanan()
        }
    }

    private fun getTarikSimpananAPIResponse() {
        tarikNominalSimpananViewModel.tarikNominalSimpananResponse.observe(this) { res ->
            if (res.code == 200) {
                val docnum = res.docnum!!
                AndroidUIHelper.showWarningToastShort(this, "Tarik Simpanan Berhasil")
                val extraData = ProcessedTarikSimp(docnum)
                val toConfirmPage = Intent(
                    this@TarikNominalSimpananActivity,
                    TarikSimpananProcessedActivity::class.java
                ).apply {
                    putExtra(TarikSimpananProcessedActivity.EXTRA_PROCESSED_TARIK_SIMP, extraData)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                startActivity(toConfirmPage)
            } else {
                AndroidUIHelper.showAlertDialog(
                    this,
                    "ERROR ${res.code}: Tidak Dapat Melakukan Tarik Simpanan",
                    "${res.message}"
                )
            }
        }
    }

    private fun getUserMBRID() {
        tarikNominalSimpananViewModel.getSession().observe(this) {
            userMBRID = it.username
        }
    }

    private fun checkLoading() {
        tarikNominalSimpananViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun confirmTarikSimpanan() {
        val nominal = binding.tvNominalTarikSimpanan.text.toString()
        val convertedNominal = FormatterAngka.formatterRibuanKeLong(nominal)
        val tipeSimpanan = binding.tvTypeSimpanan.text
        val catatanTransaksi = binding.edtCatatan.text.toString()
        val simpananYgTersedia = binding.tvNominalTipeSimpanan.text.toString()
        val convertedNominalYgTersedia = FormatterAngka.formatterRibuanKeLong(simpananYgTersedia)
        val tglSaatIni = Date()
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = dateFormatter.format(tglSaatIni)
        when (tipeSimpanan) {
            "Simpanan Sukarela" -> {
                tipeTransaksi = "SS"
                Log.d(TAG, "Tipe Simpanan: $tipeSimpanan")
            }

            "Simpanan Khusus" -> {
                tipeTransaksi = "SK"
                Log.d(TAG, "Tipe Simpanan: $tipeSimpanan")
            }

            "Simpanan Khusus Pagu" -> {
                tipeTransaksi = "SKP"
                Log.d(TAG, "Tipe Simpanan: $tipeSimpanan")
            }
        }
        tarikNominalSimpananViewModel.tarikNominalSimpanan(
            mbrid = userMBRID,
            nominalSimpanan = convertedNominal.toString(),
            catatan = catatanTransaksi,
            tipeSimpanan = tipeTransaksi,
            simpananYgTersedia = convertedNominalYgTersedia.toString(),
            transDate = formattedDate
        )
        extraData = ProcessedTarikSimp(
            ""
        )
    }

    private fun getDataFromPreviousActivity() {
        val simpData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<SimpTypeWNominal>(
                EXTRA_SIMP_TYPE,
                SimpTypeWNominal::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_SIMP_TYPE)
        }

        if (simpData == null) {
            AndroidUIHelper.showAlertDialog(
                this,
                "Error",
                "Tidak dapat melanjutkan tarik simpanan. Silahkan coba beberapa saat lagi"
            )
            Log.e(TAG, "Data dari intent sebelumnya tidak dapat ditemukan")
            finish()
        }

        binding.tvNominalTipeSimpanan.text = simpData!!.nominal ?: "Data is null"
        binding.tvTypeSimpanan.text = simpData.tipeSimpanan ?: "Data is null"
    }

    private fun chipButtonLogic() {
        binding.chip100rb.setOnClickListener {
            updateNominalValue(binding.chip100rb)
        }
        binding.chip300rb.setOnClickListener {
            updateNominalValue(binding.chip300rb)
        }
        binding.chip500rb.setOnClickListener {
            updateNominalValue(binding.chip500rb)
        }
    }

    private fun updateNominalValue(chip: Chip) {
        val nilaiChip = chip.text.toString().replace("Rp", "").replace(".", "").replace(" ", "")
        val formattedValue = FormatterAngka.formatterAngkaRibuan(nilaiChip.toInt())
        binding.tvNominalTarikSimpanan.text = formattedValue
        binding.btnConfirmTarikSimpanan.visibility = View.VISIBLE
    }

    private fun logicAturNominal() {
        binding.btnKurangiNominal.visibility = View.GONE

        binding.btnTambahNominal.setOnClickListener {
            tambahNominal()
        }

        binding.btnKurangiNominal.setOnClickListener {
            kurangiNominal()
        }
    }

    private fun kurangiNominal() {
        val jmlNominal = binding.tvNominalTarikSimpanan.text
        val currentNumber = jmlNominal.toString()
        val newNumber = FormatterAngka.formatterRibuanKeInt(currentNumber) - 10000
        binding.tvNominalTarikSimpanan.text = FormatterAngka.formatterAngkaRibuan(newNumber)
        if (binding.tvNominalTarikSimpanan.text == "0") {
            binding.btnKurangiNominal.visibility = View.GONE
            binding.btnConfirmTarikSimpanan.visibility = View.GONE
        }
    }

    private fun tambahNominal() {
        val jmlNominal = binding.tvNominalTarikSimpanan.text
        val currentNumber = jmlNominal.toString()
        val newNumber = FormatterAngka.formatterRibuanKeInt(currentNumber) + 10000
        binding.tvNominalTarikSimpanan.text = FormatterAngka.formatterAngkaRibuan(newNumber)
        binding.btnKurangiNominal.visibility = View.VISIBLE
        binding.btnConfirmTarikSimpanan.visibility = View.VISIBLE
    }

    companion object {
        private val TAG = TarikNominalSimpananActivity::class.java.simpleName
        const val EXTRA_SIMP_TYPE = "extra_simp_type"
    }
}