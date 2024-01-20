package ru.nelshin.telegram.ui.screens.main_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import ru.nelshin.telegram.R
import ru.nelshin.telegram.models.CommonModel
import ru.nelshin.telegram.utilits.downloadAndSetImage

class MainListAdapter: RecyclerView.Adapter<MainListAdapter.MainListHolder> (){

    private var listItems = mutableListOf<CommonModel>()

    class MainListHolder(view: View): RecyclerView.ViewHolder(view){
        val itemName:TextView = view.findViewById(R.id.main_list_item_name)
        val itemLastMessage:TextView = view.findViewById(R.id.main_list_last_message)
        val itemPhoto:CircleImageView = view.findViewById(R.id.main_list_item_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)
        return MainListHolder(view)
    }

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: MainListHolder, position: Int) {
        holder.itemName.text = listItems[position].fullname
        holder.itemLastMessage.text = listItems[position].lastMessage
        holder.itemPhoto.downloadAndSetImage(listItems[position].photoUrl)
    }

    fun updateListItems(item: CommonModel){
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }
}