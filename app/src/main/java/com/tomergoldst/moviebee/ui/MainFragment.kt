package com.tomergoldst.moviebee.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomergoldst.moviebee.R
import com.tomergoldst.moviebee.extentions.dp2Px
import com.tomergoldst.moviebee.models.Movie
import com.tomergoldst.moviebee.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment(),
    MoviesPostersRecyclerListAdapter.OnAdapterInteractionListener {

    private val mModel by viewModel<MainViewModel>()

    private lateinit var mAdapter: MoviesPostersRecyclerListAdapter
    private var mListener: OnFragmentInteraction? = null

    companion object {
        fun newInstance() = MainFragment()
    }

    // Container Activity must implement this interface
    interface OnFragmentInteraction {
        fun onMovieClicked(movie: Movie, view: View)
        fun onShowFavoritesMoviesClicked()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAdapter = MoviesPostersRecyclerListAdapter(this)
        recyclerView.apply {
            // show different number of columns for different states
            val spanCount = if (UiUtils.isPortrait(context)) 3 else 5

            layoutManager = GridLayoutManager(context, spanCount)
            addItemDecoration(SimpleItemDecoration(context.dp2Px(4), context.dp2Px(8)))
            addOnScrollListener(object : EndlessRecyclerViewScrollListener(
                layoutManager as GridLayoutManager
            ) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    mModel.getMoreMovies()
                }
            })
            adapter = mAdapter
        }

        loadingViewGroup.visibility = View.VISIBLE

        favoriteMoviesBtn.setOnClickListener {
            mListener?.onShowFavoritesMoviesClicked()
        }

        mModel.movies.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                mAdapter.submitList(it)
            }
        })

        mModel.dataLoading.observe(viewLifecycleOwner, Observer {
            loadingViewGroup.visibility = if (it) View.VISIBLE else View.GONE
            recyclerView.visibility = if (it) View.GONE else View.VISIBLE
        })

        mModel.error.observe(viewLifecycleOwner, Observer {
            errorTxv.isVisible = it == true
        })
    }

    override fun onItemClicked(movie: Movie, view: View) {
        mListener!!.onMovieClicked(movie, view)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as? OnFragmentInteraction
        if (mListener == null) {
            throw ClassCastException("$context must implement OnFragmentInteraction")
        }

    }

}
