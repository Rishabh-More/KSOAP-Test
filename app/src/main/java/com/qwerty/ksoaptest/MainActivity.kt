package com.qwerty.ksoaptest

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.qwerty.ksoaptest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<List<String>> {

    private lateinit var binding: ActivityMainBinding
    private val LOADER_ID = 132

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)

        binding.recyclerView.adapter = CitiesAdapter(mutableListOf())

        binding.searchButton.setOnClickListener {

            // Initializes/restarts the Loader to begin a background thread for networking purposes.
            LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this)

            binding.emptyStateTextview.visibility = View.INVISIBLE
            binding.recyclerView.visibility = View.INVISIBLE

            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<String>> {
        return object : AsyncTaskLoader<List<String>>(this){
            override fun onStartLoading() {
                super.onStartLoading()
                forceLoad()
            }

            override fun loadInBackground(): List<String>? {
                if (binding.inputQuery.text.toString().isEmpty()) {
                    return null
                }

                // Returns XML response data via the following Utils object method.
                return QueryUtils.fetchCitiesData(binding.inputQuery.text.toString())
            }
        }
    }

    override fun onLoadFinished(loader: Loader<List<String>>, data: List<String>?) {
        binding.progressBar.visibility = View.GONE

        // Displays the RecyclerView of data. Otherwise, displays an empty state TextView instead.
        if (data != null) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.recyclerView.adapter = CitiesAdapter(data)
        } else {
            binding.emptyStateTextview.visibility = View.VISIBLE
        }
    }

    override fun onLoaderReset(loader: Loader<List<String>>) {
        binding.recyclerView.adapter = CitiesAdapter(mutableListOf())
    }
}