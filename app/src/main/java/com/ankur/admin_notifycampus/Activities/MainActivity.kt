package com.ankur.admin_notifycampus.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ankur.admin_notifycampus.R
import com.ankur.admin_notifycampus.Sessions.SessionTemplate
import com.ankur.admin_notifycampus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addNotice.setOnClickListener(this)
        binding.addFaculty.setOnClickListener(this)
        binding.deleteNotice.setOnClickListener(this)
        binding.deleteFaculty.setOnClickListener(this)


    }

    override fun onClick(v: View?) {

        if (v != null) {
            when(v.id){

                R.id.add_notice ->{
                    val intent = Intent(this,AddNotice::class.java)
                    startActivity(intent)
                }
                R.id.addFaculty ->{
                    val intent = Intent(this,AddFaculty::class.java)
                    startActivity(intent)
                }
                R.id.deleteNotice->{
                    val intent=Intent(this,SessionTemplate::class.java)
                    startActivity(intent)
                }
                R.id.deleteFaculty->{
                    val intent =Intent(this,SessionTemplate::class.java)
                    startActivity(intent)
                }

            }
        }
    }
}