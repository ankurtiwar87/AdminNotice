package com.ankur.admin_notifycampus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ankur.admin_notifycampus.Activities.AddFaculty
import com.ankur.admin_notifycampus.Activities.AddNotice
import com.ankur.admin_notifycampus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addNotice.setOnClickListener(this)
        binding.addFaculty.setOnClickListener(this)


    }

    override fun onClick(v: View?) {

        if (v != null) {
            when(v.id){

                R.id.add_notice->{
                    val intent = Intent(this,AddNotice::class.java)
                    startActivity(intent)
                }
                R.id.addFaculty->{
                    val intent = Intent(this,AddFaculty::class.java)
                    startActivity(intent)
                }

            }
        }
    }
}