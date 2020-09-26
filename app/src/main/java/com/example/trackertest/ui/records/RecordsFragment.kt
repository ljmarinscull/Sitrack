package com.example.trackertest.ui.records

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackertest.R
import com.example.trackertest.data.database.Record
import com.example.trackertest.ui.MainViewModel
import com.example.trackertest.utils.hideProgress
import com.example.trackertest.utils.showProgress
import com.example.trackertest.utils.toast
import com.example.trackertest.vo.Resource
import kotlinx.android.synthetic.main.fragment_records.*

class RecordsFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_records, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(ContextCompat.getDrawable(context,R.drawable.divider)!!)
            addItemDecoration(itemDecoration)
        }

        if(mainViewModel.recordList.value!!.isNotEmpty()){
            val adapter = RecordAdapter(requireActivity(), mainViewModel.recordList.value!!)
            tvEmpty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.adapter = adapter
        }

    }
}