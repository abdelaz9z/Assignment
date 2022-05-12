package com.hyperone.assignment.utils

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hyperone.assignment.R
import com.hyperone.assignment.adapter.ListAdapter
import com.hyperone.assignment.room.AppDatabase
import com.hyperone.assignment.room.Source

class DatabaseRoomUtil : Activity() {

    //==============================================================================================
    // ● Save response items in local Database Room                                                                                              =
    //==============================================================================================

    /**
     * Insert data (video, pdf) to database (Room) and open the detail activity
     *
     * @param type String
     * @param url String
     * @param name String
     */
    fun insertDataToDatabase(
        db: AppDatabase,
        uid: Int,
        type: String,
        url: String,
        name: String
    ) {
        if (inputCheck(type, url, name)) {
            // Create User Object
            val source = Source(uid, type, url, name)

            // Add Data to Database
            val sourceDao = db.sourceDao()
            sourceDao.insertAll(source)

            Toast.makeText(this, "Successfully added!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Check if the input is valid
     *
     * @param type String
     * @param url String
     * @param name String
     * @return Boolean
     */
    private fun inputCheck(type: String, url: String, name: String): Boolean {
        return !(TextUtils.isEmpty(type) && TextUtils.isEmpty(url) && TextUtils.isEmpty(name))
    }

    /**
     * My source bottom sheet dialog fragment class (for video, pdf)
     */
    fun mySourceBottomSheetDialog(
        db: AppDatabase,
        recyclerViewMySource: RecyclerView = findViewById(R.id.recyclerViewMySource),
        sourceArrayList: ArrayList<Source> = ArrayList()
    ) {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_my_source, null)

        val imageButtonMySourceDone = view.findViewById<ImageButton>(R.id.imageButtonMySourceDone)

        recyclerViewMySource.layoutManager = LinearLayoutManager(this)
        recyclerViewMySource.setHasFixedSize(true)

        val sourceDao = db.sourceDao()
        val sources: List<Source> = sourceDao.getAll()

        for (source in sources) {

            val type = source.type
            val url = source.url
            val name = source.name

            Log.i("Database room", "id: $type url: $url name: $name")

            sourceArrayList.addAll(listOf(source))
            val listAdapter = ListAdapter(sourceArrayList)
            recyclerViewMySource.adapter = listAdapter
        }

        // Dismiss the dialog when the user makes a selection
        imageButtonMySourceDone.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                behaviour.isDraggable = false
            }
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }
//==============================================================================================
}