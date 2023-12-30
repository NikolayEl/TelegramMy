package ru.nelshin.telegram.ui.screens

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import ru.nelshin.telegram.R
import ru.nelshin.telegram.databinding.FragmentSettingsBinding
import ru.nelshin.telegram.utilits.APP_ACTIVITY
import ru.nelshin.telegram.database.AUTH
import ru.nelshin.telegram.utilits.AppStates
import ru.nelshin.telegram.database.CURRENT_UID
import ru.nelshin.telegram.database.FOLDER_PROFILE_IMAGE
import ru.nelshin.telegram.database.REF_STORAGE_ROOT
import ru.nelshin.telegram.database.USER
import ru.nelshin.telegram.utilits.downloadAndSetImage
import ru.nelshin.telegram.database.getUrlFromStorage
import ru.nelshin.telegram.database.putFileToStorage
import ru.nelshin.telegram.database.putUrlToDatabase
import ru.nelshin.telegram.utilits.replaceFragment
import ru.nelshin.telegram.utilits.restartActivity
import ru.nelshin.telegram.utilits.showToast


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
        mBinding.settingsStatus.text = USER.state
        mBinding.settingsUsername.text = USER.username
        mBinding.settingsBtnChangeUsername.setOnClickListener {
            replaceFragment(ChangeUserNameFragment())
        }
        mBinding.settingsBtnChangeBio.setOnClickListener {
            replaceFragment(ChangeBioFragment())
        }
        mBinding.settingsChangePhoto.setOnClickListener {
            changePhotoUser()
        }
        if(USER.photoUrl.isNotEmpty())mBinding.settingsUserPhoto.downloadAndSetImage(USER.photoUrl)
    }

    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250, 250)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AppStates.updateState(AppStates.OFFLINE)
                AUTH.signOut()
                restartActivity()
            }

            R.id.settings_menu_change_name -> replaceFragment(ChangeNameFragment())
        }
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)

            putFileToStorage(uri, path){
                getUrlFromStorage(path){
                    putUrlToDatabase(it){
                        mBinding.settingsUserPhoto.downloadAndSetImage(it)
                        showToast(getString(R.string.toast_data_updated))
                        USER.photoUrl = it
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                    }
                }
            }
        }
    }

}