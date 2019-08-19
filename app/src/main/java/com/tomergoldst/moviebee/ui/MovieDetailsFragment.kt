package com.tomergoldst.moviebee.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.transition.Explode
import com.tomergoldst.moviebee.app.GlideApp
import com.tomergoldst.moviebee.data.remote.Constants
import com.tomergoldst.moviebee.models.Movie
import kotlinx.android.synthetic.main.fragment_movie_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import androidx.transition.TransitionInflater
import com.tomergoldst.moviebee.R


class MovieDetailsFragment : BaseFragment() {

    private val mModel by viewModel<MovieDetailsViewModel>()

    private var movieId: Long = -1

    companion object {
        const val ARG_MOVIE_ID = "ARG_MOVIE_ID"
        const val ARG_TRANSITION_NAME_POSTER_IMAGE = "ARG_TRANSITION_NAME_POSTER_IMAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        enterTransition = Explode()

        movieId = arguments!!.getLong(ARG_MOVIE_ID)
        mModel.init(movieId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_details, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setToolbar(toolbar)

        posterImv.transitionName = arguments!!.getString(ARG_TRANSITION_NAME_POSTER_IMAGE)

        favoriteImb.setOnClickListener {
            mModel.toggleFavoriteMovie()
        }

        mModel.movie.observe(viewLifecycleOwner, Observer { movie ->
            movie?.let { bindMovie(movie) }
        })

        mModel.isFavorite.observe(viewLifecycleOwner, Observer {
            bindFavorite(it)
        })

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

    }

    private fun bindMovie(movie: Movie) {
        movie.apply {
            title.let { titleTxv.text = it }
            overview.let { overviewTxv.text = it }

            if (!posterPath.isNullOrEmpty()) {
                loadImage(context!!, posterImv, posterPath!!)
            } else {
                posterImv.setImageResource(R.drawable.img_empty_img_holder_portrait)
            }

            if (!backdropPath.isNullOrEmpty()) {
                loadImage(context!!, backdropImv, backdropPath!!)
            } else {
                backdropImv.setImageResource(R.drawable.img_empty_img_holder_landscape)
            }

            voteAverage.let {
                avgRatingTxv.text = getString(R.string.average_rating_out_of, it.toString(), "10")
            }

            releaseDate.let {
                val calendar = Calendar.getInstance()
                calendar.time = it
                val year = calendar.get(Calendar.YEAR)
                releaseYearTxv.text = year.toString()
            }
        }
    }

    private fun bindFavorite(it: Boolean) {
        ImageViewCompat.setImageTintList(
            favoriteImb,
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context!!,
                    if (it) R.color.colorFavorite else R.color.colorIconOnSurface
                )
            )
        )
    }

    private fun loadImage(context: Context, imageView: ImageView, posterPath: String) {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.apply {
            strokeWidth = 5f
            centerRadius = 60f
            setColorFilter(
                ContextCompat.getColor(context, R.color.colorAccent),
                PorterDuff.Mode.SRC_IN
            )
            start()
        }

        val fullPath = String.format(Constants.POSTER_PATH, Constants.POSTER_WIDTH, posterPath)

        GlideApp.with(context)
            .load(fullPath)
            .placeholder(circularProgressDrawable)
            .error(R.drawable.img_empty_img_holder_landscape)
            .into(imageView)
    }

}
