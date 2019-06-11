package com.mlsdev.mlsdevstore.presentaion.product

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.databinding.FragmentProductBinding
import com.mlsdev.mlsdevstore.presentaion.adapter.ProductGalleryAdapter
import com.mlsdev.mlsdevstore.presentaion.fragment.BaseFragment
import com.mlsdev.mlsdevstore.presentaion.utils.ExtrasKeys
import javax.inject.Inject


class ProductFragment : BaseFragment() {

    @Inject
    lateinit var galleryAdapter: ProductGalleryAdapter
    lateinit var binding: FragmentProductBinding
    val viewModel: ProductDetailsViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(ProductDetailsViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false)
        binding.viewModel = viewModel
        lifecycle.addObserver(viewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.productImagesGallery.adapter = galleryAdapter
        viewModel.setProductDetailsData(arguments)

        viewModel.imagesLiveData.observe(this, Observer { galleryAdapter.setImages(it) })
        viewModel.descriptionStateLiveData.observe(this, Observer {
            if (it) {
                val bottomSheet = DescriptionBottomSheetFragment()
                bottomSheet.arguments = bundleOf(Pair(ExtrasKeys.KEY_PRODUCT_DESCRIPTION, viewModel.description.get()))
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            }
        })

        viewModel.getInfoMessageLive().observe(this, Observer {
            Toast.makeText(context, getString(it), Toast.LENGTH_SHORT).show()
        })

        initErrorHandler()
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.messageLiveData.observe(this, Observer {
            errorInViewHandler.showAlertDialog(getString(it.title), getString(it.message))
        })
    }

    private fun initErrorHandler() {
        errorInViewHandler.observeNetworkError(this, viewModel.networkErrorLiveData)
        errorInViewHandler.observeCommonError(this, viewModel.commonErrorLiveData)
        errorInViewHandler.observeTechError(this, viewModel.technicalErrorLiveData)
        errorInViewHandler.observeAuthError(this, viewModel.authErrorLiveData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycle.removeObserver(viewModel)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        viewModel.getFavoriteLiveData().observe(this, Observer {
            menu.getItem(0).setIcon(if (it) R.drawable.ic_delete else R.drawable.ic_save)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_to_favorites -> {
                viewModel.onFavoriteButtonClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}