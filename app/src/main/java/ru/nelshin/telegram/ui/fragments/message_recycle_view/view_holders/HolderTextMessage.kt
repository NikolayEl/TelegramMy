package ru.nelshin.telegram.ui.fragments.message_recycle_view.view_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.nelshin.telegram.R


class HolderTextMessage(view: View) : RecyclerView.ViewHolder(view) {


    val blocUserMessage: ConstraintLayout = view.findViewById(R.id.bloc_user_message)
    val chatUserMessage: TextView = view.findViewById(R.id.chat_user_message)
    val chatUserMessageTime: TextView = view.findViewById(R.id.chat_user_message_time)

    val blockReceivedMessage: ConstraintLayout = view.findViewById(R.id.bloc_recived_message)
    val chatReceivedMessage: TextView = view.findViewById(R.id.chat_recived_message)
    val chatReceivedMessageTime: TextView = view.findViewById(R.id.chat_recived_message_time)
}