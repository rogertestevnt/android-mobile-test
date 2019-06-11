package com.mlsdev.mlsdevstore.presentaion.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.data.DataLoadState
import com.mlsdev.mlsdevstore.databinding.FragmentStoreBinding
import com.mlsdev.mlsdevstore.presentaion.fragment.BaseFragment
import com.mlsdev.mlsdevstore.presentaion.products.PagedProductsAdapter
import io.reactivex.disposables.Disposable

class StoreFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var adapter: PagedProductsAdapter
    lateinit var binding: FragmentStoreBinding
    private var collectionSubscription: Disposable? = null
    val viewModel: StoreViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(StoreViewModel::class.java) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_store, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.refreshLayout.setOnRefreshListener(this)
        initRecyclerView()
        initErrorHandler()
        initClickHandlers()
    }

    private fun initClickHandlers() {
        binding.buttonBrowseCategories.setOnClickListener {
            findNavController().navigate(StoreFragmentDirections.actionStoreFlowFragmentToCategoriesFragment())
        }
    }

    private fun initErrorHandler() {
        errorInViewHandler.observeNetworkError(this, viewModel.networkErrorLiveData)
        errorInViewHandler.observeCommonError(this, viewModel.commonErrorLiveData)
        errorInViewHandler.observeTechError(this, viewModel.technicalErrorLiveData)
        errorInViewHandler.observeAuthError(this, viewModel.authErrorLiveData)
    }

    private fun initRecyclerView() {
        adapter = PagedProductsAdapter(
                {},
                { findNavController(this).navigate(StoreFragmentDirections.actionStoreFlowFragmentToProductFragment(it)) })

        binding.rvProducts.adapter = adapter
        (binding.rvProducts.layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return when (viewType) {
                    R.layout.layout_network_state -> 2
                    R.layout.item_product -> 1
                    else -> -1
                }
            }
        }

        viewModel.loadingState.observe(this, Observer {
            adapter.setLoadState(it)

            when (it) {
                DataLoadState.LOADING -> {
                    binding.loadingIndicator.root.visibility =
                            if (!binding.refreshLayout.isRefreshing && adapter.itemCount == 0) View.VISIBLE
                            else View.GONE

                    binding.refreshLayout.isRefreshing = binding.refreshLayout.isRefreshing
                }
                else -> {
                    binding.refreshLayout.isRefreshing = false
                    binding.loadingIndicator.root.visibility = View.GONE
                }
            }

        })

    }

    override fun onRefresh() {
        viewModel.refresh()
    }

    override fun onStart() {
        super.onStart()
        collectionSubscription = viewModel.products.subscribe { pagedList ->
            adapter.submitList(pagedList)
        }
    }

    override fun onStop() {
        super.onStop()
        collectionSubscription?.dispose()
    }
}
