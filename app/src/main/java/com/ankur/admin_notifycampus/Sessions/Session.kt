package com.ankur.admin_notifycampus.Sessions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.ankur.admin_notifycampus.Models.NoticeModel
import com.ankur.admin_notifycampus.adapters.RemoveNoticeAdapter
import com.ankur.admin_notifycampus.databinding.ActivitySession23Binding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Session : AppCompatActivity() {

    private lateinit var binding:ActivitySession23Binding
    private lateinit var  collection: String
    private val TAG="session23"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySession23Binding.inflate(layoutInflater)
        setContentView(binding.root)

        collection=intent.getStringExtra("session").toString()
        Log.d(TAG,"collection is $collection")
        getSession23Notice()

    }

    private fun getSession23Notice() {

        val list = ArrayList<NoticeModel>()
        lifecycleScope.launch(Dispatchers.IO){
            Firebase.firestore.collection(collection).get().await()
                .also {
                    for (doc in it.documents){
                        val data = doc.toObject(NoticeModel::class.java)
//                        Log.d(TAG,"data : $data")
                        list.add(data!!)
                        Log.d(TAG,"list : $list")

                    }
                }

            withContext(Dispatchers.Main){
                binding.recyclerView.adapter=RemoveNoticeAdapter(this@Session,list)
            }
        }

    }
}