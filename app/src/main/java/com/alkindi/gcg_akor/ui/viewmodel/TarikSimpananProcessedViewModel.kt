package com.alkindi.gcg_akor.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alkindi.gcg_akor.data.remote.response.TarikSimpananProcessedResponse
import com.alkindi.gcg_akor.data.remote.retrofit.ApiConfig
import com.alkindi.gcg_akor.data.repository.UserRepository
import com.alkindi.gcg_akor.utils.ApiNetworkingUtils

class TarikSimpananProcessedViewModel(userRepository: UserRepository) : ViewModel() {
    private val _tarikSimpananProcessed = MutableLiveData<TarikSimpananProcessedResponse>()
    val tarikSimpananProcessed: LiveData<TarikSimpananProcessedResponse> = _tarikSimpananProcessed

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    suspend fun getPullTransactionInfo(docnum: String) {
        try {
            _isLoading.value = true
            val apiService = ApiConfig.getApiService()
            val responseCode =
                "NU5mgOhAZUGhJ24WH1zuqwTnRtBFfK6yMSWfNPvmW0jfPrfwhy01oldySn3sU%2BduqyIWqKpK2My9Jqa8UDU9sA%3D%3D"
            val data = mapOf(
                "docnum" to docnum
            )
            val encodedData = ApiNetworkingUtils.jsonFormatter(data)
            val fullUrl =
                "${ApiConfig.BASE_URL_KOPEGMAR}txn?fnc=runLib;opic=${ApiConfig.API_DEV_CODE_KOPEGMAR};csn=${ApiConfig.WORKSPACE_CODE_KOPEGMAR};rc=${responseCode};vars=${encodedData}"

            val response = apiService.getTarikSimpananInfo(fullUrl)
            _tarikSimpananProcessed.value = response
        } catch (e: Exception) {
            Log.e(
                TAG,
                "Unable to execute get getPullTransactionInfo function: ${e.message.toString()}"
            )
        } finally {
            _isLoading.value = false
        }
    }

    companion object {
        private val TAG = TarikSimpananProcessedViewModel::class.java.simpleName
    }
}