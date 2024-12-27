package com.alkindi.gcg_akor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alkindi.gcg_akor.R
import com.alkindi.gcg_akor.data.local.model.SimpTypeWNominal
import com.alkindi.gcg_akor.data.model.ViewModelFactory
import com.alkindi.gcg_akor.databinding.ActivityTarikSimpananBinding
import com.alkindi.gcg_akor.ui.fragment.DetailSimpananFragment
import com.alkindi.gcg_akor.ui.fragment.RiwayatTransaksiFragment
import com.alkindi.gcg_akor.ui.viewmodel.TarikSimpananViewModel
import com.alkindi.gcg_akor.utils.AndroidUIHelper
import com.alkindi.gcg_akor.utils.FormatterAngka

class TarikSimpananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTarikSimpananBinding
    private val tarikSimpananViewModel: TarikSimpananViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var userID: String
    private lateinit var nominalValue: String
    private var isNominalVisible = true
    private var setShownFragment = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTarikSimpananBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkLoading()
        spinnerLogic()
        getUserID()
        fetchUserNominal()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentDetailSimpanan, RiwayatTransaksiFragment()).commit()

        binding.btnTarikSimpanan.setOnClickListener {
            val extraData = SimpTypeWNominal(
                binding.spinnerTipeSimpanan.selectedItem.toString(),
                binding.tvNominalSimpananSukarela.text.toString()
            )

            val toTarikNominal =
                Intent(this@TarikSimpananActivity, TarikNominalSimpananActivity::class.java)

            toTarikNominal.putExtra(TarikNominalSimpananActivity.EXTRA_SIMP_TYPE, extraData)
            startActivity(toTarikNominal)
        }

        binding.btnDetailSimpanan.setOnClickListener {
            if (setShownFragment) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentDetailSimpanan, DetailSimpananFragment(), "frag_detail")
                    .commit()
                binding.btnDetailSimpanan.text = "Detail Simpanan"
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentDetailSimpanan, RiwayatTransaksiFragment(), "frag_detail")
                    .commit()
                binding.btnDetailSimpanan.text = "Riwayat Transaksi"
            }
            setShownFragment = !setShownFragment
        }

        binding.btnBack.setOnClickListener {
            finish()
        }


        binding.btnNominalVisibility.setOnClickListener {
            toggleNominalVisibility()
        }

    }

    private fun spinnerLogic() {
        tarikSimpananViewModel.dataSimpananUser.observe(this) {
            if (it.code == 500) {
                AndroidUIHelper.showAlertDialog(
                    this,
                    "Error",
                    "Gagal mengambil data dikarenakan tidak dapat terhubung dengan API. Silahkan hubungi administrator"
                )
            } else {
                val spinnerItems = listOf(
                    "Simpanan Sukarela",
                    "Simpanan Khusus",
                    "Simpanan Khusus Pagu",
                    "Simpanan Pokok",
                    "Simpanan Wajib"
                )
                val nominalData = mapOf(
                    "Simpanan Sukarela" to it.data?.get(0)?.sskr.toString(),
                    "Simpanan Khusus" to it.data?.get(0)?.sskp.toString(),
                    "Simpanan Khusus Pagu" to it.data?.get(0)?.sskq.toString(),
                    "Simpanan Pokok" to it.data?.get(0)?.sspo?.toString(),
                    "Simpanan Wajib" to it.data?.get(0)?.sswj?.toString()
                )
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerTipeSimpanan.adapter = adapter

                binding.spinnerTipeSimpanan.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val selectedItem = parent?.getItemAtPosition(position).toString()
                            passDataToFragment(selectedItem)

                            val nominal = nominalData[selectedItem] ?: "0"
                            val formattedNominal =
                                FormatterAngka.formatterAngkaRibuanDouble(nominal.toDouble())
                            binding.tvNominalSimpananSukarela.text = formattedNominal
                            nominalValue = formattedNominal
                            when (selectedItem) {
                                "Simpanan Pokok" -> {
                                    binding.btnTarikSimpanan.visibility = View.GONE
                                }

                                "Simpanan Wajib" -> {
                                    binding.btnTarikSimpanan.visibility = View.GONE
                                }

                                "Simpanan Khusus Pagu" -> {
                                    binding.btnTarikSimpanan.visibility = View.VISIBLE
                                }

                                "Simpanan Khusus" -> {
                                    binding.btnTarikSimpanan.visibility = View.VISIBLE
                                }

                                "Simpanan Sukarela" -> {
                                    binding.btnTarikSimpanan.visibility = View.VISIBLE
                                }
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }
                    }
            }
        }

    }

    private fun passDataToFragment(selectedValue: String) {
        val fragment = DetailSimpananFragment()

        val bundle = Bundle()
        bundle.putString("selected_val", selectedValue)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentDetailSimpanan, fragment)
            .commit()
    }

    private fun fetchUserNominal() {
        tarikSimpananViewModel.dataSimpananUser.observe(this) {
            if (it.code == 500) {
                AndroidUIHelper.showAlertDialog(
                    this,
                    "Error",
                    "Gagal mengambil data dikarenakan tidak dapat terhubung dengan API. Silahkan hubungi administrator"
                )
            } else {
                spinnerLogic()  // Call the spinner logic here after data is fetched
            }
        }
    }

    private fun checkLoading() {
        tarikSimpananViewModel.isLoading.observe(this) {
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

    private fun getUserID() {
        tarikSimpananViewModel.getSession().observe(this) {
            userID = it.username
            tarikSimpananViewModel.getDataSimpananUser(userID)
        }
    }


    private fun toggleNominalVisibility() {
        if (isNominalVisible) {
            binding.tvNominalSimpananSukarela.text = "............."
            binding.btnNominalVisibility.setImageResource(R.drawable.baseline_visibility_24)
        } else {
            binding.tvNominalSimpananSukarela.text = nominalValue
            binding.btnNominalVisibility.setImageResource(R.drawable.baseline_visibility_off_24)
        }
        isNominalVisible = !isNominalVisible
    }


    companion object {
        private val TAG = TarikSimpananActivity::class.java.simpleName
    }

}