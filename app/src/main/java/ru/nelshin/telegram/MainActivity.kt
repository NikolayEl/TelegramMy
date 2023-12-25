package ru.nelshin.telegram

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ru.nelshin.telegram.activities.RegisterActivity
import ru.nelshin.telegram.databinding.ActivityMainBinding
import ru.nelshin.telegram.ui.fragments.ChatsFragment
import ru.nelshin.telegram.ui.objects.AppDrawer
import ru.nelshin.telegram.utilits.APP_ACTIVITY
import ru.nelshin.telegram.utilits.AUTH
import ru.nelshin.telegram.utilits.AppStates
import ru.nelshin.telegram.utilits.initFarebase
import ru.nelshin.telegram.utilits.initUser
import ru.nelshin.telegram.utilits.replaceActivity
import ru.nelshin.telegram.utilits.replaceFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAppDrawer: AppDrawer
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFarebase()
        initUser{
            initFields()
            initFunc()
        }

    }
    private fun initFunc() {
        if (AUTH.currentUser != null) {
            setSupportActionBar(mToolbar)
            mAppDrawer.create()
            replaceFragment(ChatsFragment(), false)
        } else {
            replaceActivity(RegisterActivity())
        }


    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }
}



