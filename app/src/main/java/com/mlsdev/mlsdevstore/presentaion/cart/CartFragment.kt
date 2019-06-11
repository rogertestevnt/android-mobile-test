package com.mlsdev.mlsdevstore.presentaion.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.databinding.FragmentCartBinding
import com.mlsdev.mlsdevstore.presentaion.fragment.BaseFragment

class CartFragment : BaseFragment() {
    lateinit var cartProductsAdapter: CartProductsAdapter
    lateinit var binding: FragmentCartBinding
    val viewModel: CartViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(CartViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartProductsAdapter = CartProductsAdapter { productId -> viewModel.removeItem(productId) }
        binding.viewModel = viewModel
        binding.itemsRecycler.adapter = cartProductsAdapter
        binding.buttonCheckout.setOnClickListener {
            findNavController().navigate(CartFragmentDirections.actionCartFlowFragmentToCheckoutFragment())
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.productsInCart.observe(this, Observer { pagedList ->
            cartProductsAdapter.submitList(pagedList)
            binding.viewModel?.cartIsEmpty?.set(pagedList.isEmpty())
        })
    }

    override fun onDestroy() {
        lifecycle.removeObserver(viewModel)
        super.onDestroy()
    }
}
