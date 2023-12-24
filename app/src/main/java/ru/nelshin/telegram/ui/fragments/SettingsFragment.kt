package ru.nelshin.telegram.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import ru.nelshin.telegram.MainActivity
import ru.nelshin.telegram.R
import ru.nelshin.telegram.activities.RegisterActivity
import ru.nelshin.telegram.databinding.FragmentSettingsBinding
import ru.nelshin.telegram.utilits.AUTH
import ru.nelshin.telegram.utilits.USER
import ru.nelshin.telegram.utilits.replaceActivity
import ru.nelshin.telegram.utilits.replaceFragment


class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private lateinit var mBinding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        mBinding.settingsBio.text = USER.bio
        mBinding.settingsFullName.text = USER.fullname
        mBinding.settingsPhoneNumber.text = USER.phone
        mBinding.settingsStatus.text = USER.status
        mBinding.settingsUsername.text = USER.username
        mBinding.settingsBtnChangeUsername.setOnClickListener {
            replaceFragment(ChangeUserNameFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_menu_exit -> {
                AUTH.signOut()
                (activity as MainActivity).replaceActivity(RegisterActivity())
            }
            R.id.settings_menu_change_name -> replaceFragment(ChangeNameFragment())
        }
        return true
    }
}