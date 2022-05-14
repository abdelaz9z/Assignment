package com.hyperone.assignment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.hyperone.assignment.R
import com.hyperone.assignment.const.Layout.HORIZONTAL
import com.hyperone.assignment.const.SourceType.PDF
import com.hyperone.assignment.models.Source
import com.hyperone.assignment.room.AppDatabase
import com.hyperone.assignment.utils.DatabaseRoomUtil
import com.hyperone.assignment.utils.DownloadUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import zlc.season.rxdownload4.download

/**
 * SourceAdapter class is used to display the list of sources in recycler view.
 *
 * @author Abdelaziz Daoud
 * @since 12/05/2022
 *
 * Adapter to inflate the appropriate list item layout and populate the view with information
 * from the appropriate data source
 */
class SourceAdapter(
    private val sources: ArrayList<Source>,
    private val layout: Int
) : RecyclerView.Adapter<SourceAdapter.PdfViewHolder>() {

    /**
     * Inflates the appropriate list item layout and returns the view holder
     */
    private var onItemClick: ((Source) -> Unit)? = null

    /**
     * Inflates the appropriate list item layout and returns the view holder
     */
    private var onButtonClick: ((Source) -> Unit)? = null

    /**
     * Inflates the appropriate list item layout and returns the view holder
     *
     * @property imageType ImageView?
     * @property textName TextView?
     * @property textUrl TextView?
     * @property imageButtonDownload ImageButton?
     * @property progressBarDownload LinearProgressIndicator?
     * @constructor
     */
    inner class PdfViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        // Declare and initialize all of the list item UI components
        val imageType: ImageView? = view!!.findViewById(R.id.imageType)
        val textName: TextView? = view!!.findViewById(R.id.textName)
        val textUrl: TextView? = view!!.findViewById(R.id.textUrl)
        val imageButtonDownload: ImageButton? = view!!.findViewById(R.id.imageButtonDownload)
        val progressBarDownload: LinearProgressIndicator? =
            view!!.findViewById(R.id.progressBarDownload)
    }

    /**
     * Inflate the appropriate list item layout and populate the view with information
     *
     * @param parent ViewGroup
     * @param viewType Int
     * @return PdfViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        //  Use a conditional to determine the layout type and set it accordingly.
        //  the vertical/horizontal list item should be used.
        val inflate = when (layout) {
            // Inflate the layout
            HORIZONTAL -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_video_horizontal, parent, false)
            else -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pdf_vertical, parent, false)
        }

        //  Null should not be passed into the view holder. This should be updated to reflect
        //  the inflated layout.
        return PdfViewHolder(inflate)
    }

    /**
     * Bind the view holder with the appropriate data source
     *
     * @return Int return the size of the data set
     */
    override fun getItemCount(): Int {
        return sources.size
    }

    /**
     * Bind the view holder with the appropriate data source
     *
     * @param holder PdfViewHolder
     * @param position Int
     */
    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {

        // Get the data at the current position
        val sourcesData = sources[position]

        // Set the image type
        if (sourcesData.type == PDF)
            holder.imageType?.setImageResource(R.drawable.ic_baseline_picture_as_pdf_24)
        else
            holder.imageType?.setImageResource(R.drawable.ic_baseline_video_24)

        // Set the text for the current text of the source
        holder.textName?.text = sourcesData.name

        // Set the text for the current url of the source
        holder.textUrl?.text = sourcesData.url

        holder.imageButtonDownload?.setOnClickListener {
            onButtonClick?.invoke(sourcesData)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(sourcesData)
        }

        holder.imageButtonDownload?.setOnClickListener {
            // Get the current state of the download button
            val id = sourcesData.id!!.toInt()
            val type = sourcesData.type.toString()
            var url = sourcesData.url.toString().trim()
            val name = sourcesData.name.toString()

            // I/Download: Downloading PDF 3 from (https://kotlinlang.org/docs/kotlin-reference.pdf with id 3 and type PDF
            // I/Download: Downloading PDF 9 from https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf with id 9 and type PDF
            Log.i("Download", "Downloading $name from $url with id $id and type $type")

            if (url.startsWith("(")) {
                url = url.substring(1)
            }

            // Download the file
            DownloadUtil().downloadFileFromUrl(url, name, type, holder.itemView.context)

            // Set the progress bar to visible
            val disposable = url.download().observeOn(AndroidSchedulers.mainThread())?.subscribeBy(
                onNext = { progress ->
                    holder.progressBarDownload?.max = progress.totalSize.toInt()
                    holder.progressBarDownload?.progress = progress.downloadSize.toInt()

                },
                onComplete = {
                    //download complete (Open file)
                    holder.imageButtonDownload.setImageResource(R.drawable.ic_baseline_folder_24)
                    holder.imageButtonDownload.isEnabled = false

                    // Insert the downloaded file into the database room
                    val db = Room.databaseBuilder(
                        holder.itemView.context, AppDatabase::class.java,
                        "database-name"
                    ).allowMainThreadQueries().build()
                    DatabaseRoomUtil(holder.itemView.context).insertDataToDatabase(
                        db, id, type, url, name
                    )
                },
                onError = {
                    //download failed (Retry button)
                    holder.imageButtonDownload.setImageResource(R.drawable.ic_baseline_loop_24)
                    holder.imageButtonDownload.isEnabled = true
                }
            )
            Log.i("SourceAdapter", disposable.toString())
        }
    }
}