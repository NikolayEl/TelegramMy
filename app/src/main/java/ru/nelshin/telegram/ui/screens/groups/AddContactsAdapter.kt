package ru.nelshin.telegram.ui.screens.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import ru.nelshin.telegram.R
import ru.nelshin.telegram.models.CommonModel
import ru.nelshin.telegram.utilits.downloadAndSetImage

class AddContactsAdapter: RecyclerView.Adapter<AddContactsAdapter.AddContactsHolder> (){

    private var listItems = mutableListOf<CommonModel>()

    class AddContactsHolder(view: View): RecyclerView.ViewHolder(view){
        val itemName:TextView = view.findViewById(R.id.add_contacts_item_name)
        val itemLastMessage:TextView = view.findViewById(R.id.add_contacts_last_message)
        val itemPhoto:CircleImageView = view.findViewById(R.id.add_contacts_item_photo)
        val itemChoice:CircleImageView = view.findViewById(R.id.add_contacts_item_choice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddContactsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_contacts_item, parent, false)

        val holder = AddContactsHolder(view)
        holder.itemView.setOnClickListener {
            if(listItems[holder.absoluteAdapterPosition].choice){
                holder.itemChoice.visibility = View.INVISIBLE
                listItems[holder.absoluteAdapterPosition].choice = false
                AddContactsFragment.listContacts.remove(listItems[holder.absoluteAdapterPosition])

            } else {
                holder.itemChoice.visibility = View.VISIBLE
                listItems[holder.absoluteAdapterPosition].choice = true
                AddContactsFragment.listContacts.add(listItems[holder.absoluteAdapterPosition])
            }
        }
        return holder
    }

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: AddContactsHolder, position: Int) {
        holder.itemName.text = listItems[position].fullname
        holder.itemLastMessage.text = listItems[position].lastMessage
        holder.itemPhoto.downloadAndSetImage(listItems[position].photoUrl)
    }

    fun updateListItems(item: CommonModel){
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }
}