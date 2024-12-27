package com.alkindi.gcg_akor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.alkindi.gcg_akor.data.local.model.UserProfile
import com.alkindi.gcg_akor.data.model.ViewModelFactory
import com.alkindi.gcg_akor.data.remote.customizednetworkingclass.ImageLoaderCustom
import com.alkindi.gcg_akor.data.remote.retrofit.ApiConfig
import com.alkindi.gcg_akor.databinding.ActivityProfileBinding
import com.alkindi.gcg_akor.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var userID: String
    private lateinit var extraData: UserProfile
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkLoading()
        getProfileData()
        checkFetchedData()
        binding.btnLogout.setOnClickListener {
            profileViewModel.deleteUserSession(userID)
            logOut()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        getUserProfileImage()
        binding.btnEditProfil.setOnClickListener {
            val toEditProfile =
                Intent(this@ProfileActivity, EditProfileActivity::class.java).putExtra(
                    EditProfileActivity.EXTRA_ID, extraData
                )
            startActivity(toEditProfile)
        }

    }

    private fun getUserProfileImage() {
        lifecycleScope.launch {
            profileViewModel.getUserImage("10006")
        }
    }

    private fun checkLoading() {
        profileViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) = if (isLoading) binding.progressBar.visibility =
        View.VISIBLE else binding.progressBar.visibility = View.GONE


    private fun checkFetchedData() {
        profileViewModel.profileDataResponse.observe(this) { resp ->
            val noHP = resp.data?.telp
            val namaUser = resp.data?.fname
            val emailUser = resp.data?.email

            binding.edtPhone.setText(noHP ?: "Kosong")
            binding.edtUserName.setText(namaUser ?: "Kosong")
            binding.edtEmail.setText(emailUser ?: "Kosong")
            binding.tvIdUser.text = userID
            extraData = UserProfile(
                namaUser,
                emailUser,
                noHP
            )
        }

        profileViewModel.userImageResponse.observe(this) { res ->
//            Glide.with(this).load("${ApiConfig.BASE_URL_KOPEGMAR}${res.data}")
//                .into(binding.imProfile)
            ImageLoaderCustom(binding.imProfile).execute("${ApiConfig.BASE_URL_KOPEGMAR}${res.data}")
        }
    }

    private fun getProfileData() {
        profileViewModel.getSession().observe(this) {
            userID = it.username
            profileViewModel.getUserProfileData(it.username)
        }
    }

    private fun logOut() {
        val logout = Intent(this@ProfileActivity, LoginActivity::class.java)
        logout.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(logout)
        finish()
    }
}