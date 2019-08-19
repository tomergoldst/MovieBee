package com.tomergoldst.moviebee.ui

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun bindRecyclerView() {
        mAdapter = MoviesPostersRecyclerListAdapter(this)

        recyclerView.apply {
            // show different number of columns for different states
            val spanCount = if (UiUtils.isPortrait(context)) 3 else 5
            layoutManager = GridLayoutManager(context, spanCount)
            addItemDecoration(SimpleItemDecoration(context.dp2Px(4), context.dp2Px(8)))
            adapter = mAdapter
        }
    }

    override fun onItemClicked(movie: Movie, view: View) {
        val bundle = bundleOf(
            MovieDetailsFragment.ARG_MOVIE_ID to movie.id,
            MovieDetailsFragment.ARG_TRANSITION_NAME_POSTER_IMAGE to ViewCompat.getTransitionName(view)
        )
        val extras = FragmentNavigatorExtras(
            view to ViewCompat.getTransitionName(view).toString()
        )
        findNavController().navigate(R.id.movieDetailsFragment, bundle, null, extras)
    }

}
