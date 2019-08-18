package com.tomergoldst.moviebee.ui

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.tomergoldst.moviebee.R
import com.tomergoldst.moviebee.app.GlideApp
import com.tomergoldst.moviebee.data.remote.Constants
import com.tomergoldst.moviebee.models.Movie

class MoviesPostersRecyclerListAdapter(
    val mListener: OnAdapterInteractionListener
) :
    ListAdapter<Movie, MovieViewHolder>(DiffCallback()) {

    interface OnAdapterInteractionListener {
        fun onItemClicked(movie: Movie, view: View)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        return MovieViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(holder.adapterPosition)
        holder.bind(item, object : MovieViewHolder.OnViewHolderInteractionListener{
            override fun onItemClicked(movie: Movie, view: View) {
                mListener.onItemClicked(movie, view)
            }
        })
    }

    companion object{
        class DiffCallback : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return (oldItem.id == newItem.id)
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

}
