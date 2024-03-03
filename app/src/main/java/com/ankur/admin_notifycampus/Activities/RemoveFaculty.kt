package com.ankur.admin_notifycampus.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ankur.admin_notifycampus.R
import com.ankur.admin_notifycampus.databinding.ActivityRemoveFacultyBinding

class RemoveFaculty : AppCompatActivity() {

    private lateinit var binding: ActivityRemoveFacultyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRemoveFacultyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}