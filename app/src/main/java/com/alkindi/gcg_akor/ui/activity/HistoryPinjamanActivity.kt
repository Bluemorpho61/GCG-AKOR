package com.alkindi.gcg_akor.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alkindi.gcg_akor.data.model.ViewModelFactory
import com.alkindi.gcg_akor.databinding.ActivityHistoryPinjamanBinding
import com.alkindi.gcg_akor.ui.adapter.HistoryPinjamanAdapter
import com.alkindi.gcg_akor.ui.viewmodel.HistoryPinjamanViewModel
import kotlinx.coroutines.launch

class HistoryPinjamanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryPinjamanBinding
    private lateinit var userID: String
    private val historyPinjamanViewModel: HistoryPinjamanViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryPinjamanBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        getUserID()
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        showHistoryPinjamanData()
        checkLoading()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getHistoryData() {
        lifecycleScope.launch {
            historyPinjamanViewModel.getHistoryData(userID)
        }
    }

    private fun getUserID() {
        historyPinjamanViewModel.getSession().observe(this) {
            userID = it.username
            getHistoryData()
        }
    }

    private fun checkLoading() {
        historyPinjamanViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading)
            binding.progressBar.visibility = View.VISIBLE
        else
            binding.progressBar.visibility = View.GONE
    }


    private fun showHistoryPinjamanData() {
        historyPinjamanViewModel.historyPinjamanResponse.observe(this) {
            binding.rvHistoryPinjaman.layoutManager = LinearLayoutManager(this)
            val adapter = HistoryPinjamanAdapter()
            adapter.submitList(it.data)
            binding.rvHistoryPinjaman.adapter = adapter
        }

    }

}