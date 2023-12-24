package ru.nelshin.telegram

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ru.nelshin.telegram.activities.RegisterActivity
import ru.nelshin.telegram.databinding.ActivityMainBinding
import ru.nelshin.telegram.models.User
import ru.nelshin.telegram.ui.fragments.ChatsFragment
import ru.nelshin.telegram.ui.objects.AppDrawer
import ru.nelshin.telegram.utilits.AUTH
import ru.nelshin.telegram.utilits.AppValueEventListener
import ru.nelshin.telegram.utilits.NODE_USERS
import ru.nelshin.telegram.utilits.REF_DATABASE_ROOT
import ru.nelshin.telegram.utilits.UID
import ru.nelshin.telegram.utilits.USER
import ru.nelshin.telegram.utilits.initFarebase
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
    }

    override fun onStart() {
        super.onStart()
        initFields()
        initFunc()
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
        initFarebase()
        initUser()

    }

    private fun initUser() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener{

                USER = it.getValue(User::class.java) ?:User()
            })
    }
}



