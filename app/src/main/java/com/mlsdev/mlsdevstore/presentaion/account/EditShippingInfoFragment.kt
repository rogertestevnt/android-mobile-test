package com.mlsdev.mlsdevstore.presentaion.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.mlsdev.mlsdevstore.databinding.FragmentEditShippingInfoBinding
import com.mlsdev.mlsdevstore.presentaion.fragment.BaseFragment

class EditShippingInfoFragment : BaseFragment() {
    val viewModel: EditAccountViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(EditAccountViewModel::class.java)
    }

    lateinit var binding: FragmentEditShippingInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lifecycle.addObserver(viewModel)
        binding = FragmentEditShippingInfoBinding.inflate(inflater)
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
        viewModel.profileDataUpdated.observe(this, Observer {
            findNavController().navigateUp()
        })
    }

}