package com.mlsdev.mlsdevstore.presentaion.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<BottomNavigationView>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: BottomNavigationView, dependency: View): Boolean {
        return dependency is NestedScrollView
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: BottomNavigationView,
                                     directTargetChild: View,
                                     target: View,
                                     axes: Int,
                                     type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout,
                                child: BottomNavigationView,
                                target: View,
                                dxConsumed: Int,
                                dyConsumed: Int,
                                dxUnconsumed: Int,
                                dyUnconsumed: Int,
                                type: Int) {
        if (dyUnconsumed < 0) {
            showBottomNavigationView(child)
        } else if (dyUnconsumed > 0) {
            hideBottomNavigationView(child)
        }
    }

    private fun hideBottomNavigationView(view: BottomNavigationView) {
        view.animate().translationY(view.height.toFloat())
    }

    private fun showBottomNavigationView(view: BottomNavigationView) {
        view.animate().translationY(0f)
    }
}