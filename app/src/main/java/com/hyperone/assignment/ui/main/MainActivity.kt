package com.hyperone.assignment.ui.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val recyclerViewMainVideo = binding.recyclerViewMainVideo
        val recyclerViewMainPdf = binding.recyclerViewMainPdf

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
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission from popup was granted, perform download
//                    downloadFile(mUrl, mTitle, mDescription)
                } else {
                    // Permission from popup was denied, show error message
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
            Log.d("MainActivity", "id: $id")
        }

        typeAdapter.onButtonClick = {
            val id = it.id.toString()
            val type = it.type.toString()
            val url = it.url.toString()
            val name = it.name.toString()

            Log.d("MainActivity", "id: $id")
            Log.d("MainActivity", "type: $type")
            Log.d("MainActivity", "url: $url")
            Log.d("MainActivity", "name: $name")
        }
    }

    companion object {
        const val PERMISSION_CODE = 1000
    }
}