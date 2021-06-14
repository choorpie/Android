package com.example.finaldemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.finaldemo.database.Hotel
import com.example.finaldemo.database.HotelDatabase
import com.example.finaldemo.databinding.FragmentAddBinding

//fragment to add a new hotel into the database
class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var viewModel: MyViewModel
    private var newHotel = Hotel("", "", 0, "", 0, "")
    val PICKUPIMAGE = 1
    val PICKUPMAP = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)

        //retrieve the database dao
        val application = requireNotNull(this.activity).application
        val dataSource = HotelDatabase.getInstance(application).hotelDatabaseDao

        viewModel = ViewModelProvider(requireActivity(),
                MyViewModelFactory(requireActivity().application)).get(MyViewModel::class.java)

        //enable the photo pickup
        binding.selPhotoButton.setOnClickListener {
            val photo_intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            photo_intent.addCategory(Intent.CATEGORY_OPENABLE)
            photo_intent.type = "image/*"
            startActivityForResult(photo_intent, PICKUPIMAGE)
//            this.startActivity(photo_intent)
        }

        //enable the map pickup
        binding.selMapButton.setOnClickListener {
            val map_intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            map_intent.addCategory(Intent.CATEGORY_OPENABLE)
            map_intent.type = "image/*"
            startActivityForResult(map_intent, PICKUPMAP)
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun saveData() {
        newHotel.city = binding.cityEdit.text.toString()
        newHotel.name = binding.nameEdit.text.toString()
        newHotel.description = binding.descriptEdit.text.toString()
        newHotel.phoneNumber = binding.phoneNuberEdit.text.toString()
//        newHotel.mapId
        viewModel.insertHotel(newHotel)
        activity?.onBackPressed()  //simulate the press of the back button
    }

    // get the photo file path returned from the intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PICKUPIMAGE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let {
                        newHotel.photoFile = it.toString()
                    }
                }
            }
            PICKUPMAP -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let {
                        newHotel.fileMap = it.toString()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.file_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> saveData()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        // Hide the keyboard.
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}