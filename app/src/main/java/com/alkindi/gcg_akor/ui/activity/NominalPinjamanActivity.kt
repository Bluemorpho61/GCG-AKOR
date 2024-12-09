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
import com.alkindi.gcg_akor.R
import com.alkindi.gcg_akor.data.local.model.InputtedBiayaPot
import com.alkindi.gcg_akor.data.model.ViewModelFactory
import com.alkindi.gcg_akor.databinding.ActivityNominalPinjamanBinding
import com.alkindi.gcg_akor.ui.viewmodel.NominalPinjamanViewModel
import com.alkindi.gcg_akor.utils.AndroidUIHelper
import com.alkindi.gcg_akor.utils.FormatterAngka
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class NominalPinjamanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNominalPinjamanBinding
    private lateinit var userID: String
    private var tipePinjaman: String? = null
    private var dataPotonganExtra: InputtedBiayaPot? = null
    private val nominalPinjamanViewModel: NominalPinjamanViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNominalPinjamanBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getSession()
        getDataFromPreviousActivity()
        checkJenisPinjaman()
        btnAturNominalLogic()
        chipButtonLogic()
        checkLoading()
        showRincianPotongan()
        observeResponsePengajuan()
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnConfirmPinjaman.setOnClickListener {
//            startActivity(
//                Intent(
//                    this@NominalPinjamanActivity,
//                    DetailPinjamanInfoActivity::class.java
//                )
//            )
            unggahDataPengajuanPinjaman()
        }

        binding.btnHitungBiaya.setOnClickListener {
            hitungBiayaBtnPressed()
        }
    }

    private fun checkJenisPinjaman() {
        val tipePinjaman = binding.tvTipePinjaman.text.toString()
        this.tipePinjaman = tipePinjaman
//        if (tipePinjaman == "Rumah" || tipePinjaman == "Motor") {
//            binding.btnHitungBiaya.visibility = View.GONE
//            binding.btnConfirmPinjaman.visibility = View.VISIBLE
//        }else{
//            binding.btnHitungBiaya.visibility = View.VISIBLE
//            binding.btnConfirmPinjaman.visibility = View.GONE
//        }
    }

    private fun observeResponsePengajuan() {
        if (tipePinjaman == "Rumah" || tipePinjaman == "Motor") {
//            nominalPinjamanViewModel
        } else {
            nominalPinjamanViewModel.uploadPengajuanPinjaman.observe(this) { res ->
                if (res.code == 200) {
                    val toDetailPinjaman = Intent(this, DetailPinjamanInfoActivity::class.java)
                    AndroidUIHelper.showWarningToastShort(this, "Pengajuan pinjaman berhasil!")
                    startActivity(toDetailPinjaman)
                } else {
                    AndroidUIHelper.showWarningToastShort(
                        this,
                        "Error ${res.code}: ${res.message.toString()}"
                    )
                    return@observe
                }
            }
        }
    }

    private fun unggahDataPengajuanPinjaman() {
        val tglSaatIni = Date()
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = dateFormatter.format(tglSaatIni)

        nominalPinjamanViewModel.hitungAdmResponse.observe(this) { res ->
            lifecycleScope.launch {
                nominalPinjamanViewModel.ajukanPinjaman(
                    mbrid = userID,
                    amount = FormatterAngka.formatterRibuanKeInt(binding.tvJumlahNominal.text.toString())
                        .toString(),
                    term = dataPotonganExtra?.jmlTenor ?: "",
                    docDate = formattedDate.toString(),
                    angsuran = res.data?.angsuran.toString(),
                    asuransi = res.data?.asuransi.toString(),
                    adm = res.data?.adm.toString(),
                    maksAngsuran = res.data?.maksur.toString(),
                    batasAngsuran = res.data?.batang.toString(),
                    danaCair = res.data?.danaCair.toString(),
                    dateBonus = dataPotonganExtra?.tanggalBonus,
                    dateCair = dataPotonganExtra?.tanggalBonus,
                    saldoPjm = res.data?.totPjm.toString(),
                    simpKhusus = res.data?.simpKhusus.toString(),
                    jasa = res.data?.jasa.toString(),
                    loancode = dataPotonganExtra?.tipePinjaman.toString(),
                    gaji = dataPotonganExtra?.nomTipePot.toString(),
                    potonganPribadi = dataPotonganExtra?.nomPotPri.toString(),
                    noAtasan = dataPotonganExtra?.noAtasan.toString(),
                    jasaTotPjm = res.data?.jasaTotpjm.toString(),
                    pjmCode = dataPotonganExtra?.tipePotongan.toString(),
                    provisi = res.data?.provisi.toString(),
                    minimalSimpanan = res.data?.minsimp.toString(),
                    totalAmount = res.data?.total.toString()
                )
            }
        }


    }

    private fun showRincianPotongan() {
        if (tipePinjaman == "Rumah" || tipePinjaman == "Motor") {
            nominalPinjamanViewModel.hitungAdmResponseLain.observe(this) { res ->
                if (res.code == 200) {
                    binding.cardviewRincian.visibility = View.VISIBLE
                    binding.btnHitungBiaya.visibility = View.GONE
                    binding.btnConfirmPinjaman.visibility = View.VISIBLE

                    val nominalSimpKhusus = res.data?.simpKhusus.toString()
                    val nominalPinjamanDiajukan =
                        FormatterAngka.formatterRibuanKeInt(binding.tvJumlahNominal.text.toString())
                    val nominalAdministrasi = res.data?.adm.toString()
                    val nominalAsuransi = res.data?.asuransi.toString()
                    val nominalProvisi = res.data?.provisi.toString()
                    val nominalJasa =
                        FormatterAngka.formatterRibuanKeInt(binding.edtJasa.text.toString())
                    val nominalTotal = nominalPinjamanDiajukan + nominalJasa
                    val nominalAngsBln =
                        FormatterAngka.formatterRibuanKeInt(binding.edtAngsuran.text.toString())
                    val nominalDanaCair = res.data?.danaCair.toString()

                    binding.edtSimpananPagu.setText(nominalSimpKhusus)
                    binding.edtAdministrasi.setText(nominalAdministrasi)
                    binding.edtAsuransi.setText(nominalAsuransi)
                    binding.edtProvisi.setText(nominalProvisi)
                    binding.edtJmlJasa.setText(nominalJasa.toString())
                    binding.edtJmlTotal.setText(nominalTotal.toString())
                    binding.edtAngsBln.setText(nominalAngsBln.toString())
                    binding.edtDanaDiterima.setText(nominalDanaCair)
                }
            }
        } else {
            nominalPinjamanViewModel.hitungAdmResponse.observe(this) { res ->
                if (res.code == 200) {
                    binding.cardviewRincian.visibility = View.VISIBLE
                    binding.btnHitungBiaya.visibility = View.GONE
                    binding.btnConfirmPinjaman.visibility = View.VISIBLE
                    val asuransiValue = res.data?.asuransi
                    val nominalPagu = res.data?.simpKhusus.toString()

                    val nominalAdministrasi = res.data?.adm.toString()
//                val nominalAsuransi = res.data?.asuransi.toString()
                    val formattedAsuransi = FormatterAngka.formatterAngkaRibuanDouble(asuransiValue)
                    val angsuran = res.data?.angsuran.toString()
                    val nominalProvisi =
                        FormatterAngka.formatterRibuanKeIntDouble(res.data?.provisi.toString())
                            .toString()
                    val danaDiterima = res.data?.danaCair.toString()
                    val total = res.data?.total.toString()
                    val nominalJasa = res.data?.jasa.toString()

                    binding.edtSimpananPagu.setText(nominalPagu)
                    binding.edtProvisi.setText(nominalProvisi)
                    binding.edtAdministrasi.setText(nominalAdministrasi)
                    binding.edtAngsBln.setText(angsuran)
                    binding.edtAsuransi.setText(formattedAsuransi)
                    binding.edtJmlTotal.setText(total)
                    binding.edtDanaDiterima.setText(danaDiterima)
                    binding.edtJmlJasa.setText(nominalJasa)
                } else {
                    AndroidUIHelper.showWarningToastShort(this, "Error ${res.code}: ${res.message}")
                }
            }
        }
    }

    private fun checkLoading() {
        nominalPinjamanViewModel.isLoading.observe(this) { res ->
            showLoading(res)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarCalculate.visibility = View.VISIBLE
            binding.progressBarAjukanSimpanan.visibility = View.VISIBLE
        } else {
            binding.progressBarCalculate.visibility = View.GONE
            binding.progressBarAjukanSimpanan.visibility = View.GONE
        }
    }

    private fun getSession() {
        nominalPinjamanViewModel.getSession().observe(this) {
            userID = it.username
        }
    }

    private fun getDataFromPreviousActivity() {
        dataPotonganExtra = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<InputtedBiayaPot>(
                EXTRA_DATA,
                InputtedBiayaPot::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA)
        }

        Log.d(TAG, "Data yang didapat dari activity sebelumnya: $dataPotonganExtra")

        binding.tvTipePotongan.text = dataPotonganExtra?.tipePotongan
        binding.tvJmlTenor.text = dataPotonganExtra?.jmlTenor

        when (dataPotonganExtra?.tipePinjaman) {
            "JAPAN" -> {
                binding.tvTipePinjaman.text = "Jangka Panjang"
                binding.imTipePinjaman.setImageResource(R.drawable.tipin_japan)
                binding.fieldTambahan.visibility = View.GONE
            }

            "RUMAH" -> {
                binding.tvTipePinjaman.text = "Rumah"
                binding.imTipePinjaman.setImageResource(R.drawable.ic_rumah)
                binding.fieldTambahan.visibility = View.VISIBLE
            }

            "BRANG" -> {
                binding.tvTipePinjaman.text = "Kredit Barang"
                binding.imTipePinjaman.setImageResource(R.drawable.ic_brang)
                binding.fieldTambahan.visibility = View.GONE
            }

            "MOBIL" -> {
                binding.tvTipePinjaman.text = "Mobil"
                binding.imTipePinjaman.setImageResource(R.drawable.ic_mobil)
                binding.fieldTambahan.visibility = View.GONE
            }

            "MOTOR" -> {
                binding.tvTipePinjaman.text = "Motor"
                binding.imTipePinjaman.setImageResource(R.drawable.ic_motor)
                binding.fieldTambahan.visibility = View.VISIBLE
            }

            "JAPEN" -> {
                binding.tvTipePinjaman.text = "Jangka Pendek"
                binding.imTipePinjaman.setImageResource(R.drawable.ic_japen)
                binding.fieldTambahan.visibility = View.GONE
            }
        }
    }

    private fun hitungBiayaBtnPressed() {
        if (tipePinjaman == "Rumah" || tipePinjaman == "Motor") {
            val tipePinjaman = dataPotonganExtra?.tipePinjaman.toString()
            val tenor = dataPotonganExtra?.jmlTenor.toString()
            val mbrid = userID
            dataPotonganExtra?.nomTipePot.toString()
            val jasa =
                FormatterAngka.formatterRibuanKeInt(binding.edtJasa.text.toString()).toString()
            dataPotonganExtra?.nomPotPri.toString()
            val pinjamanDiajukan =
                FormatterAngka.formatterRibuanKeInt(binding.tvJumlahNominal.text.toString())
                    .toString()
            lifecycleScope.launch {
                nominalPinjamanViewModel.hitungAdmPinjamanLain(
                    am = pinjamanDiajukan,
                    mbrid = mbrid,
                    jasa = jasa,
                    term = tenor,
                    lon = tipePinjaman
                )
            }
        } else {
            val tipePinjaman = dataPotonganExtra?.tipePinjaman.toString()
            val tenor = dataPotonganExtra?.jmlTenor.toString()
            val mbrid = userID
            val nominalTipePotongan = dataPotonganExtra?.nomTipePot.toString()
            val potonganPribadi = dataPotonganExtra?.nomPotPri.toString()
            val pinjamanDiajukan =
                FormatterAngka.formatterRibuanKeInt(binding.tvJumlahNominal.text.toString())

            lifecycleScope.launch {
                nominalPinjamanViewModel.hitungAdmPinjaman(
                    lon = tipePinjaman,
                    term = tenor,
                    am = pinjamanDiajukan,
                    mbrid = mbrid,
                    pot = potonganPribadi,
                    sal = nominalTipePotongan
                )
            }
        }


    }

    private fun chipButtonLogic() {
        binding.chip1jt.setOnClickListener {
            updateNominalValue(binding.chip1jt)
        }
        binding.chip5jt.setOnClickListener {
            updateNominalValue(binding.chip5jt)
        }
        binding.chip10jt.setOnClickListener {
            updateNominalValue(binding.chip10jt)
        }
    }

    private fun updateNominalValue(chip: Chip) {
        val nilaiChip = chip.text.toString().replace("Rp", "").replace(".", "")
        val formattedValue = FormatterAngka.formatterAngkaRibuan(nilaiChip.toInt())
        binding.tvJumlahNominal.text = formattedValue
    }

    private fun btnAturNominalLogic() {
        binding.btnTambahPinjaman.setOnClickListener {
            tambahNilaiPinjaman()
        }

        binding.btnKrgPinjaman.setOnClickListener {
            kurangiNilaiPinjaman()
        }

        if (binding.tvJumlahNominal.text == "0") {
            binding.btnKrgPinjaman.visibility = View.GONE
        }
    }

    private fun kurangiNilaiPinjaman() {
        val tvNumberValue = binding.tvJumlahNominal.text
        val currentNumber = tvNumberValue.toString()
        val newNumber = FormatterAngka.formatterRibuanKeInt(currentNumber) - 10000
        binding.tvJumlahNominal.text = FormatterAngka.formatterAngkaRibuan(newNumber)
        if (binding.tvJumlahNominal.text == "0") {
            binding.btnKrgPinjaman.visibility = View.GONE
        }
    }

    private fun tambahNilaiPinjaman() {
        val tvNumberValue = binding.tvJumlahNominal.text
        val currentNumber = tvNumberValue.toString()
        val newNumber = FormatterAngka.formatterRibuanKeInt(currentNumber) + 10000
        binding.tvJumlahNominal.text = FormatterAngka.formatterAngkaRibuan(newNumber)
        binding.btnKrgPinjaman.visibility = View.VISIBLE
    }


    companion object {
        private val TAG = NominalPinjamanActivity::class.java.simpleName
        const val EXTRA_DATA = "extra_data"
    }

}