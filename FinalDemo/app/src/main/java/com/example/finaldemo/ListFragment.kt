package com.example.finaldemo

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finaldemo.Adapter.HotelAdapter
import com.example.finaldemo.Adapter.SwipeHandler
import com.example.finaldemo.database.HotelDatabase
import com.example.finaldemo.databinding.FragmentListBinding

//fragment with a recyclerview to show a list of hotels activities
class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: MyViewModel
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)

        //retrieve the database dao
        val application = requireNotNull(this.activity).application
        val dataSource = HotelDatabase.getInstance(application).hotelDatabaseDao

        //get the shared viewModel associated with the activity
        viewModel = ViewModelProvider(requireActivity(),
                MyViewModelFactory(requireActivity().application)).get(MyViewModel::class.java)

        //setup RecyclerView
        val layoutManager = LinearLayoutManager(this.activity)
        binding.recyclerView.layoutManager = layoutManager
        val adapter = HotelAdapter(requireActivity(), viewModel) //based on ListAdapter
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this.activity, DividerItemDecoration.VERTICAL))

        //setup swipe handler
        val swipeHandler = ItemTouchHelper(SwipeHandler(adapter,0,(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)))
        swipeHandler.attachToRecyclerView(binding.recyclerView)

        //observe any changes on the data source of the recylerview
        //hotelList is a livedata return by the database query
        viewModel.hotelList.observe(viewLifecycleOwner, Observer {
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

        // Initialize Search View
        searchView = menu?.findItem(R.id.searchView)?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                context?.hideKeyboard()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.searchHotel(query!!)
                return true
            }
        })

        searchView.setOnCloseListener(object: SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                viewModel.getAllHotels()
                searchView.onActionViewCollapsed()
                context?.hideKeyboard()
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.
        onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    fun Context.hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}