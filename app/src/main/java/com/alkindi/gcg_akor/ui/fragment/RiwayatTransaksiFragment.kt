package com.alkindi.gcg_akor.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alkindi.gcg_akor.data.model.ViewModelFactory
import com.alkindi.gcg_akor.databinding.FragmentRiwayatTransaksiBinding
import com.alkindi.gcg_akor.ui.adapter.RiwayatTransaksiAdapter
import com.alkindi.gcg_akor.ui.viewmodel.RiwayatTransaksiFragmentViewModel
import kotlinx.coroutines.launch


class RiwayatTransaksiFragment : Fragment() {
    private lateinit var binding: FragmentRiwayatTransaksiBinding
    private var userID: String? = null
    private val riwayatTransaksiFragmentViewModel: RiwayatTransaksiFragmentViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRiwayatTransaksiBinding.inflate(inflater, container, false)
        val view = binding.root
        getSession()
        return view
    }

    private fun getSession() {
        if (userID == null) {
            riwayatTransaksiFragmentViewModel.getSession().observe(viewLifecycleOwner) {
                userID = it.username
                showListRiwayat()
            }
        } else {
            showListRiwayat()
        }
    }

    private fun showListRiwayat() {
        lifecycleScope.launch {
            riwayatTransaksiFragmentViewModel.getHistoryTarikSimp(userID!!)
        }
        riwayatTransaksiFragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        riwayatTransaksiFragmentViewModel.historyTarikSimpResponse.observe(viewLifecycleOwner) { resp ->
            Log.d(TAG, "Fetched data: ${resp.data}")
            if (resp.data.isNullOrEmpty()) {
                Log.d(TAG, "No data available")
                binding.rvRiwayatTransaksi.visibility = View.GONE
            } else {
                binding.rvRiwayatTransaksi.visibility =
                    View.VISIBLE
                val adapter = RiwayatTransaksiAdapter()
                adapter.submitList(resp.data)

                binding.rvRiwayatTransaksi.layoutManager = LinearLayoutManager(requireActivity())
                binding.rvRiwayatTransaksi.adapter = adapter
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private val TAG = RiwayatTransaksiFragment::class.java.simpleName
    }
}