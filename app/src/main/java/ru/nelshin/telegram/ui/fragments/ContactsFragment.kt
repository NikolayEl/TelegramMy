package ru.nelshin.telegram.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView
import ru.nelshin.telegram.R
import ru.nelshin.telegram.databinding.FragmentContactsBinding
import ru.nelshin.telegram.models.CommonModel
import ru.nelshin.telegram.utilits.APP_ACTIVITY
import ru.nelshin.telegram.utilits.AppValueEventListener
import ru.nelshin.telegram.utilits.CURRENT_UID
import ru.nelshin.telegram.utilits.NODE_PHONES_CONTACTS
import ru.nelshin.telegram.utilits.NODE_USERS
import ru.nelshin.telegram.utilits.REF_DATABASE_ROOT
import ru.nelshin.telegram.utilits.downloadAndSetImage
import ru.nelshin.telegram.utilits.getCommonModel


class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    private lateinit var mBinding: FragmentContactsBinding

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactHolder>
    private lateinit var mRefContacts: DatabaseReference
    private lateinit var mRefUsers: DatabaseReference
    private lateinit var mRefUsersListener: AppValueEventListener
    private var mapListeners = hashMapOf<DatabaseReference, AppValueEventListener>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentContactsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Contacts"
        initRecycleView()
    }

    private fun initRecycleView() {
        mRecyclerView = mBinding.contactsRecycleView
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts, CommonModel::class.java)
            .build()
        mAdapter = object : FirebaseRecyclerAdapter<CommonModel, ContactHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.contact_item, parent, false)
                return ContactHolder(view)
            }

            override fun onBindViewHolder(
                holder: ContactHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)

                mRefUsersListener = AppValueEventListener {
                    val contact = it.getCommonModel()
                    holder.name.text = contact.fullname
                    holder.status.text = contact.state
                    holder.photo.downloadAndSetImage(contact.photoUrl)
                }

                mRefUsers.addValueEventListener(mRefUsersListener)
                mapListeners[mRefUsers] = mRefUsersListener
            }

        }

        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }

    class ContactHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.contact_fullname)
        val status: TextView = view.findViewById(R.id.contact_status)
        val photo: CircleImageView = view.findViewById(R.id.contact_photo)
    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
        println()
        mapListeners.forEach {
            it.key.removeEventListener(it.value)
        }
        println()
    }
}

