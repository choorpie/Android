package com.example.finaldemo.Adapter

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.finaldemo.ListFragmentDirections
import com.example.finaldemo.MyViewModel
import com.example.finaldemo.database.Hotel
import com.example.finaldemo.databinding.ItemLayoutBinding

class HotelAdapter(val view: Context, val viewModel: MyViewModel) : ListAdapter<Hotel, HotelAdapter.ViewHolder>(BallDiffCallback()), SwipeHandlerInterface {

    class BallDiffCallback : DiffUtil.ItemCallback<Hotel>(){
        override fun areItemsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(layoutInflater, parent, false)
        val viewHolder = ViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            //get the selected hotel's id in the database
            val rawId = getItem(viewHolder.bindingAdapterPosition).id
            //pass the id to the detailfragment
            it.findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToDetailFragment(rawId))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hotel = getItem(position)
        holder.binding.hotel = hotel
        holder.binding.executePendingBindings()
    }

    override fun onItemDelete(position: Int) {
        //the view has been removed out of the screen
        val deletedHotel = getItem(position)
        AlertDialog.Builder(view).apply {
            setTitle("Delete this hotel?")
            setCancelable(false)
            setPositiveButton("Yes") {dialog, which ->
                viewModel.deleteHotel(deletedHotel) //delete the hotel from the database
            }
            setNegativeButton("No") {dialog, which ->
                notifyItemChanged(position) //restore the view
            }
            show()
        }
    }
}

//for resolve app:setImage in the item_layout.xml
@BindingAdapter("setImage")
fun ImageView.setSceneImage(hotel: Hotel?) {
    hotel?.let {
        if (hotel.photoFile.isNotEmpty()) {
            Glide.with(this.context)
                .load(Uri.parse(hotel.photoFile))
                .apply(RequestOptions().centerCrop())
                .into(this)
        } else {
            setImageResource(hotel.photoId)
        }
    }
}
@BindingAdapter("android:src")
fun ImageView.setImageResource(hotel: Hotel?) {
    hotel?.let {
        if (hotel.fileMap.isNotEmpty()) {
            Glide.with(this.context)
                    .load(Uri.parse(hotel.fileMap))
                    .apply(RequestOptions().centerCrop())
                    .into(this)
        } else {
            setImageResource(hotel.mapId)
        }
    }
}

