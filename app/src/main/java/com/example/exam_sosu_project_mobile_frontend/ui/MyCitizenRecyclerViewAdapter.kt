package com.example.exam_sosu_project_mobile_frontend.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exam_sosu_project_mobile_frontend.databinding.CitizenItemBinding
import com.example.exam_sosu_project_mobile_frontend.entities.Citizen


class MyCitizenRecyclerViewAdapter(
    private var values: ArrayList<Citizen>
) : RecyclerView.Adapter<MyCitizenRecyclerViewAdapter.ViewHolder>() {

    private var items:ArrayList<Citizen> = values

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            CitizenItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        //holder.idView.text = item.id
        holder.contentView.text = item.firstName+" "+item.lastName
        holder.contentView.setOnClickListener {view->
            val intent = Intent(view.context, CitizenViewActivity::class.java)
            intent.putExtra("id",item.id)
            view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: CitizenItemBinding) : RecyclerView.ViewHolder(binding.root) {
        //val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    fun updateData(viewModels: Collection<Citizen>) {
        items.clear()
        items.addAll(viewModels)
        notifyDataSetChanged()
    }

}