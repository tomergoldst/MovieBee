package com.tomergoldst.moviebee.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tomergoldst.moviebee.models.Movie
import android.os.Build
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.transition.Explode
import com.tomergoldst.moviebee.R

class MainActivity : AppCompatActivity(),
    MainFragment.OnFragmentInteraction,
    FavoriteMoviesFragment.OnFragmentInteraction {

    private lateinit var mCurrentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            showMainFragment()
        } else {
            mCurrentFragment = supportFragmentManager.getFragment(savedInstanceState, SAVED_STATE_CURRENT_FRAGMENT)!!
        }

    }

    private fun showMainFragment() {
        val f = MainFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, f, "MainFragment")
            .commit()

        mCurrentFragment = f
    }

    private fun showMovieDetailsFragment(movie: Movie, view: View) {
        val f = MovieDetailsFragment.newInstance(movie.id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            f.sharedElementEnterTransition = MovieDetailsTransition()
            //f.enterTransition = Explode()
            //mCurrentFragment.exitTransition = Explode()
            f.sharedElementReturnTransition = MovieDetailsTransition()
        }

        supportFragmentManager
            .beginTransaction()
            .addSharedElement(view, getString(R.string.transition_poster))
            .replace(R.id.container, f, "MovieDetailsFragment")
            .addToBackStack(null)
            .commit()

        mCurrentFragment = f
    }

    private fun showFavoritesMoviesFragment() {
        val f = FavoriteMoviesFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, f, "FavoritesMoviesFragment")
            .addToBackStack(null)
            .commit()

        mCurrentFragment = f
    }

    override fun onMovieClicked(movie: Movie, view: View) {
        showMovieDetailsFragment(movie, view)
    }

    override fun onShowFavoritesMoviesClicked() {
        showFavoritesMoviesFragment()
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //Save the fragment's instance
        supportFragmentManager.putFragment(outState, SAVED_STATE_CURRENT_FRAGMENT, mCurrentFragment)
    }

    companion object {
        const val SAVED_STATE_CURRENT_FRAGMENT = "SAVED_STATE_CURRENT_FRAGMENT"
    }

}
