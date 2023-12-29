package ru.nelshin.telegram.ui.fragments.message_recycle_view.view_holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nelshin.telegram.R
import ru.nelshin.telegram.ui.fragments.message_recycle_view.views.MessageView

class AppHolderFactory {
    companion object{
        fun getHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder{
            return when(viewType){
                MessageView.MESSAGE_IMAGE -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.message_item_image, parent, false)
                    HolderImageMessage(view)
                }
                else ->{
                        val view = LayoutInflater.from(parent.context)
                            .inflate(R.layout.message_item_text, parent, false)
                        HolderTextMessage(view)
                }
            }
        }
    }
}