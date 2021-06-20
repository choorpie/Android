package com.example.finaldemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.finaldemo.database.Hotel
import com.example.finaldemo.database.HotelDatabase
import com.example.finaldemo.databinding.FragmentDetailBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DetailFragment : Fragment(){

    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: MyViewModel
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val display = false

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        //retrieve the database dao
        val application = requireNotNull(this.activity).application
        val dataSource = HotelDatabase.getInstance(application).hotelDatabaseDao

        //shared viewmodel with the activity
        viewModel = ViewModelProvider(requireActivity(),
                MyViewModelFactory(requireActivity().application)).get(MyViewModel::class.java)

        //retrieve the passed argument (selected hotel's id from the recyclerview)
        val args = DetailFragmentArgs.fromBundle(requireArguments())
        viewModel.getHotel(args.rawId)

        //set an observer to the liveData and hence update the UI
        viewModel.selectedHotel.observe(viewLifecycleOwner, Observer {
            //do data binding in the layout
            binding.hotel = it
//            binding.mapImage.setImageResource(args.rawId)
        })

        //enable the phone dialer
        binding.phoneNumber.setOnClickListener {
            val phoneNumber = binding.phoneNumber.text.toString().trim()
            val phone_intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber))
            startActivity(phone_intent)
        }

        // rate it button
        binding.rateButton.setOnClickListener(){
            it.findNavController()
                .navigate(DetailFragmentDirections.actionDetailFragmentToRateFragment(args.rawId, args.displayImg))
        }

        // map it button
        val extras = FragmentNavigatorExtras()

        binding.mapImage.setOnClickListener(){
            val passedHotel = viewModel.selectedHotel.value!!
            it.findNavController()
                .navigate(DetailFragmentDirections.actionDetailFragmentToMapsFragment(passedHotel.name, passedHotel.name))
        }

        when(args.displayImg){
            1 -> binding.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_sentiment_very_satisfied_black_24dp))
            2 -> binding.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_sentiment_dissatisfied_black_24dp))
            3 -> binding.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_mood_bad_black_24dp))
            0 -> binding.icon.visibility = View.INVISIBLE
        }

//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = getFragmentManager()?.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

        return binding.root
    }



}