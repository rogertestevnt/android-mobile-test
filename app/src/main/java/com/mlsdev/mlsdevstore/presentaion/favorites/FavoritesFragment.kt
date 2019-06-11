package com.mlsdev.mlsdevstore.presentaion.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mlsdev.mlsdevstore.databinding.FragmentFavoritesBinding
import com.mlsdev.mlsdevstore.presentaion.fragment.BaseFragment

class FavoritesFragment : BaseFragment() {

    val viewModel: FavoritesViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(FavoritesViewModel::class.java) }
    lateinit var adapter: FavoriteProductsAdapter
    lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lifecycle.addObserver(viewModel)
        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycle.removeObserver(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initRecyclerView()
    }

    private fun initAdapter() {
        adapter = FavoriteProductsAdapter(
                { clickedItem ->
                    NavHostFragment
                            .findNavController(this)
                            .navigate(FavoritesFragmentDirections.actionFavoritesFragmentToProductFragment(clickedItem))
                },
                { favoredItem -> viewModel.addProductIntoFavorites(favoredItem) },
                { unfavoredItem -> viewModel.removeProductFromFavorites(unfavoredItem) })
    }

    private fun initRecyclerView() {
        binding.collectionProducts.adapter = adapter
        binding.collectionProducts.itemAnimator = DefaultItemAnimator()
        binding.collectionProducts.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        binding.collectionProducts.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()
        viewModel.favoriteProducts.observe(this, Observer { products ->
            adapter.setItems(products)
            binding.empty = products.isEmpty()
        })
    }

}