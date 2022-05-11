package com.hyperone.assignment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.hyperone.assignment.R
import com.hyperone.assignment.const.Layout.HORIZONTAL
import com.hyperone.assignment.models.Source

/**
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
    var onItemClick: ((Source) -> Unit)? = null

    /**
     * Inflates the appropriate list item layout and returns the view holder
     */
    var onButtonClick: ((Source) -> Unit)? = null

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
        //  if the layout variable is Layout.GRID the grid list item should be used. Otherwise the
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
     * @return Int
     */
    override fun getItemCount(): Int {
        return sources.size
    } // return the size of the data set

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

        if (sourcesData.type == "PDF")
            holder.imageType?.setImageResource(R.drawable.ic_baseline_picture_as_pdf_24)
        else
            holder.imageType?.setImageResource(R.drawable.ic_baseline_video_24)

        // Set the text for the current title of the article
        holder.textName?.text = sourcesData.name

        // Set the text for the current author of the article
        holder.textUrl?.text = sourcesData.url

        holder.imageButtonDownload?.setOnClickListener {
            onButtonClick?.invoke(sourcesData)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(sourcesData)
        }
    }
}