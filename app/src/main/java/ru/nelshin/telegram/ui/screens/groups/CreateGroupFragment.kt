package ru.nelshin.telegram.ui.screens.groups

import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.nelshin.telegram.R
import ru.nelshin.telegram.models.CommonModel
import ru.nelshin.telegram.ui.screens.base.BaseFragment
import ru.nelshin.telegram.utilits.APP_ACTIVITY
import ru.nelshin.telegram.utilits.getPlurals
import ru.nelshin.telegram.utilits.hideKeyboard
import ru.nelshin.telegram.utilits.showToast

class CreateGroupFragment(private var listContacts:List<CommonModel>):BaseFragment(R.layout.fragment_create_group) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group)
        hideKeyboard()
        initRecyclerView()
        APP_ACTIVITY.findViewById<FloatingActionButton>(R.id.create_group_btn_complete).setOnClickListener {
            showToast("click")
        }
        APP_ACTIVITY.findViewById<EditText>(R.id.create_group_input_name).requestFocus()
        APP_ACTIVITY.findViewById<TextView>(R.id.create_group_counts).text = getPlurals(listContacts.size)
    }

    private fun initRecyclerView() {
        mRecyclerView = APP_ACTIVITY.findViewById(R.id.create_group_recycle_view)
        mAdapter = AddContactsAdapter()
        mRecyclerView.adapter = mAdapter
        listContacts.forEach { mAdapter.updateListItems(it) }
    }
}