package ru.nelshin.telegram.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.nelshin.telegram.R
import ru.nelshin.telegram.database.CURRENT_UID
import ru.nelshin.telegram.database.NODE_USERNAMES
import ru.nelshin.telegram.database.REF_DATABASE_ROOT
import ru.nelshin.telegram.database.USER
import ru.nelshin.telegram.database.updateCurrentUsername
import ru.nelshin.telegram.databinding.FragmentChangeUserNameBinding
import ru.nelshin.telegram.utilits.AppValueEventListener
import ru.nelshin.telegram.utilits.showToast

class ChangeUserNameFragment : BaseChangeFragment(R.layout.fragment_change_user_name) {

    private lateinit var mBinding: FragmentChangeUserNameBinding

    lateinit var mNewUsername: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentChangeUserNameBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        mBinding.settingsInputUsername.setText(USER.username)
    }

    override fun change() {
        mNewUsername = mBinding.settingsInputUsername.text.toString().lowercase()
        if (mNewUsername.isEmpty()) {
            showToast("Field is empty")
        } else {
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.hasChild(mNewUsername)) {
                        showToast("This user already exists")
                    } else {
                        changeUserName()
                    }
                })
        }
    }

    private fun changeUserName() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername).setValue(CURRENT_UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUsername(mNewUsername)
                }
            }
    }


}