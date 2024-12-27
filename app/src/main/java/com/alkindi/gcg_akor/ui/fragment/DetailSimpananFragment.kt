package com.alkindi.gcg_akor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alkindi.gcg_akor.data.model.ViewModelFactory
import com.alkindi.gcg_akor.databinding.FragmentDetailSimpananBinding
import com.alkindi.gcg_akor.ui.adapter.DetailSimpananAdapter
import com.alkindi.gcg_akor.ui.viewmodel.DetailSimpananFragmentViewModel
import com.alkindi.gcg_akor.utils.AndroidUIHelper


class DetailSimpananFragment : Fragment() {
    private lateinit var binding: FragmentDetailSimpananBinding
    private var selectedValue: String? = null

    private val detailSimpananFragmentViewModel: DetailSimpananFragmentViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedValue = it.getString("selected_val")
            AndroidUIHelper.showWarningToastShort(requireContext(), selectedValue.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailSimpananBinding.inflate(inflater, container, false)
        val view = binding.root
        getRVData()
        checkLoading()
        showRVData()

        return view
    }

    private fun getRVData() {
        detailSimpananFragmentViewModel.getSession().observe(viewLifecycleOwner) {
            when (selectedValue) {
                "Simpanan Khusus" -> {
                    detailSimpananFragmentViewModel.getDetailSimpananData(it.username, "SK")
                }
                "Simpanan Sukarela"->{
                    detailSimpananFragmentViewModel.getDetailSimpananData(it.username,"SS")
                }
                "Simpanan Khusus Pagu"->{
                    detailSimpananFragmentViewModel.getDetailSimpananData(it.username,"SKP")
                }
                "Simpanan Pokok"->{
                    detailSimpananFragmentViewModel.getDetailSimpananData(it.username,"SP")
                }
                "Simpanan Wajib"->{
                    detailSimpananFragmentViewModel.getDetailSimpananData(it.username,"SW")
                }
            }
        }
    }

    private fun checkLoading() {
        detailSimpananFragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


    private fun showRVData() {
        binding.rvDetailSimpanan.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = DetailSimpananAdapter()
        detailSimpananFragmentViewModel.detailSimpananResponse.observe(viewLifecycleOwner) { res ->
            adapter.submitList(res.data)
            binding.rvDetailSimpanan.adapter = adapter
        }
    }

}