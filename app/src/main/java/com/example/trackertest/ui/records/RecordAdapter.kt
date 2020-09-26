package com.example.trackertest.ui.records

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackertest.R
import com.example.trackertest.data.database.Record
import kotlinx.android.synthetic.main.record_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class RecordAdapter(val context: Context, private var recordList: List<Record>) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return MainAdapterViewHolder(
            LayoutInflater.from(context).inflate(R.layout.record_row, parent, false)
        )
    }
    override fun getItemCount(): Int {
        return recordList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MainAdapterViewHolder -> holder.bind(recordList[position], position)
            else -> throw IllegalArgumentException()
        }
    }
    inner class MainAdapterViewHolder(itemView: View) : BaseViewHolder<Record>(itemView) {
        override fun bind(item: Record, position: Int) {
            try {
                var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                val date = format.parse(item.createdAt)
                format = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH)
                itemView.tvDate.text = format.format(date)

                itemView.tvLat.text = item.latitude.toString()
                itemView.tvLong.text = item.longitude.toString()

                itemView.tvSpeed.text = item.speed.toString()
                itemView.tvAcc.text = item.accuracy.toString()

            } catch (e: Exception) {
                Log.e("SimpleDateFormat: error",e.toString())
            }
        }
    }
}