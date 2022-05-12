package com.hyperone.assignment.ui.main

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hyperone.assignment.R
import com.hyperone.assignment.adapter.SourceAdapter
import com.hyperone.assignment.const.Layout.HORIZONTAL
import com.hyperone.assignment.const.Layout.VERTICAL
import com.hyperone.assignment.databinding.ActivityMainBinding

/**
 * MainActivity class is the main activity of the application.
 * It contains the recycler view and the adapter to display the sources.
 *
 * @property binding ActivityMainBinding
 * @property mainViewModel MainViewModel
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private lateinit var recyclerViewMySource: RecyclerView

    /**
     * onCreate method is called when the activity is created.
     *
     * @param savedInstanceState Bundle?
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
//        mSourceViewModel = ViewModelProvider(this).get(SourceViewModel::class.java)

        val recyclerViewMainVideo = binding.recyclerViewMainVideo
        val recyclerViewMainPdf = binding.recyclerViewMainPdf

        /**
         * Observe the data in the view model and set the adapter on the recycler view
         * Show data in recycler view for videos
         */
        mainViewModel.pdfList.observe(this) {
            it?.let {
                // Set the adapter on the recycler view and set the layout manager
                initRecyclerView(
                    recyclerViewMainPdf,
                    RecyclerView.VERTICAL,
                    SourceAdapter(it, VERTICAL)
                )
            }
        }

        /**
         * Observe the data in the view model and set the adapter on the recycler view
         * Show data in recycler view for pdfs
         */
        mainViewModel.videoList.observe(this) {
            it?.let {
                // Set the adapter on the recycler view and set the layout manager
                initRecyclerView(
                    recyclerViewMainVideo,
                    RecyclerView.HORIZONTAL,
                    SourceAdapter(it, HORIZONTAL)
                )
            }
        }

        /**
         * ProgressBar (video, pdf) is the progress bar to display the loading state.
         */
        mainViewModel.isLoading.observe(this) {
            it?.let {
                binding.progressBarVideo.visibility = if (it) View.VISIBLE else View.GONE
                binding.progressBarPdf.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        // My source bottom sheet dialog fragment class (for video, pdf)
        binding.imageButtonMainDownload.setOnClickListener { mySourceBottomSheetDialog() }
    }

    /**
     * Initialize the recycler view
     *
     * @param recyclerView RecyclerView
     * @param layoutManager Int
     * @param typeAdapter SourceAdapter
     */
    private fun initRecyclerView(
        recyclerView: RecyclerView,
        layoutManager: Int,
        typeAdapter: SourceAdapter
    ) {

        recyclerView.adapter = typeAdapter

        // Specify fixed size to improve performance
        recyclerView.setHasFixedSize(true)

        RecyclerView.HORIZONTAL

        val linearLayoutManager = LinearLayoutManager(this, layoutManager, false)
        recyclerView.layoutManager = linearLayoutManager

        typeAdapter.onItemClick = {
            val id = it.id.toString()
            val type = it.type.toString()
            val url = it.url.toString()
            val name = it.name.toString()

            // Insert data (video, pdf) to database
            insertDataToDatabase(type, url, name)
        }
    }

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
    private fun insertDataToDatabase(type: String, url: String, name: String) {
        if (inputCheck(type, url, name)) {
            // Create User Object

            // Add Data to Database


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
    private fun mySourceBottomSheetDialog() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_my_source, null)

        val imageButtonMySourceDone = view.findViewById<ImageButton>(R.id.imageButtonMySourceDone)

        recyclerViewMySource = view.findViewById(R.id.recyclerViewMySource)
        recyclerViewMySource.layoutManager = LinearLayoutManager(this)
        recyclerViewMySource.setHasFixedSize(true)

//        for (source in sources) {
//            Log.d("MainActivity", "id: ${source.name}")
//            // Recyclerview
//            val adapter = ListAdapter(source.type, source.url, source.name)
//            recyclerViewMySource.adapter = adapter
//            recyclerViewMySource.layoutManager = LinearLayoutManager(this)
//        }
//
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


