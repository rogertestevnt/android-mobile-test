package com.mlsdev.mlsdevstore.presentaion.adapter

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.data.DataLoadState

abstract class BasePagedAdapter<T>(
        protected val retryCallback: () -> Unit,
        diffCallback: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    protected var loadDataState: DataLoadState? = null

    open inner class NetworkStateViewHolder(itemView: View, retryCallback: () -> Unit) : RecyclerView.ViewHolder(itemView) {

        private val progressBar: ProgressBar = itemView.findViewById(R.id.loading_progress_bar)
        private val buttonRetry: Button = itemView.findViewById(R.id.button_retry_loading)

        init {
            buttonRetry.setOnClickListener { retryCallback() }
        }

        fun bindTo() {
            //loading and retry
            buttonRetry.visibility = if (loadDataState == DataLoadState.FAILED || loadDataState == DataLoadState.NOT_FOUND) View.VISIBLE else View.GONE
            progressBar.visibility = if (loadDataState == DataLoadState.LOADING) View.VISIBLE else View.GONE
        }

        fun clear() {
            buttonRetry.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    protected fun hasExtraRow(): Boolean {
        return loadDataState != null && loadDataState != DataLoadState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    /**
     * Set the current network state to the adapter
     * but this work only after the initial load
     * and the adapter already have list to add new loading raw to it
     * so the initial loading state the activity responsible for handle it
     *
     * @param loadState the new network state
     */
    fun setLoadState(loadState: DataLoadState?) {
        val lastItem = itemCount - 1
        currentList?.let {
            val previousState = this.loadDataState
            val hadExtraRow = hasExtraRow()
            this.loadDataState = loadState
            val hasExtraRow = hasExtraRow()
            if (hadExtraRow != hasExtraRow) {
                if (hadExtraRow) {
                    notifyItemRemoved(lastItem)
                } else {
                    notifyItemInserted(lastItem)
                }
            } else if (hasExtraRow && previousState !== loadState) {
                notifyItemChanged(itemCount - 1)
            }
        }
    }

}