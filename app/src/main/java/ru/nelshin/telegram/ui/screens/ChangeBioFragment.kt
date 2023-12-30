package ru.nelshin.telegram.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.nelshin.telegram.R
import ru.nelshin.telegram.database.USER
import ru.nelshin.telegram.database.setBioToDatabase
import ru.nelshin.telegram.databinding.FragmentChangeBioBinding

class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {

    private lateinit var mBinding: FragmentChangeBioBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentChangeBioBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        mBinding.settingsInputBio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = mBinding.settingsInputBio.text.toString()
        setBioToDatabase(newBio)
    }

}