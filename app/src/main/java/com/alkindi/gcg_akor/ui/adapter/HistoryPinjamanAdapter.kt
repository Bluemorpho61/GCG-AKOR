package com.alkindi.gcg_akor.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alkindi.gcg_akor.data.remote.response.HistoryPinjamanItem
import com.alkindi.gcg_akor.databinding.RvHistorypinjamanCardBinding
import com.alkindi.gcg_akor.ui.activity.DetailHistoryPinjamanActivity
import com.alkindi.gcg_akor.utils.FormatterAngka

class HistoryPinjamanAdapter :
    ListAdapter<HistoryPinjamanItem, HistoryPinjamanAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: RvHistorypinjamanCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryPinjamanItem) {
            binding.tvUniqueId.text = item.docNum
            val statusPinjaman = item.aprinfo.toString().first()
            val fetchedNominalGaji = item.amount.toString()
            val fetchedNominalPinjamanBulanan = item.remain.toString()
            val formattedNominalGaji =
                FormatterAngka.formatterAngkaRibuanDouble(fetchedNominalGaji.toDouble())
            val formattedNominalPinjamanBulanan =
                FormatterAngka.formatterAngkaRibuanDouble(fetchedNominalPinjamanBulanan.toDouble())

            when (statusPinjaman) {
                'A' -> binding.tvStatus.text = "Diterima"
                'R' -> binding.tvStatus.text = "Ditolak"
                else -> binding.tvStatus.text = "Diproses"
            }
            val formattedTenor = FormatterAngka.penghilangNilaiKoma(item.term.toString())
            binding.tvNominalPinjamanBulanan.text = formattedNominalPinjamanBulanan
            binding.tvPinjamanBulanan.text = item.pjmCode
            binding.tvNominalGaji.text = formattedNominalGaji
            binding.tvSisaTenor.text = formattedTenor
            with(itemView) {
                setOnClickListener {
                    Intent(context, DetailHistoryPinjamanActivity::class.java).apply {
                        putExtra(DetailHistoryPinjamanActivity.EXTRA_DOCNUM, item.docNum)
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