package com.ankur.admin_notifycampus.Activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.ankur.admin_notifycampus.Models.FacultyModel
import com.ankur.admin_notifycampus.R
import com.ankur.admin_notifycampus.databinding.ActivityAddFacultyBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class AddFaculty : AppCompatActivity() {

    private lateinit var binding:ActivityAddFacultyBinding
    //    This URI is for Image from Gallery
    private var coverImage: Uri? = null
    private lateinit var dialog: Dialog
    private var spinnerYear:String=""
    private var spinnerBlock:String=""

    private var facultyImageUrl:String ?= ""
    private val storage = Firebase.storage
    private val storageRef = storage.reference




    //    This is used to went to gallery
    private var launchGalleyActivity =registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

        if (it.resultCode== Activity.RESULT_OK)
        {
            coverImage=it.data!!.data
            binding.FacultyImage.setImageURI(coverImage)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityAddFacultyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        dialog= Dialog(this)
        dialog.setContentView(R.layout.progress_dialog)
        dialog.setCancelable(false)

//        OnClick Gallery Open
        binding.FacultyImage.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type="image/*"
            launchGalleyActivity.launch(intent)
        }


        val year = resources.getStringArray(R.array.year)
        val block = resources.getStringArray(R.array.department)

       //spinner for year

        val adapter1 = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,year)
        binding.spinner1.adapter=adapter1

        binding.spinner1.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerYear=year[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@AddFaculty, "Select Category", Toast.LENGTH_SHORT).show()
            }

        }

        //spinner for block

        val adapter2 = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,block)
        binding.spinner2.adapter=adapter2

        binding.spinner2.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerBlock=block[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@AddFaculty, "Select Category", Toast.LENGTH_SHORT).show()
            }

        }


        binding.submit.setOnClickListener {
            validateData()
        }

    }

    private fun validateData() {
        val teacherName = binding.TeacherName.text.toString()
        val phoneNumber = binding.TeacherPhoneNumber.text.toString()
        val email = binding.email.text.toString()
        val cabin = binding.Cabin.text.toString()

        if (teacherName.isEmpty()){
            binding.TeacherName.error="Enter Name"
            binding.TeacherName.requestFocus()
            return
        }

        if (phoneNumber.isEmpty()){
            binding.TeacherPhoneNumber.error="Enter Phone No."
            binding.TeacherPhoneNumber.requestFocus()
            return
        } else if (!isValidPhoneNumber(phoneNumber)) {
            binding.TeacherPhoneNumber.error = "Enter Correct Phone No."
            binding.TeacherPhoneNumber.requestFocus()
            return
        }

        if (email.isEmpty()){
            binding.email.error="Enter email"
            binding.email.requestFocus()
            return
        } else if (!isValidEmail(email)) {
            binding.email.error = "Enter Correct email"
            binding.email.requestFocus()
            return
        }

        if (cabin.isEmpty()){
            binding.Cabin.error="Enter Cabin No."
            binding.Cabin.requestFocus()
            return
        }
        if (coverImage == null){
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show()
        } else {
            uploadImage()
        }
    }



    private fun uploadImage() {


        dialog.show()

        val fileName = UUID.randomUUID().toString()+".jpg"

        val refStorage = storageRef.child("Faculty Images").child("Images/$fileName")
        refStorage.putFile(coverImage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { Image  ->
//                    Data to be upload on firebase
                    facultyImageUrl=Image.toString()

                    uploadData()
                }
            }.addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(this, "Something went wrong with Storage", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadData() {
        val db = Firebase.firestore.collection(spinnerYear)
        val id = db.document().id


        val data = FacultyModel(
            id,
           name =  binding.TeacherName.text.toString(),
            phoneNo = binding.TeacherPhoneNumber.text.toString(),
            email = binding.email.text.toString(),
            cabinNo = binding.Cabin.text.toString(),
            year = spinnerYear,
            block = spinnerBlock,
            imageUrl = facultyImageUrl
        )


        lifecycleScope.launch(Dispatchers.IO) {

            db.document(id).set(data).await()

            withContext(Dispatchers.Main){

//                reset the values
                dialog.dismiss()
                binding.FacultyImage.setImageResource(R.drawable.faculty_image)
                binding.TeacherName.text=null
                binding.email.text=null
                binding.TeacherPhoneNumber.text=null
                binding.Cabin.text=null
                binding.spinner1.setSelection(0)
                binding.spinner2.setSelection(0)
                Toast.makeText(applicationContext,"Faculty Successfully added",Toast.LENGTH_SHORT).show()

            }

        }


    }

    fun isValidPhoneNumber(phone: String): Boolean {
        val phoneRegex = Regex("^[6-9][0-9]{9}$")
        return phoneRegex.matches(phone)
    }


    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        return emailRegex.matches(email)
    }

}