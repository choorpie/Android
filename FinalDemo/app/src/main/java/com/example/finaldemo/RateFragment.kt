package com.example.finaldemo

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.finaldemo.database.Hotel
import com.example.finaldemo.database.HotelDatabase
import com.example.finaldemo.databinding.FragmentAddBinding
import com.example.finaldemo.databinding.FragmentRateBinding

class RateFragment : Fragment() {

    private lateinit var binding: FragmentRateBinding
    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition =
                TransitionInflater
                        .from(context)
                        .inflateTransition(android.R.transition.slide_right)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var display = 0

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rate, container, false)

        //shared viewmodel with the activity
        viewModel = ViewModelProvider(requireActivity(),
            MyViewModelFactory(requireActivity().application)).get(MyViewModel::class.java)

        //retrieve the database dao
        val application = requireNotNull(this.activity).application
        val dataSource = HotelDatabase.getInstance(application).hotelDatabaseDao

        val args = RateFragmentArgs.fromBundle(requireArguments())

        binding.imageView.setOnClickListener {
            display = 1
            it.findNavController()
                .navigate(RateFragmentDirections.actionRateFragmentToDetailFragment(args.rawId, display))
        }

        binding.imageView2.setOnClickListener {
            display = 2
            it.findNavController()
                .navigate(RateFragmentDirections.actionRateFragmentToDetailFragment(args.rawId, display))
        }

        binding.imageView3.setOnClickListener {
            display = 3
            it.findNavController()
                .navigate(RateFragmentDirections.actionRateFragmentToDetailFragment(args.rawId, display))
        }

        binding.imageView0.setOnClickListener {
            display = 0
            it.findNavController()
                .navigate(RateFragmentDirections.actionRateFragmentToDetailFragment(args.rawId, display))
        }

        return binding.root
    }

}