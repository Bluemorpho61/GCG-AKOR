package com.alkindi.gcg_akor.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alkindi.gcg_akor.data.model.HistoryPinjamanModel
import com.alkindi.gcg_akor.data.remote.response.HistoryPinjamanItem
import com.alkindi.gcg_akor.data.remote.response.HistoryPinjamanResponse
import com.alkindi.gcg_akor.databinding.RvHistorypinjamanCardBinding
import com.alkindi.gcg_akor.ui.activity.DetailHistoryPinjaman
import com.alkindi.gcg_akor.utils.FormatterAngka

class HistoryPinjamanAdapter :
    ListAdapter<HistoryPinjamanItem, HistoryPinjamanAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: RvHistorypinjamanCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryPinjamanItem) {
            binding.tvUniqueId.text = item.docNum
            binding.tvNominalGaji.text = item.amount.toString()
//            binding.tvNominalPinjamanBulanan.text = item.pinjamanBulanan
            val fetchedNominalPinjamanBulanan =item.remain.toString()
            binding.tvNominalPinjamanBulanan.text = fetchedNominalPinjamanBulanan
            binding.tvPinjamanBulanan.text =item.pjmCode
            binding.tvSisaTenor.text = item.sisaTenor.toString()
            with(itemView) {
                setOnClickListener {
                    Intent(context, DetailHistoryPinjaman::class.java).apply {
                        context.startActivity(this)
                    }
                }
            }
        }
    }


    companion object {
        @SuppressLint("DiffUtilEquals")
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryPinjamanItem>() {
            override fun areContentsTheSame(
                oldItem: HistoryPinjamanItem,
                newItem: HistoryPinjamanItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: HistoryPinjamanItem,
                newItem: HistoryPinjamanItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            RvHistorypinjamanCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val historyData = getItem(position)
        holder.bind(historyData)
    }


}