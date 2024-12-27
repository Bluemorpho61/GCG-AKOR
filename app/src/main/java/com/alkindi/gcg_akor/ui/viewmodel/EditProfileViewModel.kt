package com.alkindi.gcg_akor.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alkindi.gcg_akor.data.local.model.UserModel
import com.alkindi.gcg_akor.data.remote.response.UpdateProfileResponse
import com.alkindi.gcg_akor.data.remote.response.UserProfileImageResponse
import com.alkindi.gcg_akor.data.remote.retrofit.ApiConfig
import com.alkindi.gcg_akor.data.repository.UserRepository
import com.alkindi.gcg_akor.utils.ApiNetworkingUtils
import com.google.gson.Gson
import kotlinx.coroutines.launch

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _updateProfileResponse = MutableLiveData<UpdateProfileResponse>()
    val updateProfileResponse: LiveData<UpdateProfileResponse> = _updateProfileResponse

    private val _userImageResponse = MutableLiveData<UserProfileImageResponse>()
    val userImageResponse: LiveData<UserProfileImageResponse> = _userImageResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession(): LiveData<UserModel> = userRepository.getSession().asLiveData()

    suspend fun getUserImage(mbrid: String) {
        if (mbrid.isEmpty()) {
            Log.e(TAG, "Unable to use function getUserImage: ${Log.ERROR}")
        } else {
            try {
                _isLoading.value = true
                val apiService = ApiConfig.getApiService()
                val apiCode = "KvRnqbr%2Bktu7HRDvQttp6EuNm8yG06I%2BsB2%2BPg9itk8%3D"
                val data = mapOf(
                    "mbrid" to mbrid
                )
                val encodedData = ApiNetworkingUtils.jsonFormatter(data)
                val fullUrl =
                    "${ApiConfig.BASE_URL_KOPEGMAR}txn?fnc=runLib;opic=${ApiConfig.API_DEV_CODE_KOPEGMAR};csn=${ApiConfig.WORKSPACE_CODE_KOPEGMAR};rc=${apiCode};vars=${encodedData}"
                val response = apiService.getImageGambar(fullUrl)
                _userImageResponse.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Unable to execute request image process: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun updateUserProfile(
        userId: String,
        namaUser: String?,
        noHP: String?,
        emailUser: String?,
        pwLama: String?,
        pwBaru: String?,
        pwKetikUlang: String?
    ) {
        try {
            val jsonMap = mutableMapOf<String, String>()
            jsonMap["userID"] = userId
            if (!namaUser.isNullOrEmpty()) jsonMap["userName"] = namaUser
            if (!noHP.isNullOrEmpty()) jsonMap["phoneNumber"] = noHP
            if (!emailUser.isNullOrEmpty()) jsonMap["emailAddress"] = emailUser
            if (!pwLama.isNullOrEmpty()) jsonMap["cp"] = pwLama
            if (!pwBaru.isNullOrEmpty()) jsonMap["np"] = pwBaru
            if (!pwKetikUlang.isNullOrEmpty()) jsonMap["rp"] = pwKetikUlang

            // Convert the map to a JSON strin
            val argl = Gson().toJson(jsonMap)
            Log.d(TAG, "Nilai yang ingin diubah: $argl")
            _isLoading.value = true
            viewModelScope.launch {
                val apiService = ApiConfig.getApiService()
                val response = apiService.updateProfileData(argl = argl)
                _updateProfileResponse.value = response
            }
//            val argl = """{
//                    "userID":"$userId",
//                    "userName":"${namaUser ?:""}",
//                    "phoneNumber":"${noHP ?:""}",
//                    "emailAddress":"${emailUser?:""}",
//                    "cp":"${pwLama?:""}",
//                    "np":"${pwBaru?:""}",
//                    "rp":"${pwKetikUlang?:""}"
//                    }""".trimMargin()
//            Log.d(TAG, "Nilai yang ingin diubah: $argl")
//            _isLoading.value = true
//            viewModelScope.launch {
//                val apiService = ApiConfig.getApiService()
//                val response = apiService.updateProfileData(argl = argl)
//                _updateProfileResponse.value = response
//            }
        } catch (e: Exception) {
            Log.e(TAG, "Update user profile data failed: ${Log.ERROR}")
        } finally {
            _isLoading.value = false
        }
    }

    companion object {
        private val TAG = EditProfileViewModel::class.java.simpleName
    }
}