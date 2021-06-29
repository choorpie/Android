package com.example.finaldemo

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.finaldemo.databinding.FragmentBookBinding
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BookFragment : Fragment() {
    private lateinit var binding: FragmentBookBinding
    // Calender
    val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    //do what you want here
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book, container, false)
        binding.dateIn.setOnClickListener {
            context?.hideKeyboard()
            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener{ view, mYear, mMonth, mDay ->

                // set to textView
                binding.dateIn.setText("" + mDay + " / " + mMonth + " / " + mYear)
                Log.i("now", mMonth.toString())
            }, year, month+1, day)
            // show dialog
            dpd.show()
        }

        binding.dateOut.setOnClickListener {
            context?.hideKeyboard()
            val yearOut = year
            val monthOut = month
            val dayOut = day
            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener{ view, mYear, mMonth, mDay ->
                // set to textView
                binding.dateOut.setText("" + mDay + " / " + mMonth + " / " + mYear)
                Log.i("now", mMonth.toString())
            }, year, month+1, day)
            // show dialog
            dpd.show()
        }

        binding.sureBookButton.setOnClickListener {
            var dateIn = binding.dateIn.text
            var dateOut = binding.dateOut.text
            var DayMonthYearIn = dateIn.split(Regex("/")) // ["day", "month, "year"]
            var DayMonthYearOut = dateOut.split(Regex("/")) // ["day", "month, "year"]

            if(DayMonthYearIn[0] >= DayMonthYearOut[0]){
                if(DayMonthYearIn[1] >= DayMonthYearOut[1]){
//                    Toast.makeText(context!!, "No", Toast.LENGTH_SHORT).show()
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Please maker sure about data")
                    builder.setMessage("入住日期不能晚於退房日期!!")

                    builder.setPositiveButton("Done"){ dialog,_ ->
                        dialog.dismiss()
                    }
                    builder.show()
                }
            }


//            for(str in DayMonthYear){
//                Toast.makeText(context!!, str, Toast.LENGTH_SHORT).show()
//            }

        }

        binding.cancelBookButton.setOnClickListener{
            it.findNavController()
                .navigateUp()
        }

        return binding.root
    }

    fun Context.hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}