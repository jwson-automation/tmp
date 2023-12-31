package com.project.tmp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResultAdapter(val resultList: List<ResultDto>) :
    RecyclerView.Adapter<ResultAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nickname: TextView = itemView.findViewById(R.id.nickname)
        val desc: TextView = itemView.findViewById(R.id.description)
        val distance : TextView = itemView.findViewById(R.id.distance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.result_view, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = resultList[position]
        holder.nickname.text = result.nickname
        holder.desc.text = result.description
        holder.distance.text = "Distance : ${result.distance.toString()}km "
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

}