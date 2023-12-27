package ru.nelshin.telegram.ui.fragments

import androidx.fragment.app.Fragment
import ru.nelshin.telegram.R
import ru.nelshin.telegram.utilits.APP_ACTIVITY

class ChatsFragment : Fragment(R.layout.fragment_chats) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Chats"

    }
}