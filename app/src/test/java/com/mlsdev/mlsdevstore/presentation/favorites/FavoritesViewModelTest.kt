package com.mlsdev.mlsdevstore.presentation.favorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mlsdev.mlsdevstore.presentaion.favorites.FavoritesViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavoritesViewModelTest {

    @Mock
    lateinit var repository: FavoriteProductsRepository

    lateinit var viewModel: FavoritesViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun beforeTest() {
        MockitoAnnotations.initMocks(this)
        viewModel = FavoritesViewModel(repository)
    }

    @Test
    fun observeFavoriteProducts() {

    }

}