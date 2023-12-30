package ru.nelshin.telegram.ui.message_recycle_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.nelshin.telegram.R
import ru.nelshin.telegram.database.CURRENT_UID
import ru.nelshin.telegram.ui.message_recycle_view.views.MessageView
import ru.nelshin.telegram.utilits.asTime
import ru.nelshin.telegram.utilits.downloadAndSetImage

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {
    val blockReceivedImageMessage: ConstraintLayout =
        view.findViewById(R.id.bloc_recived_image_message)
    val blocUserImageMessage: ConstraintLayout = view.findViewById(R.id.bloc_user_image_message)
    val chatUserImage: ImageView = view.findViewById(R.id.chat_user_image)
    val chatReceivedImage: ImageView = view.findViewById(R.id.chat_received_image)
    val chatUserImageMessageTime: TextView =
        view.findViewById(R.id.chat_user_image_message_time)
    val chatReceivedImageMessageTime: TextView =
        view.findViewById(R.id.chat_recived_image_message_time)

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockReceivedImageMessage.visibility = View.GONE
            blocUserImageMessage.visibility = View.VISIBLE
            chatUserImage.downloadAndSetImage(view.fileUrl)
            chatUserImageMessageTime.text =
                view.timeStamp.asTime()

        } else {
            blockReceivedImageMessage.visibility = View.VISIBLE
            blocUserImageMessage.visibility = View.GONE
            chatReceivedImage.downloadAndSetImage(view.fileUrl)
            chatReceivedImageMessageTime.text =
                view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {

    }

    override fun onDettach() {

    }
}