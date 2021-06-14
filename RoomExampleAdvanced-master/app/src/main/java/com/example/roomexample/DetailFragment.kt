package com.example.roomexample

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.roomexample.database.SceneDatabase
import com.example.roomexample.databinding.FragmentDetailBinding

//fragment to display the detailed scene selected from the recyclerview
class DetailFragment : Fragment() {

   private lateinit var binding: FragmentDetailBinding
   private lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        //retrieve the database dao
        val application = requireNotNull(this.activity).application
        val dataSource = SceneDatabase.getInstance(application).sceneDatabaseDao

        //shared viewmodel with the activity
        viewModel = ViewModelProvider(requireActivity(), MyViewModelFactory(dataSource)).get(MyViewModel::class.java)

        //retrieve the passed argument (selected scene's id from the recyclerview)
        val args = DetailFragmentArgs.fromBundle(requireArguments())
        viewModel.getScene(args.rawId)

        //set an observer to the liveData and hence update the UI
        viewModel.selectedScene.observe(viewLifecycleOwner, Observer {
            //do data binding in the layout
            binding.scene = it
        })

        return binding.root
    }

}