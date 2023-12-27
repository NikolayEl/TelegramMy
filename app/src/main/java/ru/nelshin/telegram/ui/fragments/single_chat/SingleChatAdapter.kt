package ru.nelshin.telegram.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.nelshin.telegram.R
import ru.nelshin.telegram.models.CommonModel
import ru.nelshin.telegram.database.CURRENT_UID
import ru.nelshin.telegram.utilits.asTime

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {


    private var mListMessagesCache = emptyList<CommonModel>()

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blocUserMessage: ConstraintLayout = view.findViewById(R.id.bloc_user_message)
        val chatUserMessage: TextView = view.findViewById(R.id.chat_user_message)
        val chatUserMessageTime: TextView = view.findViewById(R.id.chat_user_message_time)

        val blockReceivedMessage: ConstraintLayout = view.findViewById(R.id.bloc_recived_message)
        val chatReceivedMessage: TextView = view.findViewById(R.id.chat_recived_message)
        val chatReceivedMessageTime: TextView = view.findViewById(R.id.chat_recived_message_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return SingleChatHolder(view)
    }

    override fun getItemCount(): Int = mListMessagesCache.size

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        if (mListMessagesCache[position].from == CURRENT_UID) {
            holder.blocUserMessage.visibility = View.VISIBLE
            holder.chatReceivedMessage.visibility = View.GONE
            holder.chatUserMessage.text = mListMessagesCache[position].text
            holder.chatUserMessageTime.text =
                mListMessagesCache[position].timeStamp.toString().asTime()
        } else {
            holder.blocUserMessage.visibility = View.GONE
            holder.chatReceivedMessage.visibility = View.VISIBLE
            holder.chatReceivedMessage.text = mListMessagesCache[position].text
            holder.chatReceivedMessageTime.text =
                mListMessagesCache[position].timeStamp.toString().asTime()
        }

    }

    fun setList(list: List<CommonModel>) {
        mListMessagesCache = list
        notifyDataSetChanged()
    }
}

