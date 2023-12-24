package ru.nelshin.telegram.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import ru.nelshin.telegram.MainActivity
import ru.nelshin.telegram.R
import ru.nelshin.telegram.databinding.FragmentChangeUserNameBinding
import ru.nelshin.telegram.utilits.AppValueEventListener
import ru.nelshin.telegram.utilits.CHILD_USERNAME
import ru.nelshin.telegram.utilits.NODE_USERNAMES
import ru.nelshin.telegram.utilits.NODE_USERS
import ru.nelshin.telegram.utilits.REF_DATABASE_ROOT
import ru.nelshin.telegram.utilits.UID
import ru.nelshin.telegram.utilits.USER
import ru.nelshin.telegram.utilits.showToast
import java.util.Locale

class ChangeUserNameFragment : BaseFragment(R.layout.fragment_change_user_name) {

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
        setHasOptionsMenu(true)
        mBinding.settingsInputUsername.setText(USER.username)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> change()
        }
        return true
    }

    private fun change() {
        mNewUsername = mBinding.settingsInputUsername.text.toString().lowercase()
        if (mNewUsername.isEmpty()){
            showToast("Field is empty")
        } else {
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener{
                    if(it.hasChild(mNewUsername)){
                        showToast("This user already exists")
                    } else {
                        changeUserName()
                    }
                })
        }
    }

    private fun changeUserName() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername).setValue(UID)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    updateCurrentUsername()
                }
            }
    }

    private fun updateCurrentUsername() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_USERNAME).setValue(mNewUsername)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    deleteOldUsername()
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    private fun deleteOldUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username).removeValue()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    showToast(getString(R.string.toast_data_updated))
                    this.parentFragmentManager.popBackStack()
                    USER.username = mNewUsername
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }
}