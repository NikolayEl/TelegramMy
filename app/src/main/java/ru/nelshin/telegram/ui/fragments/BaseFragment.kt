package ru.nelshin.telegram.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.nelshin.telegram.MainActivity

open class BaseFragment(val layout: Int) : Fragment(layout) {

    private lateinit var mRootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(layout, container, false)
        (activity as MainActivity).mAppDrawer.disableDrawer()
        return mRootView
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).mAppDrawer.enableDrawer()

    }
}