package ru.nelshin.telegram.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.nelshin.telegram.R
import ru.nelshin.telegram.databinding.FragmentChangeNameBinding
import ru.nelshin.telegram.utilits.APP_ACTIVITY
import ru.nelshin.telegram.utilits.CHILD_FULLNAME
import ru.nelshin.telegram.utilits.NODE_USERS
import ru.nelshin.telegram.utilits.REF_DATABASE_ROOT
import ru.nelshin.telegram.utilits.CURRENT_UID
import ru.nelshin.telegram.utilits.USER
import ru.nelshin.telegram.utilits.showToast

class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {
    private lateinit var mBinding: FragmentChangeNameBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentChangeNameBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        initFullnameList()
    }

    private fun initFullnameList() {
        val fullnameList = USER.fullname.split(' ')
        if (fullnameList.size > 1) {
            mBinding.settingsInputName.setText(fullnameList[0])
            mBinding.settingsInputSurname.setText(fullnameList[1])
        } else mBinding.settingsInputName.setText(fullnameList[0])
    }

    override fun change() {
        val name = mBinding.settingsInputName.text.toString()
        val surname = mBinding.settingsInputSurname.text.toString()
        if (name.isEmpty()){
            showToast(getString(R.string.settings_toast_name_is_empty))
        } else {
            val fullname = "$name $surname"
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FULLNAME)
                .setValue(fullname).addOnCompleteListener {
                    if(it.isSuccessful){
                        showToast(getString(R.string.toast_data_updated))
                        USER.fullname = fullname
                        this.parentFragmentManager.popBackStack()
                    }
                }
        }
    }
}