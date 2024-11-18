package com.alkindi.gcg_akor.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alkindi.gcg_akor.data.model.ViewModelFactory
import com.alkindi.gcg_akor.databinding.ActivityDetailHistoryPinjamanBinding
import com.alkindi.gcg_akor.ui.viewmodel.DetailHistoryPinjamanViewModel
import com.alkindi.gcg_akor.utils.FormatterAngka

class DetailHistoryPinjamanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHistoryPinjamanBinding
    private lateinit var extraDocnum: String
    private val detailHistoryPinjamanViewModel: DetailHistoryPinjamanViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailHistoryPinjamanBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        extraDocnum = intent.getStringExtra(EXTRA_DOCNUM).toString()
        checkLoading()
        fetchDetailData()
        showDetailData()


        binding.btnBack.setOnClickListener {
            finish()
        }


    }

    private fun showDetailData() {
        detailHistoryPinjamanViewModel.detailHistoryPinjamanResponse.observe(this) { res ->
            val tgl = res.data?.get(0)?.docDate.toString()
            val nominalPinjaman = res.data?.get(0)?.amount.toString()
            val nominalAdminstrasi = res.data?.get(0)?.amAdm.toString()
            val nominalAsuransi = res.data?.get(0)?.asuransi.toString()
            val nominalProvisi = res.data?.get(0)?.provisi.toString()
            val nominalDanaDiterima = res.data?.get(0)?.danaCair.toString()

            val parsedTgl = FormatterAngka.dateFormatForDetail(tgl)
            val parsedNominalPinjaman =
                FormatterAngka.formatterAngkaRibuanDouble(nominalPinjaman.toDouble())
            val parsedProvisi = FormatterAngka.formatterAngkaRibuanDouble(nominalProvisi.toDouble())
            val parsedNominalAdministrasi =
                FormatterAngka.formatterAngkaRibuanDouble(nominalAdminstrasi.toDouble())
            val parsedNominalAsuransi =
                FormatterAngka.formatterAngkaRibuanDouble(nominalAsuransi.toDouble())
            val parsedNominalDanaDiterima =
                FormatterAngka.formatterAngkaRibuanDouble(nominalDanaDiterima.toDouble())

            binding.tvStatusPinjaman.text = res.data?.get(0)?.statusPjm ?: "Kosong"
            binding.tvTgl.text = parsedTgl
            binding.tvNoRef.text = res.data?.get(0)?.docNum
            binding.tvTipePinjaman.text = res.data?.get(0)?.pjmCode
            binding.tvNominaJumlahPinjaman.text = parsedNominalPinjaman
            binding.tvNominalBiayaAdministrasi.text = parsedNominalAdministrasi
            binding.tvNominalAsuransi.text = parsedNominalAsuransi
            binding.tvNominalProvinsi.text = parsedProvisi
            binding.tvNominalDanaDiterima.text = parsedNominalDanaDiterima
        }
    }

    private fun checkLoading() {
        detailHistoryPinjamanViewModel.isLoading.observe(this) { res ->
            showLoading(res)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun fetchDetailData() {
        detailHistoryPinjamanViewModel.getDetailHistoryPinjaman(extraDocnum)
    }


    companion object {
        private val TAG = DetailHistoryPinjamanActivity::class.java.simpleName
        const val EXTRA_DOCNUM = "extra_docnum"
    }
}