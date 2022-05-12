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
import com.hyperone.assignment.const.SourceType
import com.hyperone.assignment.room.Source

class ListAdapter(private var sources: ArrayList<Source>) : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Declare and initialize all of the list item UI components
        val imageType: ImageView? = view.findViewById(R.id.imageType)
        val textName: TextView? = view.findViewById(R.id.textName)
        val textUrl: TextView? = view.findViewById(R.id.textUrl)
        val imageButtonDownload: ImageButton? = view.findViewById(R.id.imageButtonDownload)
        val progressBarDownload: LinearProgressIndicator? =
            view.findViewById(R.id.progressBarDownload)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_pdf_vertical, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return sources.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = sources[position]
        // Set the image type
        if (currentItem.type == SourceType.PDF)
            holder.imageType?.setImageResource(R.drawable.ic_baseline_picture_as_pdf_24)
        else
            holder.imageType?.setImageResource(R.drawable.ic_baseline_video_24)

        // Set the text for the current text of the source
        holder.textName?.text = currentItem.name

        // Set the text for the current url of the source
        holder.textUrl?.text = currentItem.url

        // ButtonDownload & ProgressBarDownload are hidden by default
        holder.imageButtonDownload!!.visibility = View.GONE
        holder.progressBarDownload!!.visibility = View.GONE
    }

    fun setData(user: ArrayList<Source>) {
        this.sources = user
        notifyDataSetChanged()
    }
}