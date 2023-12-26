package ru.nelshin.telegram.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import ru.nelshin.telegram.MainActivity
import ru.nelshin.telegram.R
import ru.nelshin.telegram.activities.RegisterActivity
import ru.nelshin.telegram.databinding.FragmentEnterCodeBinding
import ru.nelshin.telegram.utilits.AUTH
import ru.nelshin.telegram.utilits.AppTextWatcher
import ru.nelshin.telegram.utilits.CHILD_ID
import ru.nelshin.telegram.utilits.CHILD_PHONE
import ru.nelshin.telegram.utilits.CHILD_USERNAME
import ru.nelshin.telegram.utilits.NODE_PHONES
import ru.nelshin.telegram.utilits.NODE_USERS
import ru.nelshin.telegram.utilits.REF_DATABASE_ROOT
import ru.nelshin.telegram.utilits.replaceActivity
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
        (activity as RegisterActivity).title = mPhoneNumber
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
                                (activity as RegisterActivity).replaceActivity(MainActivity())
                            }
                            .addOnFailureListener{showToast(it.message.toString())}
                    }
            } else showToast(task1.exception?.message.toString())
        }

    }
}