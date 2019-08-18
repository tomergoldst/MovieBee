package com.tomergoldst.moviebee.ui

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.tomergoldst.moviebee.R
import com.tomergoldst.moviebee.app.GlideApp
import com.tomergoldst.moviebee.data.remote.Constants
import com.tomergoldst.moviebee.models.Movie

class MovieViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val moviePosterImv: AppCompatImageView = v.findViewById(R.id.moviePosterImv)
    private val moviePosterTitleTxv: AppCompatTextView = v.findViewById(R.id.moviePosterTitleTxv)

    interface OnViewHolderInteractionListener {
        fun onItemClicked(movie: Movie, view: View)
    }

    fun bind(movie: Movie, listener: OnViewHolderInteractionListener) {
        val context = itemView.context

        moviePosterTitleTxv.text = movie.title

        itemView.setOnClickListener {
            listener.onItemClicked(movie, moviePosterImv)
        }

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

        movie.posterPath?.let {
            val fullPosterPath = String.format(Constants.POSTER_PATH, Constants.POSTER_WIDTH, movie.posterPath)

            GlideApp.with(context)
                .load(fullPosterPath)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.img_empty_img_holder_portrait)
                .into(moviePosterImv)

        } ?: moviePosterImv.setImageResource(R.drawable.img_empty_img_holder_portrait)

        ViewCompat.setTransitionName(moviePosterImv, "image_$adapterPosition")

    }

    companion object {
        fun from(parent: ViewGroup): MovieViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_movie_poster, parent, false)
            return MovieViewHolder(view)
        }
    }

}