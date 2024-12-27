package com.alkindi.gcg_akor.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alkindi.gcg_akor.data.local.db.room.entity.PersonalDataEntity
import com.alkindi.gcg_akor.data.local.model.UserModel
import com.alkindi.gcg_akor.data.remote.response.EditPersonalDataResponse
import com.alkindi.gcg_akor.data.remote.response.PersonalDataResponse
import com.alkindi.gcg_akor.data.remote.response.UserProfileImageResponse
import com.alkindi.gcg_akor.data.remote.retrofit.ApiConfig
import com.alkindi.gcg_akor.data.repository.UserRepository
import com.alkindi.gcg_akor.utils.ApiNetworkingUtils
import kotlinx.coroutines.launch

class PersonalDataViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _personalDataResponse = MutableLiveData<List<PersonalDataResponse>?>()
    val personalDataResponse: LiveData<List<PersonalDataResponse>?> = _personalDataResponse

    private val _userImageResponse = MutableLiveData<UserProfileImageResponse>()
    val userImageResponse: LiveData<UserProfileImageResponse> = _userImageResponse

    private val _editUserPersonalDataResponse = MutableLiveData<EditPersonalDataResponse>()
    val editPersonalDataResponse: LiveData<EditPersonalDataResponse> = _editUserPersonalDataResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession(): LiveData<UserModel> = userRepository.getSession().asLiveData()

    val savedPersonalData: LiveData<List<PersonalDataEntity>> = userRepository.getUserPersonalData()

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


    suspend fun editUserPersonalData(
        mbrid: String,
        name: String?,
        nip: String?,
        birthPlace: String?,
        birthDate: String?,
        address: String
    ) {
        try {
            _isLoading.value = true

            val argl = """{
                "mbrid":"$mbrid",
                "name":"$name",
                "nip":"$nip",
                "birth_place":"$birthPlace",
                "birth_date":"$birthDate",
                "address":"$address"
                }""".trimMargin()
            val apiService = ApiConfig.getApiService()
            val response = apiService.editUserPersonalData(argl = argl)
            _editUserPersonalDataResponse.value = response
        } catch (e: Exception) {
            Log.e(TAG, "Error when executing editUserPersonalData function: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }


    fun getPersonalData(username: String?) {
        if (username == null) Log.e(
            TAG, "Couldn't fetch current user data: " + Log.ERROR.toString()
        ) else {
            try {
                viewModelScope.launch {
                    _isLoading.value = true
                    userRepository.fetchUserPersonalData(username)
                    _isLoading.value = false
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error in Viewmodel class: ${e.message.toString()}")
                _isLoading.value = false
            }
        }
    }


    companion object {
        private val TAG = PersonalDataViewModel::class.java.simpleName
    }
}