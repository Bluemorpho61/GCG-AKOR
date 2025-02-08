package com.alkindi.gcg_akor.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.alkindi.gcg_akor.data.model.ViewModelFactory
import com.alkindi.gcg_akor.data.remote.customizednetworkingclass.ImageLoaderCustom
import com.alkindi.gcg_akor.data.remote.retrofit.ApiConfig
import com.alkindi.gcg_akor.databinding.ActivityPersonalDataBinding
import com.alkindi.gcg_akor.ui.viewmodel.PersonalDataViewModel
import com.alkindi.gcg_akor.utils.AndroidUIHelper
import kotlinx.coroutines.launch

class PersonalDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonalDataBinding
    private lateinit var idMember: String
    private val personalDataViewModel: PersonalDataViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDataBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        showData()
        getImageData()
        buttonLogic()
        checkLoading()

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnKonfirmEdit.setOnClickListener {
            changeUserPersonalData()
            observeEditResponse()
        }
    }

    private fun showData() {
        getData()
        assignData()
    }

    private fun assignData() {
        val male = "Laki-laki"
        val female = "Perempuan"
        personalDataViewModel.personalDataResponse.observe(this) { res ->
            binding.edtNama.setText(res.data?.name ?: "Kosong")
            binding.edtNIP.setText(res.data?.mbrEmpno ?: "Kosong")
            binding.edtKelamin.setText(
                if (res.data?.gender == "M") male else female
            )
            binding.edtAlamat.setText(res.data?.address ?: "Kosong")
            binding.edtEmail.setText(res.data?.email ?: "Kosong")
            binding.edtNoHP.setText(res.data?.phone ?: "Kosong")
            binding.edtTglLahir.setText(res.data?.birthDate ?: "Kosong")
            binding.edtTempatLahir.setText(res.data?.birthPlace ?: "Kosong")
            binding.edtIdMember.setText(res.data?.mbrid ?: "Kosong")

        }
    }

    private fun getData() {
        personalDataViewModel.getSession().observe(this) {
            lifecycleScope.launch {
                personalDataViewModel.getUserPersonalData(it.username)
            }
        }
    }

    private fun observeEditResponse() {
        try {
            personalDataViewModel.editPersonalDataResponse.observe(this) { res ->
                if (res.code == 200) {
                    AndroidUIHelper.showWarningToastShort(
                        this,
                        "Personal Data user telah berhasil diubah"
                    )
                    binding.layoutConfirmButton.visibility = View.GONE
                    binding.btnEditPersonal.visibility = View.VISIBLE
                    binding.tvEdtFieldWarning.visibility = View.GONE

                    binding.edtNama.isEnabled = false
                    binding.edtNama.isFocusable = false

                    binding.edtNIP.isEnabled = false
                    binding.edtNIP.isFocusable = false

                    binding.edtTempatLahir.isEnabled = false
                    binding.edtTempatLahir.isFocusable = false

                    binding.edtTglLahir.isEnabled = false
                    binding.edtTglLahir.isFocusable = false

                    binding.edtAlamat.isEnabled = false
                    binding.edtAlamat.isFocusable = false

                    binding.edtNoHP.isEnabled = false
                    binding.edtNoHP.isFocusable = false

                    binding.edtEmail.isEnabled = false
                    binding.edtEmail.isFocusable = false
                } else {
                    AndroidUIHelper.showAlertDialog(
                        this,
                        "Error!",
                        "Tidak dapat melakukan edit data user!: ${res.message.toString()}"
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to execute function: ${e.message.toString()}")
            AndroidUIHelper.showAlertDialog(this, "Error!", "${e.message}")
        }

    }

    private fun checkLoading() {
        personalDataViewModel.isLoading.observe(this) { res ->
            showLoading(res)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading)
            binding.progressBar.visibility = View.VISIBLE
        else
            binding.progressBar.visibility = View.GONE
    }

    private fun changeUserPersonalData() {
        val memberId = binding.edtIdMember.text.toString()
        val name = binding.edtNama.text.toString()
        val noNIP = binding.edtNIP.text.toString()
        val tempatLahir = binding.edtTempatLahir.text.toString()
        val tglLahir = binding.edtTglLahir.text.toString()
        val alamat = binding.edtAlamat.text.toString()

        lifecycleScope.launch {
            try {
                personalDataViewModel.editUserPersonalData(
                    memberId,
                    name,
                    noNIP,
                    tempatLahir,
                    tglLahir,
                    alamat
                )

            } catch (e: Exception) {
                Log.e(TAG, "Unable to execute editPersonalData function: $e")
                AndroidUIHelper.showAlertDialog(this@PersonalDataActivity, "Error", "${e.message}")
            }
        }

    }

    private fun getImageData() {
        personalDataViewModel.getSession().observe(this) {
            lifecycleScope.launch {
                personalDataViewModel.getUserImage(it.username)
                showImageData()
            }
        }

    }

    private fun showImageData() {
        personalDataViewModel.userImageResponse.observe(this) { res ->
            ImageLoaderCustom(binding.imProfile).execute("${ApiConfig.BASE_URL_KOPEGMAR}${res.data}")
        }
    }

    private fun buttonLogic() {
        binding.btnEditPersonal.setOnClickListener {
            binding.layoutConfirmButton.visibility = View.VISIBLE
            binding.btnEditPersonal.visibility = View.GONE
            binding.tvEdtFieldWarning.visibility = View.VISIBLE

            binding.edtNama.isEnabled = true
            binding.edtNama.isFocusable = true

            binding.edtNIP.isEnabled = true
            binding.edtNIP.isFocusable = true

            binding.edtTempatLahir.isEnabled = true
            binding.edtTempatLahir.isFocusable = true

            binding.edtTglLahir.isEnabled = true
            binding.edtTglLahir.isFocusable = true

            binding.edtAlamat.isEnabled = true
            binding.edtAlamat.isFocusable = true

            binding.edtNoHP.isEnabled = true
            binding.edtNoHP.isFocusable = true

            binding.edtEmail.isEnabled = true
            binding.edtEmail.isFocusable = true
        }
        binding.btnBatalkanEdit.setOnClickListener {
            binding.layoutConfirmButton.visibility = View.GONE
            binding.btnEditPersonal.visibility = View.VISIBLE
            binding.tvEdtFieldWarning.visibility = View.GONE

            binding.edtNama.isEnabled = false
            binding.edtNama.isFocusable = false

            binding.edtNIP.isEnabled = false
            binding.edtNIP.isFocusable = false

            binding.edtTempatLahir.isEnabled = false
            binding.edtTempatLahir.isFocusable = false

            binding.edtTglLahir.isEnabled = false
            binding.edtTglLahir.isFocusable = false

            binding.edtAlamat.isEnabled = false
            binding.edtAlamat.isFocusable = false

            binding.edtNoHP.isEnabled = false
            binding.edtNoHP.isFocusable = false

            binding.edtEmail.isEnabled = false
            binding.edtEmail.isFocusable = false
        }
    }

//    private fun showData() {
//        val male = "Laki-laki"
//        val female = "Perempuan"
////        personalDataViewModel.savedPersonalData.observe(this) { res ->
////            val personalData = if (!res.isNullOrEmpty()) res[0] else null
////
////            binding.edtNama.setText(personalData?.nama ?: "Kosong")
////            binding.edtNIP.setText(personalData?.mbrEmpno ?: "Kosong")
////            binding.edtIdMember.setText(idMember)
////            binding.edtTempatLahir.setText(personalData?.tempatLahir ?: "Kosong")
////            binding.edtTglLahir.setText(personalData?.tglLahir ?: "Kosong")
////            binding.edtKelamin.setText(
////                if (personalData?.jenisKelamin == "M") male else female
////            )
////            binding.edtAlamat.setText(personalData?.alamat ?: "Kosong")
////            binding.edtNoHP.setText(personalData?.noHp ?: "Kosong")
////            binding.edtEmail.setText(personalData?.email ?: "Kosong")
//
//            // Show AlertDialog if data is null or empty
////            if (personalData == null) {
////                AndroidUIHelper.showAlertDialog(this, "Error", "Gagal Mengambil data dari API")
////            }
////            if (!res.isNullOrEmpty()) {
////                binding.edtNama.setText(res[0].nama ?:"Kosong")
////                binding.edtNIP.setText(res[0].mbrEmpno ?: "Kosong")
////                binding.edtIdMember.setText(idMember)
////                binding.edtTempatLahir.setText(res[0].tempatLahir ?: "Kosong")
////                binding.edtTglLahir.setText(res[0].tglLahir ?: "Kosong")
////                if (res[0].jenisKelamin == "M")
////                    binding.edtKelamin.setText(male)
////                else
////                    binding.edtKelamin.setText(female)
////                binding.edtAlamat.setText(res[0].alamat ?: "Kosong")
////                binding.edtNoHP.setText(res[0].noHp ?: "Kosong")
////                binding.edtEmail.setText(res[0].email ?: "Kosong")
////            } else {
////                AndroidUIHelper.showAlertDialog(this, "Error", "Gagal Mengambil data dari API")
////                binding.edtNama.setText("Kosong")
////                binding.edtNIP.setText("Kosong")
////                binding.edtIdMember.setText(idMember)
////                binding.edtTempatLahir.setText( "Kosong")
////                binding.edtTglLahir.setText("Kosong")
////                if (res[0].jenisKelamin == "M")
////                    binding.edtKelamin.setText(male)
////                else
////                    binding.edtKelamin.setText(female)
////                binding.edtAlamat.setText("Kosong")
////                binding.edtNoHP.setText( "Kosong")
////                binding.edtEmail.setText("Kosong")
////            }
//        }
//    }
//
//
//
//
//    private fun showLoading(isLoading: Boolean) {
////        if (isLoading)
//////            binding.progressBar.visibility = View.VISIBLE else
//////            binding.progressBar.visibility = View.GONE
//        if (isLoading)
//    }
//
//    companion object {
//        private val TAG = PersonalDataActivity::class.java.simpleName
//    }

    companion object {
        private val TAG = PersonalDataActivity::class.java.simpleName
    }
}