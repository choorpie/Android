package com.example.recycleviewballlist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.recycleviewballlist.databinding.MyLayoutBinding

class BallAdapter(val ballList: List<Balls>): Adapter<BallAdapter.ViewHolder>(), Filterable {

    private var filterList = ArrayList<Balls>()

    init {
        //At begin, set the filterList to be the same as the ballList
        filterList = ballList as ArrayList<Balls>
    }

    inner class ViewHolder(val binding: MyLayoutBinding): RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.my_layout, parent, false)
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MyLayoutBinding.inflate(layoutInflater, parent, false)
        val viewHolder = ViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            Toast.makeText(parent.context, filterList[viewHolder.bindingAdapterPosition].name, Toast.LENGTH_SHORT).show()
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ball = filterList[position]
        holder.binding.ball = ball
        holder.binding.executePendingBindings()
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchKey = constraint.toString()
                if (searchKey.isEmpty()) {
                    filterList = ballList as ArrayList<Balls>
                }
                else {
                    val resultList = ArrayList<Balls>()  //temporary array
                    ballList.forEach {
                        if (it.name.toLowerCase().contains(searchKey.toLowerCase())) {
                            resultList.add(it)
                        }
                    }
                    filterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as ArrayList<Balls>
                //inform the recyclerview to refresh the data
                notifyDataSetChanged()
            }
        }
    }
}