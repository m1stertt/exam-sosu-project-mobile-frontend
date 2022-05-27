package com.example.exam_sosu_project_mobile_frontend.ui.citizens

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exam_sosu_project_mobile_frontend.MapsActivity
import com.example.exam_sosu_project_mobile_frontend.databinding.CitizenItemBinding
import com.example.exam_sosu_project_mobile_frontend.entities.Citizen
import com.example.exam_sosu_project_mobile_frontend.entities.DawaAddress
import com.example.exam_sosu_project_mobile_frontend.interfaces.DawaApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        holder.mapBtn.setOnClickListener {
            //@todo
            val dawaApiInterface = DawaApiInterface.create(it.context)
            dawaApiInterface.get(item.address.street,item.address.postCode,"mini").enqueue(object :
                Callback<List<DawaAddress>> {
                override fun onResponse(call: Call<List<DawaAddress>>?, response: Response<List<DawaAddress>>?) {
                    if (response?.body() != null) {
                        val x=response.body()!![0].x
                        val y=response.body()!![0].y
                        val intent = Intent(it.context, MapsActivity::class.java)
                        intent.putExtra("longitude",x)
                        intent.putExtra("latitude",y)
                        intent.putExtra("id",item.id)
                        intent.putExtra("address",item.address.street)
                        it.context.startActivity(intent)
                    }
                }
                override fun onFailure(call: Call<List<DawaAddress>>?, t: Throwable?) {
                    //@todo
                }
            })
        }
        holder.itemView.setOnClickListener {view->
            val intent = Intent(view.context, CitizenViewActivity::class.java)
            intent.putExtra("id",item.id)
            view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: CitizenItemBinding) : RecyclerView.ViewHolder(binding.root) {
        //val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
        val mapBtn: ImageButton = binding.locationBtn
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