package ru.nelshin.telegram.ui.fragments.message_recycle_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.nelshin.telegram.R

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view) {
    val blockReceivedImageMessage: ConstraintLayout =
        view.findViewById(R.id.bloc_recived_image_message)
    val blocUserImageMessage: ConstraintLayout = view.findViewById(R.id.bloc_user_image_message)
    val chatUserImage: ImageView = view.findViewById(R.id.chat_user_image)
    val chatReceivedImage: ImageView = view.findViewById(R.id.chat_received_image)
    val chatUserImageMessageTime: TextView =
        view.findViewById(R.id.chat_user_image_message_time)
    val chatReceivedImageMessageTime: TextView =
        view.findViewById(R.id.chat_recived_image_message_time)
}