package ru.nelshin.telegram.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.nelshin.telegram.R
import ru.nelshin.telegram.databinding.FragmentEnterPhoneNumberBinding
import ru.nelshin.telegram.utilits.replaceFragment
import ru.nelshin.telegram.utilits.showToast

class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

private lateinit var mBinding: FragmentEnterPhoneNumberBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEnterPhoneNumberBinding.inflate(inflater, container, false)
        mBinding.registerBtnNext.setOnClickListener {
//            mBinding.registerInputPhoneNumber.hint = "reload"
            sendCode()
        }
        return mBinding.root
    }

    private fun sendCode() {
        if (mBinding.registerInputPhoneNumber.text.toString().isEmpty()){
            showToast(getString(R.string.register_toast_enter_phone))
        } else {
            replaceFragment(EnterCodeFragment())
        }
    }
}