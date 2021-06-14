package com.example.roomexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.roomexample.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainScope = MainScope()
    private lateinit var userDao: UserDao
    private var allData: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get the database dao
        userDao = UserDatabase.getInstance(this).userDao
        //generate test data
        val user1 = User("Tom", 30)
        val user2 = User("Robert", 40)
        //database test1
//        thread {
//            user1.id = userDao.insertUser(user1)
//            user2.id = userDao.insertUser(user2)
//            //can not update the UI here
//        }

        binding.button1.setOnClickListener {
            //run on the main thread
            mainScope.launch {
                user1.id = insertUser(user1)
                user2.id = insertUser(user2)
                loadAllUsers()
                binding.textView.text = allData
            }
        }

        binding.button2.setOnClickListener {
            mainScope.launch {
                user1.age = 42
                updateUser(user1)
                loadAllUsers()
                binding.textView.text = allData
            }
        }

        binding.button3.setOnClickListener {
            mainScope.launch {
                deleteAllUser()
            }
        }
    }

    suspend private fun insertUser(user: User): Long {
        return withContext(Dispatchers.IO) {
            userDao.insertUser(user)
        }
    }

    suspend private fun updateUser(user: User) {
        withContext(Dispatchers.IO) {
            userDao.updateUser(user)
        }
    }

    suspend private fun deleteAllUser() {
        withContext(Dispatchers.IO) {
            userDao.deleleAllUser()
        }
    }

    suspend private fun loadAllUsers() {
        allData = ""
        withContext(Dispatchers.IO) {
            for (user in userDao.loadAllUsers()) {
                Log.d("Main", user.toString())
                allData += user.toString()
            }
        }
    }
}