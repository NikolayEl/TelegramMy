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
import ru.nelshin.telegram.utilits.replaceActivity
import ru.nelshin.telegram.utilits.showToast


class EnterCodeFragment(val mPhoneNumber: String, val id: String) : Fragment(R.layout.fragment_enter_code) {

    private lateinit var mBinding: FragmentEnterCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        AUTH.signInWithCredential(credential).addOnCompleteListener{
            if (it.isSuccessful){
                showToast("Welcom to telegram")
                (activity as RegisterActivity).replaceActivity(MainActivity())
            } else {
                showToast(it.exception?.message.toString())
            }
        }

    }
}