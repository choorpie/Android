package com.example.android.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class SportsAdapter(val SportList: List<Sports>): RecyclerView.Adapter<SportsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val sportImage: ImageView = view.findViewById(R.id.imageView)
        val sportName: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_layout_card, parent, false)
        val viewHolder = ViewHolder(view)

        viewHolder.itemView.setOnClickListener{

            val name = SportList[viewHolder.adapterPosition].name
            val imageId = SportList[viewHolder.adapterPosition].imageId

//            Toast.makeText(parent.context, name, Toast.LENGTH_SHORT).show()
//            when(SportList[viewHolder.adapterPosition].name){
//                ""
//            }


            view.findNavController()
                    .navigate(TitleFragmentDirections
                            .actionTitleFragmentToGameFragment(name))


        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return SportList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sport = SportList[position]
        holder.sportImage.setImageResource(sport.imageId)
        holder.sportName.text = sport.name
    }


}