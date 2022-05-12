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

//    fun startDownload(url: String, button: Button, progress: ProgressBar) {
//        val disposable = url.download()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeBy(
//                onNext = { progress ->
//                    //download progress
//                    button.text = "${progress.downloadSizeStr()}/${progress.totalSizeStr()}"
//                    button.setProgress(progress)
//                },
//                onComplete = {
//                    //download complete
//                    button.text = "Open"
//                },
//                onError = {
//                    //download failed
//                    button.text = "Retry"
//                }
//            )
//    }

    private fun taskManager(url: String): TaskManager {
        // Get a TaskManager object
        val taskManager = url.manager()

        //keep this tag for dispose
        val tag = taskManager.subscribe { status ->
            // Receive download status
            when (status) {
                is Normal -> {
                }
                is Started -> {
                }
                is Downloading -> {
                }
                is Paused -> {
                }
                is Completed -> {
                }
                is Failed -> {
                }
                is Deleted -> {
                }
                else -> {
                }
            }
        }

        return taskManager
    }

    /**
     * Download file from url and save it to local storage with given name and extension.
     *
     * @param taskManager String
     */
    fun getDownloadFile(taskManager: TaskManager) {
        val file = taskManager.file()
        // use file...

    }
}