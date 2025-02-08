package com.alkindi.gcg_akor.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.alkindi.gcg_akor.data.Result
import com.alkindi.gcg_akor.data.local.db.room.UserPersonalDAO
import com.alkindi.gcg_akor.data.local.db.room.entity.PersonalDataEntity
import com.alkindi.gcg_akor.data.local.model.UserModel
import com.alkindi.gcg_akor.data.local.pref.UserPreference
import com.alkindi.gcg_akor.data.remote.response.HistoryTarikSimpResponse
import com.alkindi.gcg_akor.data.remote.retrofit.ApiConfig
import com.alkindi.gcg_akor.data.remote.retrofit.ApiService
import com.alkindi.gcg_akor.utils.ApiNetworkingUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val userPersonalDAO: UserPersonalDAO
) {

    private val result = MediatorLiveData<Result<List<PersonalDataEntity>>>()
//    private val _historyTarikSimpResponse = MediatorLiveData<List<HistoryTarikSimpResponse>>()
//    val historyTarikSimpResponse: LiveData<List<HistoryTarikSimpResponse>> =
//        _historyTarikSimpResponse

    fun getUserPersonalData(): LiveData<List<PersonalDataEntity>> =
        userPersonalDAO.getUserPersonalData()

//    suspend fun fetchTarikSimpananHistory(username: String) {
//        val data = mapOf(
//            "username" to username.uppercase()
//        )
//        val encodedData = ApiNetworkingUtils.jsonFormatter(data)
//        val apiCode = "NU5mgOhAZUGhJ24WH1zuqwTnRtBFfK6yMhf%2B8XnZd799HXV35pDBeAmaQOPnZORD"
//        val fullUrl =
//            "${ApiConfig.BASE_URL_KOPEGMAR}txn?fmc=runLib;opic=${ApiConfig.API_DEV_CODE_KOPEGMAR};csn=${ApiConfig.WORKSPACE_CODE_KOPEGMAR};rc=${apiCode};vars=${encodedData}"
//
//        try {
//            val response = apiService.getHistorySimpanan(fullUrl)
//            _historyTarikSimpResponse.value =response
//
//        } catch (e: Exception) {
//            Log.e(TAG, "ERROR FETCHING THE DATA ${e.message.toString()}")
//            throw e
//        }
//    }


//    suspend fun fetchUserPersonalData(username: String) {
//        result.value = Result.Loading
//        val data = mapOf(
//            "username" to username.uppercase()
//        )
//        val encodedData = ApiNetworkingUtils.jsonFormatter(data)
//        val apiCode = "gS%2BZtyMBHTdgEoheRgK6hoGn9gB9jdSeepx4X6/t2uDtvQTu57s32w%3D%3D"
//        val fullUrl =
//            "${ApiConfig.BASE_URL_KOPEGMAR}txn?fnc=runLib;opic=${ApiConfig.API_DEV_CODE_KOPEGMAR};csn=${ApiConfig.WORKSPACE_CODE_KOPEGMAR};rc=${apiCode};vars=${encodedData}"
//
//        try {
//            val response = apiService.getPersonal(fullUrl)
//            withContext(Dispatchers.IO) {
//                val personalEntityData = response.map { res ->
//                    PersonalDataEntity(
//                        mbrid = username,
//                        nama = res.name ?: "Kosong",
//                        mbrEmpno = res.noNIP ?: "Kosong",
//                        mbrEmpno2 = res.noNIP2 ?: "Kosong",
//                        companyID = res.companyID ?: "Kosong",
//                        companyBegin = res.companyBegin ?: "Kosong",
//                        companyLast = res.companyLast ?: "Kosong",
//                        mbrgroup = res.mbrGroup ?: "Kosong",
//                        religion = res.religion ?: "Kosong",
//                        education = res.education ?: "Kosong",
//                        tempatLahir = res.tempatLahir ?: "Kosong",
//                        noHp = res.phone ?: "Kosong",
//                        tglLahir = res.tglLahir ?: "Kosong",
//                        alamat = res.address ?: "Kosong",
//                        email = res.email ?: "Kosong",
//                        maritalStatus = res.maritalStatus ?: "Kosong",
//                        photoFile = res.photoFile ?: "Kosong",
//                        jenisKelamin = res.gender ?: "Kosong",
//                        termination = res.termination ?: "Kosong",
//                        termEff = res.termEff ?: "Kosong",
//                        termType = res.termType ?: "Kosong",
//                        termReason = res.termReason ?: "Kosong",
//                        description = res.description ?: "Kosong",
//                        createdInfo = res.createdInfo ?: "Kosong",
//                        modifiedInfo = res.modifiedInfo ?: "Kosong",
//                        mbrPosition = res.mbrPosition ?: "Kosong",
//                        mbrUnit = res.mbrUnit ?: "Kosong",
//                        namaBank = res.bank ?: "Kosong",
//                        noRek = res.noRek ?: "Kosong",
//                        fileKtp = res.fileKTP ?: "Kosong",
//                        fileSlip = res.fileSlip ?: "Kosong",
//                        fileSk = res.fileSK ?: "Kosong",
//                        ktp = res.ktp ?: "Kosong",
//                        namaRek = res.namaRek ?: "Kosong"
//                    )
//                }
//                val isDataExist = userPersonalDAO.getDataByMBRID(username)
//                if (isDataExist == null) {
//                    userPersonalDAO.insertData(personalEntityData)
//                }
//            }
//
//
//        } catch (e: Exception) {
//            Log.e(TAG, "ERROR FETCHING AND SAVE USER PERSONAL DATA!: ${e.message.toString()}")
//            throw e
//        }
//
//    }

    suspend fun deletePersonalData(username: String) {
        try {
            withContext(Dispatchers.IO) {
                userPersonalDAO.deleteData(username)
            }
        } catch (e: Exception) {
            Log.e(TAG, "ERROR DELETING SAVED USER PERSONAL DATA ON ROOM ${e.message.toString()}")
        }
    }


//    init {
//        val db = AkorDB.getDB(application)
//        userPersonalDAO = db.userPersonalDAO()
//    }
//
//    fun insert(user: UserPersonalData) {
//        executorService.execute { userPersonalDAO.insertData(user) }
//    }
//
//    fun update(user: UserPersonalData) {
//        executorService.execute { userPersonalDAO.updateData(user) }
//    }
//
//    fun delete(user: UserPersonalData) {
//        executorService.execute { userPersonalDAO.deleteData(user) }
//    }
//
//    fun getUserData(): LiveData<UserPersonalData> = userPersonalDAO.getUserPersonalData()


    suspend fun saveSession(user: UserModel) = userPreference.saveSession(user)
    fun getSession(): Flow<UserModel> = userPreference.getSession()
    suspend fun logout() = userPreference.logout()

    fun checkSavedPreference() {
        userPreference.logSessionData()
    }

    companion object {
        private val TAG = UserRepository::class.java.simpleName
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            userPersonalDAO: UserPersonalDAO
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(
                    apiService,
                    userPreference,
                    userPersonalDAO
                )
            }.also { instance = it }

    }


}