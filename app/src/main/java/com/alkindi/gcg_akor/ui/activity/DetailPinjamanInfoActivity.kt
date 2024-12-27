package com.alkindi.gcg_akor.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.alkindi.gcg_akor.data.model.ViewModelFactory
import com.alkindi.gcg_akor.databinding.ActivityDetailPinjamanInfoBinding
import com.alkindi.gcg_akor.ui.viewmodel.DetailPinjamanInfoViewModel
import com.alkindi.gcg_akor.utils.FormatterAngka
import kotlinx.coroutines.launch

class DetailPinjamanInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPinjamanInfoBinding
    private lateinit var extraDocnum: String
    private val detailPinjamanInfoViewModel: DetailPinjamanInfoViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPinjamanInfoBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        extraDocnum = intent.getStringExtra(EXTRA_DOCNUM).toString()

        fetchData()
        showData()
    }

    private fun showData() {
        detailPinjamanInfoViewModel.detailHistoryPinjamanInfoResponse.observe(this) { res ->
            val jasa = res.data?.get(0)?.jasa
            val danaCair = res.data?.get(0)?.danaCair
            val mbrid = res.data?.get(0)?.mbrMbrid.toString()
            val tglTransaksi = res.data?.get(0)?.docDate.toString()
            val term = res.data?.get(0)?.term.toString()
            val amount = res.data?.get(0)?.amount
            val nominalAdministrasi = res.data?.get(0)?.amAdm
            val tipePotongan = res.data?.get(0)?.loanCode.toString()
            val namaUser = res.data?.get(0)?.name.toString()
            val docnum = res.data?.get(0)?.docNum.toString()
            val tipePinjaman = res.data?.get(0)?.pjmCode.toString()
            val nominalTotal = res.data?.get(0)?.totAm
            val nominalAsuransi = res.data?.get(0)?.asuransi
            val nominalProvisi = res.data?.get(0)?.provisi
            val nominalPotPri = res.data?.get(0)?.potPribadi
            val nominalAngsuranBln = res.data?.get(0)?.angsuran
            val nominalPotongan = res.data?.get(0)?.gaji

            binding.tvTipePinjaman.text = tipePinjaman
            binding.tvNoRef.text = docnum
            binding.tvNominaJumlahPinjaman.text =
                FormatterAngka.formatterAngkaRibuanDouble(amount.toString().toDouble())
            binding.tvNamaMember.text = namaUser
            binding.tvTgl.text = tglTransaksi
            binding.tvTipePotongan.text = tipePotongan
            binding.tvNominalProvinsi.text =
                FormatterAngka.formatterAngkaRibuanDouble(nominalProvisi.toString().toDouble())
            binding.tvNominalBiayaAdministrasi.text =
                FormatterAngka.formatterAngkaRibuanDouble(nominalAdministrasi.toString().toDouble())
            binding.tvNominalDanaDiterima.text =
                FormatterAngka.formatterAngkaRibuanDouble(danaCair.toString().toDouble())
            binding.tvTenor.text = FormatterAngka.penghilangNilaiKoma(term)
            binding.tvIDMember.text = mbrid
            binding.tvTipePinjamanInDetail.text = tipePinjaman
            binding.tvNominalJumlahJasa.text =
                FormatterAngka.formatterAngkaRibuanDouble(jasa.toString().toDouble())
            binding.tvNominalProvinsi.text =
                FormatterAngka.formatterAngkaRibuanDouble(nominalProvisi.toString().toDouble())
            binding.tvNominalJmlPinjaman.text =
                FormatterAngka.formatterAngkaRibuanDouble(amount.toString().toDouble())
            binding.tvNominalPot.text =
                FormatterAngka.formatterAngkaRibuanDouble(nominalPotongan.toString().toDouble())
            binding.tvNominalPotPribadi.text =
                FormatterAngka.formatterAngkaRibuanDouble(nominalPotPri.toString().toDouble())
            binding.tvNominalTotal.text =
                FormatterAngka.formatterAngkaRibuanDouble(nominalTotal.toString().toDouble())
            binding.tvNominalProvinsi.text =
                FormatterAngka.formatterAngkaRibuanDouble(nominalAsuransi.toString().toDouble())
            binding.tvAngsuranBln.text =
                FormatterAngka.formatterAngkaRibuanDouble(nominalAngsuranBln.toString().toDouble())
        }
    }


    private fun fetchData() {
        lifecycleScope.launch {
            detailPinjamanInfoViewModel.getDetailPinjamanInfo(extraDocnum)
        }

    }


    companion object {
        private val TAG = DetailPinjamanInfoActivity::class.java.simpleName
        const val EXTRA_DOCNUM = "extra_docnum"
    }
}