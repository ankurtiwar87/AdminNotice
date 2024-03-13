package com.ankur.admin_notifycampus.adapters

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SwipeToDeleteCallback(private val adapter: RemoveNoticeAdapter, private val collectionName: String) : ItemTouchHelper.SimpleCallback(
    0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        val position = viewHolder.adapterPosition
        val currentItem = adapter.list[position] // Assuming list is the list of NoticeModel objects in your adapter

        adapter.removeItem(position)

        val db = FirebaseFirestore.getInstance()
        val itemsCollection = db.collection(collectionName)

        try {

            CoroutineScope(Dispatchers.IO).launch {
                // Use await to call Firestore's delete method asynchronously
                itemsCollection.document(currentItem.id.toString()).delete().await()
            }

        } catch (e: Exception) {
            // Handle exceptions here
            // For example: Log.e(TAG, "Error deleting item from Firestore", e)
        }

    }
}