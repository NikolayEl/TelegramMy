package ru.nelshin.telegram.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import ru.nelshin.telegram.MainActivity
import ru.nelshin.telegram.R
import ru.nelshin.telegram.activities.RegisterActivity
import ru.nelshin.telegram.databinding.FragmentEnterPhoneNumberBinding
import ru.nelshin.telegram.utilits.AUTH
import ru.nelshin.telegram.utilits.replaceActivity
import ru.nelshin.telegram.utilits.replaceFragment
import ru.nelshin.telegram.utilits.showToast
import java.util.concurrent.TimeUnit

class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private lateinit var mPhoneNumber: String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

private lateinit var mBinding: FragmentEnterPhoneNumberBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEnterPhoneNumberBinding.inflate(inflater, container, false)
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener{
                    if (it.isSuccessful){
                        showToast("Welcom to telegram")
                        (activity as RegisterActivity).replaceActivity(MainActivity())
                    } else {
                        showToast(it.exception?.message.toString())
                    }
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mPhoneNumber, id))
            }
        }
        mBinding.registerBtnNext.setOnClickListener {
            sendCode()
        }
        return mBinding.root
    }

    private fun sendCode() {
        if (mBinding.registerInputPhoneNumber.text.toString().isEmpty()){
            showToast(getString(R.string.register_toast_enter_phone))
        } else {
            authUser()
        }
    }

    private fun authUser() {
        mPhoneNumber = mBinding.registerInputPhoneNumber.text.toString()
        PhoneAuthOptions.newBuilder(AUTH)
            .setPhoneNumber(mPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity as RegisterActivity)
            .setCallbacks(mCallback)
    }
}