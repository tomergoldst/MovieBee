package com.tomergoldst.moviebee.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomergoldst.moviebee.R
import com.tomergoldst.moviebee.extentions.dp2Px
import com.tomergoldst.moviebee.models.Movie
import com.tomergoldst.moviebee.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_favorite_movies.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteMoviesFragment : BaseFragment(),
    MoviesPostersRecyclerListAdapter.OnAdapterInteractionListener {

    private val mModel by viewModel<FavoriteMoviesViewModel>()

    private lateinit var mAdapter: MoviesPostersRecyclerListAdapter
    private var mListener: OnFragmentInteraction? = null

    companion object {
        fun newInstance() = FavoriteMoviesFragment()
    }

    // Container Activity must implement this interface
    interface OnFragmentInteraction {
        fun onMovieClicked(movie: Movie, view: View)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setToolbar(toolbar)

        bindRecyclerView()

        loadingViewGroup.isVisible = true

        mModel.movies.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                recyclerView.isVisible = true
                emptyTxv.isVisible = false
                mAdapter.submitList(it)
            } else {
                recyclerView.isVisible = false
                emptyTxv.isVisible = true
            }
        })

        mModel.dataLoading.observe(viewLifecycleOwner, Observer {
            loadingViewGroup.isVisible = it
        })

        mModel.error.observe(viewLifecycleOwner, Observer {
            if (it) {
                errorTxv.isVisible = it == true
                recyclerView.isVisible = false
                emptyTxv.isVisible = false
            }
        })
    }

    private fun bindRecyclerView() {
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
