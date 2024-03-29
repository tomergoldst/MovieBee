package com.tomergoldst.moviebee.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController

open class BaseFragment : Fragment(),
    FragmentManager.OnBackStackChangedListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        // Listen for changes in the back stack
        fragmentManager!!.addOnBackStackChangedListener(this)
        // Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp()

    }

    override fun onBackStackChanged() {
        shouldDisplayHomeUp()
    }

    private fun shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        fragmentManager?.let {
            val canGoBack = fragmentManager!!.backStackEntryCount > 0
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(canGoBack)
        }
    }

    fun setToolbar(toolbar: Toolbar) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

}
