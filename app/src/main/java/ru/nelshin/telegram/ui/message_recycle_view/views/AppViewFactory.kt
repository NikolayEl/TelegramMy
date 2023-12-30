package ru.nelshin.telegram.ui.message_recycle_view.views

import ru.nelshin.telegram.models.CommonModel
import ru.nelshin.telegram.utilits.TYPE_MESSAGE_IMAGE
import ru.nelshin.telegram.utilits.TYPE_MESSAGE_VOICE

class AppViewFactory {
    companion object{
        fun getView(message: CommonModel): MessageView {
            return when(message.type){
                TYPE_MESSAGE_IMAGE -> ViewImageMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl
                )
                TYPE_MESSAGE_VOICE -> ViewVoiceMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl
                )
                else -> ViewTextMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl,
                    message.text
                )
            }
        }
    }
}

