package ru.nelshin.telegram.ui.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import ru.nelshin.telegram.R
import ru.nelshin.telegram.databinding.FragmentEnterCodeBinding
import ru.nelshin.telegram.utilits.APP_ACTIVITY
import ru.nelshin.telegram.database.AUTH
import ru.nelshin.telegram.utilits.AppTextWatcher
import ru.nelshin.telegram.database.CHILD_ID
import ru.nelshin.telegram.database.CHILD_PHONE
import ru.nelshin.telegram.database.CHILD_USERNAME
import ru.nelshin.telegram.database.NODE_PHONES
import ru.nelshin.telegram.database.NODE_USERS
import ru.nelshin.telegram.database.REF_DATABASE_ROOT
import ru.nelshin.telegram.utilits.restartActivity
import ru.nelshin.telegram.utilits.showToast


class EnterCodeFragment(val mPhoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {

    private lateinit var mBinding: FragmentEnterCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        APP_ACTIVITY.title = mPhoneNumber
        mBinding.registerInputCode.addTextChangedListener(AppTextWatcher {
            val string = mBinding.registerInputCode.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })

        return mBinding.root
    }


    private fun enterCode() {
        val code = mBinding.registerInputCode.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task1 ->
            if (task1.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = mPhoneNumber
                dateMap[CHILD_USERNAME] = uid

                REF_DATABASE_ROOT.child(NODE_PHONES).child(mPhoneNumber).setValue(uid)
                    .addOnFailureListener { showToast(it.message.toString()) }
                    .addOnSuccessListener {
                        REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                            .addOnSuccessListener {
                                showToast("Welcome")
                                restartActivity()
                            }
                            .addOnFailureListener{showToast(it.message.toString())}
                    }
            } else showToast(task1.exception?.message.toString())
        }

    }
}