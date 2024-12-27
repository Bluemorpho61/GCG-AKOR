package com.alkindi.gcg_akor.ui.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.alkindi.gcg_akor.data.local.model.UserProfile
import com.alkindi.gcg_akor.data.model.ViewModelFactory
import com.alkindi.gcg_akor.data.remote.customizednetworkingclass.ImageLoaderCustom
import com.alkindi.gcg_akor.data.remote.retrofit.ApiConfig
import com.alkindi.gcg_akor.databinding.ActivityEditProfileBinding
import com.alkindi.gcg_akor.ui.viewmodel.EditProfileViewModel
import com.alkindi.gcg_akor.utils.AndroidUIHelper
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var userID: String
    private val editProfileViewModel: EditProfileViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getUserID()
        checkLoadingStatus()
        getDataFromPreviousActivity()
        changePasswordBtnLogic()
        checkResponseStatus()
        getImage()
        showUserImage()

        binding.btnConfirmEdit.setOnClickListener {
            sendDataChanges()
        }
        binding.btnGantiGambar.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showUserImage() {
        editProfileViewModel.userImageResponse.observe(this) {res->
            ImageLoaderCustom(binding.imProfile).execute("${ApiConfig.BASE_URL_KOPEGMAR}${res.data}")
        }

    }

    private fun getImage() {
        editProfileViewModel.getSession().observe(this) {
            lifecycleScope.launch {
                editProfileViewModel.getUserImage(it.username)
            }
        }
    }

    private fun getUserID() {
        editProfileViewModel.getSession().observe(this) {
            userID = it.username
        }
    }

    private fun sendDataChanges() {
        val namaUser = binding.edtNamaUser.text.toString()
        val noHPUser = binding.edtNoHP.text.toString()
        val emailUser = binding.edtEmail.text.toString()
        val pwLama = binding.edtPwOld.text.toString()
        val pwBaru = binding.edtPwNew.text.toString()
        val pwBaruRetype = binding.edtPwNewRetype.text.toString()
        editProfileViewModel.updateUserProfile(
            userID,
            namaUser,
            noHPUser,
            emailUser,
            pwLama,
            pwBaru,
            pwBaruRetype
        )
    }

    private fun checkLoadingStatus() {
        editProfileViewModel.isLoading.observe(this) { res ->
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

    private fun checkResponseStatus() {
        editProfileViewModel.updateProfileResponse.observe(this) { res ->
            if (res.code == 200) {
                AndroidUIHelper.showWarningToastShort(this, "Data Profil User Berhasil Diubah!")
            } else {
                binding.tvWarning.text = res.message.toString()
                binding.tvWarning.visibility = View.VISIBLE
            }
        }
    }

    private fun changePasswordBtnLogic() {
        binding.btnChangePW.setOnClickListener {
            binding.btnChangePW.visibility = View.GONE
            binding.edtPwOld.visibility = View.VISIBLE
            binding.edtPwNew.visibility = View.VISIBLE
            binding.edtPwNewRetype.visibility = View.VISIBLE
        }
    }

    private fun getDataFromPreviousActivity() {
        val userProfileData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(
                EXTRA_ID,
                UserProfile::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_ID)
        }

        if (userProfileData == null) {
            AndroidUIHelper.showAlertDialog(
                this,
                "Error",
                "Tidak dapat mengambil data dari laman sebelumnya",
            )
            Log.e(TAG, "Tidak dapat mengambil info dari activity sebelumnya!: ${Log.ERROR}")
            finish()
        }
        val namaUser = userProfileData!!.namaUser
        val noHP = userProfileData.noHPUser
        val emailUser = userProfileData.emailUser

        binding.edtNamaUser.setText(namaUser)
        binding.edtNoHP.setText(noHP)
        binding.edtEmail.setText(emailUser)

        if (namaUser != null && noHP != null && emailUser != null) {
            binding.edtNamaUser.setSelection(namaUser.length)
            binding.edtNoHP.setSelection(noHP.length)
            binding.edtEmail.setSelection(emailUser.length)
        }
    }

    companion object {
        private val TAG = EditProfileActivity::class.java.simpleName
        const val EXTRA_ID = "extra_id"
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it

        try {
            binding.imProfile.setImageURI(galleryUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}