package com.mlsdev.mlsdevstore.presentaion.products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.data.DataLoadState
import com.mlsdev.mlsdevstore.databinding.FragmentProductsBinding
import com.mlsdev.mlsdevstore.presentaion.fragment.BaseFragment
import io.reactivex.disposables.Disposable

class ProductsFragment : BaseFragment() {

    val viewModel: ProductsViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(ProductsViewModel::class.java) }
    lateinit var binding: FragmentProductsBinding
    lateinit var adapter: PagedProductsAdapter
    private var collectionSubscription: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_products, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { viewModel.initCategoryData(ProductsFragmentArgs.fromBundle(it).toBundle()) }
        initCollectionView()
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initCollectionView() {
        adapter = PagedProductsAdapter(
                { viewModel.retry() },
                { product ->
                    Log.d("ON_PRODUCT_ITEM_CLICK", "Product id: ${product.id}; Product name: ${product.itemTitle}")
                    findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToProductFragment(product))
                })
        binding.rvProducts.adapter = adapter
        binding.refreshLayout.setOnRefreshListener { viewModel.refresh() }

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
                DataLoadState.LOADING -> binding.refreshLayout.isRefreshing = binding.refreshLayout.isRefreshing
                else -> binding.refreshLayout.isRefreshing = false
            }

        })

    }

    override fun onStart() {
        super.onStart()
        collectionSubscription = viewModel.products.subscribe { pagedList ->
            adapter.submitList(pagedList)
            binding.isEmpty = adapter.itemCount == 0
        }
    }

    override fun onStop() {
        super.onStop()
        collectionSubscription?.dispose()
    }
}