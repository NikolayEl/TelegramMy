package ru.nelshin.telegram.ui.screens

import androidx.fragment.app.Fragment
import ru.nelshin.telegram.R
import ru.nelshin.telegram.utilits.APP_ACTIVITY
import ru.nelshin.telegram.utilits.hideKeyboard

class MainFragment : Fragment(R.layout.fragment_chats) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Telegram"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
    }
}