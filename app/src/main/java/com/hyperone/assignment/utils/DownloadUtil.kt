package com.hyperone.assignment.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import zlc.season.rxdownload4.manager.*

class DownloadUtil {
    /**
     * Download file from url and save it to local storage with given name and extension.
     *
     * @param urlString String
     * @param title String
     * @param description String
     * @param context Context
     */
    fun downloadFileFromUrl(
        urlString: String,
        title: String,
        description: String,
        context: Context
    ) {
        // Request to download url file and save it in local storage
        val request = DownloadManager.Request(Uri.parse(urlString))
            // Title of file
            .setTitle(title)
            .setDescription(description)
            // Notify when download is complete
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            // Allow over method internet connection
            .setAllowedOverMetered(true)

        // Get download system service
        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        // Set our request
        manager.enqueue(request)
    }
}