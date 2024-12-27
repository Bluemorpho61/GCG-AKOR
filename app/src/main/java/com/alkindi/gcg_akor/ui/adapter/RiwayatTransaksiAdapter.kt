package com.alkindi.gcg_akor.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alkindi.gcg_akor.R
import com.alkindi.gcg_akor.data.remote.response.RiwayatTarikSimpananItem
import com.alkindi.gcg_akor.databinding.RvRiwayattransaksiCardBinding
import com.alkindi.gcg_akor.utils.FormatterAngka
import com.bumptech.glide.Glide

class RiwayatTransaksiAdapter :
    ListAdapter<RiwayatTarikSimpananItem, RiwayatTransaksiAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: RvRiwayattransaksiCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RiwayatTarikSimpananItem) {
            val tipeSimpanan = item.stp.toString()
            val transDate = item.transDate.toString()
            val idTransaksi = item.docNum.toString()
            val statusTrans = item.aprinfo.toString().first()

            when (tipeSimpanan) {
                "SS" -> {
                    binding.tvSukarela.text = "Simpanan Sukarela"
                }

                "SK" -> {
                    binding.tvSukarela.text = "Simpanan Khusus"
                }

                "SKP" -> {
                    binding.tvSukarela.text = "Simpanan Khusus Pagu"
                }

                "SW" -> {
                    binding.tvSukarela.text = "Simpanan Wajib"
                }

                "SP" -> {
                    binding.tvSukarela.text = "Simpanan Pokok"
                }

                else -> "Unknown"
            }
            binding.idTransaksi.text = idTransaksi
            binding.tglTransaksi.text = transDate
            when (statusTrans) {
                'A' -> Glide.with(itemView.context).load(R.drawable.ic_approve_txt)
                    .into(binding.icStatusTransaksi)

                'R' -> Glide.with(itemView.context).load(R.drawable.ic_rejected_txt)
                    .into(binding.icStatusTransaksi)

                else -> Glide.with(itemView.context).load(R.drawable.ic_pending_txt)
                    .into(binding.icStatusTransaksi)
            }
            val formattedNominal = FormatterAngka.formatterAngkaRibuanDouble(item.amount)
            binding.tvNominalRiwayat.text = formattedNominal

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RvRiwayattransaksiCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataRiwayat = getItem(position)
        holder.bind(dataRiwayat)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RiwayatTarikSimpananItem>() {
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: RiwayatTarikSimpananItem,
                newItem: RiwayatTarikSimpananItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: RiwayatTarikSimpananItem,
                newItem: RiwayatTarikSimpananItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}