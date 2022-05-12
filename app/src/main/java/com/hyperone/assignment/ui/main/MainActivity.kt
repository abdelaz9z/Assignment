package com.hyperone.assignment.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hyperone.assignment.R
import com.hyperone.assignment.adapter.ListAdapter
import com.hyperone.assignment.adapter.SourceAdapter
import com.hyperone.assignment.const.Layout.HORIZONTAL
import com.hyperone.assignment.const.Layout.VERTICAL
import com.hyperone.assignment.databinding.ActivityMainBinding
import com.hyperone.assignment.room.AppDatabase
import com.hyperone.assignment.room.Source
import kotlinx.coroutines.flow.collect

/**
 * MainActivity class is the main activity of the application.
 * It contains the recycler view and the adapter to display the sources.
 *
 * @author Abdelaziz Daoud
 * @since 11/05/2022
 *
 * @property binding ActivityMainBinding
 * @property mainViewModel MainViewModel
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private lateinit var sourceArrayList: ArrayList<Source>
    private lateinit var recyclerViewMySource: RecyclerView

    private lateinit var db: AppDatabase

    /**
     * onCreate method is called when the activity is created.
     *
     * @param savedInstanceState Bundle?
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name")
            .allowMainThreadQueries().build()

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
//        mSourceViewModel = ViewModelProvider(this).get(SourceViewModel::class.java)

        val recyclerViewMainVideo = binding.recyclerViewMainVideo
        val recyclerViewMainPdf = binding.recyclerViewMainPdf

        /**
         * Observe the data in the view model and set the adapter on the recycler view
         * Show data in recycler view for pdf
         */
        mainViewModel.getPdfList()
        lifecycleScope.launchWhenStarted {
            mainViewModel.pdfList.collect { pdfList ->
                // Set the adapter on the recycler view and set the layout manager
                initRecyclerView(
                    recyclerViewMainPdf,
                    RecyclerView.VERTICAL,
                    SourceAdapter(pdfList, VERTICAL)
                )
            }
        }

        /**
         * Observe the data in the view model and set the adapter on the recycler view
         * Show data in recycler view for videos
         */
        mainViewModel.getVideoList()
        lifecycleScope.launchWhenStarted {
            mainViewModel.videoList.collect { videoList ->
                // Set the adapter on the recycler view and set the layout manager
                initRecyclerView(
                    recyclerViewMainVideo,
                    RecyclerView.HORIZONTAL,
                    SourceAdapter(videoList, HORIZONTAL)
                )
            }
        }

        /**
         * ProgressBar (video, pdf) is the progress bar to display the loading state.
         */
        lifecycleScope.launchWhenStarted {
            mainViewModel.isLoading.collect { isLoading ->
                binding.progressBarVideo.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.progressBarPdf.visibility = if (isLoading) View.VISIBLE else View.GONE
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
    }

    //==============================================================================================
    // ● Save response items in local Database Room                                                                                              =
    //==============================================================================================

    /**
     * My source bottom sheet dialog fragment class (for video, pdf)
     */
    @SuppressLint("InflateParams")
    private fun mySourceBottomSheetDialog() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_my_source, null)

        val imageButtonMySourceDone =
            view.findViewById<ImageButton>(R.id.imageButtonMySourceDone)

        recyclerViewMySource = view.findViewById(R.id.recyclerViewMySource)
        recyclerViewMySource.layoutManager = LinearLayoutManager(this)
        recyclerViewMySource.setHasFixedSize(true)

        sourceArrayList = arrayListOf()

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

        dialog.setOnShowListener { it ->
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let {
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
                behaviour.isDraggable = true
            }
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    /**
     * Setup the full height of the bottom sheet
     *
     * @param bottomSheet View
     */
    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }
//==============================================================================================
}


