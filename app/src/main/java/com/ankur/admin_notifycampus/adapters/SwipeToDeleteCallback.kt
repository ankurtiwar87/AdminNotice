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

import android.view.View
import androidx.core.content.ContextCompat
import com.ankur.admin_notifycampus.R
import com.google.android.material.snackbar.Snackbar

class SwipeToDeleteCallback(private val adapter: RemoveNoticeAdapter, private val collectionName: String, private val recyclerView: RecyclerView
) : ItemTouchHelper.SimpleCallback(
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
        val currentItem = adapter.list[position]

        adapter.removeItem(position)

        val db = FirebaseFirestore.getInstance()
        val itemsCollection = db.collection(collectionName)

        val snackbar = Snackbar.make(recyclerView, "Item deleted", Snackbar.LENGTH_LONG).apply {
            setAction("Undo") {
                adapter.restoreItem(currentItem, position)
                recyclerView.scrollToPosition(position)
            }
            setActionTextColor(ContextCompat.getColor(recyclerView.context, R.color.color3)) // Change color to your theme color
            addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (event != DISMISS_EVENT_ACTION) {
                        // User did not undo, proceed with deletion
                        try {
                            CoroutineScope(Dispatchers.IO).launch {
                                itemsCollection.document(currentItem.id.toString()).delete().await()
                            }
                        } catch (e: Exception) {
                            // Handle exceptions here
                        }
                    }
                    super.onDismissed(transientBottomBar, event)
                }
            })
        }

        snackbar.show()
    }
}
