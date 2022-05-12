package com.hyperone.assignment.utils

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.hyperone.assignment.room.AppDatabase
import com.hyperone.assignment.room.Source

/**
 * This class is used to create database and insert data in it.
 * It also provides methods to insert data from database.
 *
 * @author Abdelaziz Daoud
 * @since 11/05/2022
 *
 * @property context Context
 * @constructor
 */
class DatabaseRoomUtil(private val context: Context) {

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

            Toast.makeText(context, "Successfully added!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Please fill out all fields.", Toast.LENGTH_LONG).show()
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
//==============================================================================================
}