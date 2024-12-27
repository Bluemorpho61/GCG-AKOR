package com.alkindi.gcg_akor.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alkindi.gcg_akor.data.local.model.UserModel
import com.alkindi.gcg_akor.data.remote.response.HistoryPinjamanResponse
import com.alkindi.gcg_akor.data.remote.retrofit.ApiConfig
import com.alkindi.gcg_akor.data.repository.UserRepository
import com.alkindi.gcg_akor.utils.ApiNetworkingUtils
import kotlinx.coroutines.launch

class HistoryPinjamanViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _historyPinjamanResponse = MutableLiveData<HistoryPinjamanResponse>()
    val historyPinjamanResponse: LiveData<HistoryPinjamanResponse> = _historyPinjamanResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    suspend fun getHistoryData(userId: String) {

        try {
            _isLoading.value = true
            val apiService = ApiConfig.getApiService()
            val data = mapOf(
                "user_id" to userId
            )
            val apiCode = "UQihbFj9rzSMdr02uS94Z1oDhH69zUlWBxxyo8ewHNB3y5jCa3nBeg%3D%3D"
            val encodedData = ApiNetworkingUtils.jsonFormatter(data)
            val fullUrl =
                "${ApiConfig.BASE_URL_KOPEGMAR}txn?fnc=runLib;opic=${ApiConfig.API_DEV_CODE_KOPEGMAR};csn=${ApiConfig.WORKSPACE_CODE_KOPEGMAR};rc=${apiCode};vars=${encodedData}"
            val response = apiService.getHistoryPinjaman(fullUrl)
            _historyPinjamanResponse.value = response
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        } finally {
            _isLoading.value = false
        }

    }

    fun getSession(): LiveData<UserModel> = userRepository.getSession().asLiveData()

    companion object {
        private val TAG = HistoryPinjamanViewModel::class.java.simpleName
    }
}