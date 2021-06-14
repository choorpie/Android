package com.example.finaldemo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.finaldemo.database.HotelDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var sp: SharedPreferences
    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup navigation controller with the up button
        navController = this.findNavController(R.id.navController)
        NavigationUI.setupActionBarWithNavController(this, navController)

        // retrieve the database data
        val application = requireNotNull(this.application)
        val dataSource = HotelDatabase.getInstance(application).hotelDatabaseDao

        // get the shared viewModel associated with the activity
        viewModel = ViewModelProvider(this,
                MyViewModelFactory(this.application)).get(MyViewModel::class.java)

        //check whether the database is created or not
        //database will be initialized once in this project
        //write a mark to a sharedpreference file
        sp = getPreferences(Context.MODE_PRIVATE)
        val databaseState = sp.getBoolean("Created", false)
        if (!databaseState) {
            viewModel.initDB()
            val editor = sp.edit()
            editor.putBoolean("Created", true)
            editor.apply()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

//    internal fun onOpenMap() {
//        supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container_view_tag, MapsFragment())
//                .commitNow()
//    }
}