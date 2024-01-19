package ru.nelshin.telegram.ui.message_recycle_view.view_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.nelshin.telegram.R
import ru.nelshin.telegram.database.CURRENT_UID
import ru.nelshin.telegram.ui.message_recycle_view.views.MessageView
import ru.nelshin.telegram.utilits.asTime


class HolderTextMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {


    private val blocUserMessage: ConstraintLayout = view.findViewById(R.id.bloc_user_message)
    private val chatUserMessage: TextView = view.findViewById(R.id.chat_user_message)
    private val chatUserMessageTime: TextView = view.findViewById(R.id.chat_user_message_time)

    private val blockReceivedMessage: ConstraintLayout =
        view.findViewById(R.id.bloc_recived_message)
    private val chatReceivedMessage: TextView = view.findViewById(R.id.chat_recived_message)
    private val chatReceivedMessageTime: TextView =
        view.findViewById(R.id.chat_recived_message_time)

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blocUserMessage.visibility = View.VISIBLE
            blockReceivedMessage.visibility = View.GONE
            chatUserMessage.text = view.text
            chatUserMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blocUserMessage.visibility = View.GONE
            blockReceivedMessage.visibility = View.VISIBLE
            chatReceivedMessage.text = view.text
            chatReceivedMessageTime.text =
                view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {

    }

    override fun onDetach() {

    }
}