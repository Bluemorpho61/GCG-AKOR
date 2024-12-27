package com.alkindi.gcg_akor.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alkindi.gcg_akor.data.local.model.UserModel
import com.alkindi.gcg_akor.data.remote.response.ExtUserProfileResponse
import com.alkindi.gcg_akor.data.remote.response.UserProfileImageResponse
import com.alkindi.gcg_akor.data.remote.retrofit.ApiConfig
import com.alkindi.gcg_akor.data.repository.UserRepository
import com.alkindi.gcg_akor.utils.ApiNetworkingUtils
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _profileDataResponse = MutableLiveData<ExtUserProfileResponse>()
    val profileDataResponse: LiveData<ExtUserProfileResponse> = _profileDataResponse

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


    fun getUserProfileData(username: String) {
        if (username.isEmpty()) {
            Log.e(TAG, "Error: ${Log.ERROR}")
        } else {
            viewModelScope.launch {
                _isLoading.value = true
                try {
                    val apiService = ApiConfig.getApiService()
                    val data = mapOf(
                        "userId" to username.uppercase()
                    )
                    val encodedJson = ApiNetworkingUtils.jsonFormatter(data)
                    val apiCode = "KvRnqbr%2Bktu7HRDvQttp6LEdk0Nmqoyg7GQ98Ln6DH4%3D"
                    val fullUrl =
                        "${ApiConfig.BASE_URL_KOPEGMAR}txn?fnc=runLib;opic=${ApiConfig.API_DEV_CODE_KOPEGMAR};csn=YPNRO;rc=${apiCode};vars=${encodedJson}"
                    val response = apiService.getProfileExt(fullUrl)
                    Log.d(TAG, "Profile Data API Response: $response")
                    Log.d(TAG, "Encoded Json Data: $encodedJson")
                    _profileDataResponse.value = response
                } catch (e: HttpException) {
                    Log.e(TAG, "Error: ${e.message()}")
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }


    fun deleteUserSession(username: String) {
        viewModelScope.launch {
            userRepository.deletePersonalData(username)
            userRepository.logout()
        }
    }


    companion object {
        private val TAG = ProfileViewModel::class.java.simpleName
    }
}