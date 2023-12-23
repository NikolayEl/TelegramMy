package ru.nelshin.telegram.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.nelshin.telegram.R
import ru.nelshin.telegram.databinding.FragmentEnterCodeBinding
import ru.nelshin.telegram.utilits.AppTextWatcher
import ru.nelshin.telegram.utilits.showToast


class EnterCodeFragment : Fragment(R.layout.fragment_enter_code) {

    private lateinit var mBinding: FragmentEnterCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        mBinding.registerInputCode.addTextChangedListener(AppTextWatcher {
            val string = mBinding.registerInputCode.text.toString()
            if (string.length == 6) {
                verifiCode()
            }
        })

        return mBinding.root
    }


    fun verifiCode() {
        showToast("OK")
    }
}