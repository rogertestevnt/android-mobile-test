package com.mlsdev.mlsdevstore.presentaion.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.databinding.FragmentCheckoutBinding
import com.mlsdev.mlsdevstore.presentaion.fragment.BaseFragment

class CheckoutFragment : BaseFragment() {
    lateinit var binding: FragmentCheckoutBinding
    val viewModel: CheckoutViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(CheckoutViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lifecycle.addObserver(viewModel)
        binding = FragmentCheckoutBinding.inflate(inflater)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycle.removeObserver(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        errorInViewHandler.observeAuthError(this, viewModel.authErrorLiveData)
        errorInViewHandler.observeNetworkError(this, viewModel.networkErrorLiveData)
        errorInViewHandler.observeCommonError(this, viewModel.commonErrorLiveData)
        errorInViewHandler.observeTechError(this, viewModel.technicalErrorLiveData)
        viewModel.personalInfoErrorEvent.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.loadingStateLiveData.observe(this, Observer { binding.isLoading = it })
        viewModel.orderPostedEvent.observe(this, Observer {
            Toast.makeText(context, getString(R.string.message_order_sent), Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        })
    }
}