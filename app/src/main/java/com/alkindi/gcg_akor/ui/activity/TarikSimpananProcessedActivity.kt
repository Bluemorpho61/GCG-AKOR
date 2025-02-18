package com.alkindi.gcg_akor.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.alkindi.gcg_akor.data.local.model.ProcessedTarikSimp
import com.alkindi.gcg_akor.data.model.ViewModelFactory
import com.alkindi.gcg_akor.databinding.ActivityTarikSimpananProcessedBinding
import com.alkindi.gcg_akor.ui.viewmodel.TarikSimpananProcessedViewModel
import com.alkindi.gcg_akor.utils.AndroidUIHelper
import com.alkindi.gcg_akor.utils.FormatterAngka
import kotlinx.coroutines.launch

class TarikSimpananProcessedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTarikSimpananProcessedBinding
    private val tarikSimpananProcessedViewModel: TarikSimpananProcessedViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }
    private var extraData: ProcessedTarikSimp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTarikSimpananProcessedBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnToHome.setOnClickListener {
            val intent = Intent(this@TarikSimpananProcessedActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        checkLoading()
        getDataFromPreviousActivity()
        getTarikSimpananInfoData()
        showPullTransactionData()
    }

    private fun checkLoading() {
        tarikSimpananProcessedViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading)
            binding.progressBar.visibility = View.VISIBLE
        else
            binding.progressBar.visibility = View.GONE
    }

    private fun showPullTransactionData() {
        tarikSimpananProcessedViewModel.tarikSimpananProcessed.observe(this) { res ->
            if (res.code == 200) {
                when (res.data?.stp) {
                    "SS" -> {
                        binding.tvTipeSimpanan.text = "Simpanan Sukarela"
                    }

                    "SK" -> {
                        binding.tvTipeSimpanan.text = "Simpanan Khusus"
                    }

                    "SKP" -> {
                        binding.tvTipeSimpanan.text = "Simpanan Khusus Pagu"
                    }
                }
                val nominalDitarik = res.data?.amount
                val tglTransaksi =res.data?.transDate
                binding.tvNominalYangDitarik.text = FormatterAngka.formatterAngkaRibuan(nominalDitarik!!.toInt())
                binding.tvTglTransaksi.text =FormatterAngka.dateFormatForTarikSimp(tglTransaksi!!)

            } else {
                Log.e(TAG, "Can't fetch processed tarik simp data! ${Log.ERROR}")
                AndroidUIHelper.showAlertDialog(
                    this,
                    "Error!",
                    "Unable to fetch tarik simpanan data!"
                )
                return@observe
            }
        }
    }

    private fun getTarikSimpananInfoData() {
        lifecycleScope.launch {
            tarikSimpananProcessedViewModel.getPullTransactionInfo(extraData!!.docnum.toString())
        }
    }

    private fun getDataFromPreviousActivity() {
        val tarikSimpInfo = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(
                EXTRA_PROCESSED_TARIK_SIMP,
                ProcessedTarikSimp::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_PROCESSED_TARIK_SIMP)
        }

        if (tarikSimpInfo == null) {
            AndroidUIHelper.showAlertDialog(
                this,
                "Error",
                "Tidak dapat mengambil info data dari transaksi saat ini"
            )
            Log.e(TAG, "Tidak dapat mengambil data tarik simpanan dari activity sebelumnya")
            finish()
        }

        extraData = tarikSimpInfo
//        binding.tvNominalYangDitarik.text = tarikSimpInfo!!.nominal
//        binding.tvTglTransaksi.text =tarikSimpInfo.tglTransaksi
        binding.tvDocnum.text = extraData!!.docnum


//        when (tarikSimpInfo.tipeSimpanan) {
//            "SS" -> {
//                binding.tvTipeSimpanan.text = "Simpanan Sukarela"
//            }
//
//            "SK" -> {
//                binding.tvTipeSimpanan.text = "Simpanan Khusus"
//            }
//
//            "SKP" -> {
//                binding.tvTipeSimpanan.text = "Simpanan Khusus Pagu"
//            }
//        }
    }

    companion object {
        private val TAG = TarikSimpananProcessedActivity::class.java.simpleName
        const val EXTRA_PROCESSED_TARIK_SIMP = "extra_processed_tarik_simp"
    }
}