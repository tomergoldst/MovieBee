package com.tomergoldst.moviebee.ui

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
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
            //mListener?.onShowFavoritesMoviesClicked()
            findNavController().navigate(R.id.action_mainFragment_to_favoriteMoviesFragment2)
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

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onItemClicked(movie: Movie, view: View) {
        val bundle = bundleOf(
            MovieDetailsFragment.ARG_MOVIE_ID to movie.id,
            MovieDetailsFragment.ARG_TRANSITION_NAME_POSTER_IMAGE to ViewCompat.getTransitionName(view)
        )
        val extras = FragmentNavigatorExtras(
            view to ViewCompat.getTransitionName(view).toString()
        )
        findNavController().navigate(R.id.action_mainFragment_to_movieDetailsFragment2, bundle, null, extras)
    }

}
