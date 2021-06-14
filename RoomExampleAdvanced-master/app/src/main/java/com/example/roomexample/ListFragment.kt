package com.example.roomexample

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomexample.Adapter.SceneAdapter
import com.example.roomexample.Adapter.SwipeHandler
import com.example.roomexample.database.SceneDatabase
import com.example.roomexample.databinding.ListFragmentBinding

//fragment with a recyclerview to show a list of ball activities
class ListFragment : Fragment() {

    private lateinit var binding: ListFragmentBinding
    private lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false)

        //retrieve the database dao
        val application = requireNotNull(this.activity).application
        val dataSource = SceneDatabase.getInstance(application).sceneDatabaseDao

        //get the shared viewModel associated with the activity
        viewModel = ViewModelProvider(requireActivity(), MyViewModelFactory(dataSource)).get(MyViewModel::class.java)

        //setup RecyclerView
        val layoutManager = LinearLayoutManager(this.activity)
        binding.recyclerView.layoutManager = layoutManager
        val adapter = SceneAdapter(requireActivity(), viewModel) //based on ListAdapter
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this.activity, DividerItemDecoration.VERTICAL))

        //setup swipe handler
        val swipeHandler = ItemTouchHelper(SwipeHandler(adapter,0,(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)))
        swipeHandler.attachToRecyclerView(binding.recyclerView)

        //observe any changes on the data source of the recylerview
        //sceneList is a livedata return by the database query
        viewModel.sceneList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)  //submit the up-to-date ballList to the recyclerView
            }
        })

        //enable options menu
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.
               onNavDestinationSelected(item, requireView().findNavController())
               || super.onOptionsItemSelected(item)
    }

}