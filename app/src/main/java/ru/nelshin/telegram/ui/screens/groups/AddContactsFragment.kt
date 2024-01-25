package ru.nelshin.telegram.ui.screens.groups

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.nelshin.telegram.R
import ru.nelshin.telegram.database.CURRENT_UID
import ru.nelshin.telegram.database.NODE_MAIN_LIST
import ru.nelshin.telegram.database.NODE_MESSAGES
import ru.nelshin.telegram.database.NODE_USERS
import ru.nelshin.telegram.database.REF_DATABASE_ROOT
import ru.nelshin.telegram.database.getCommonModel
import ru.nelshin.telegram.models.CommonModel
import ru.nelshin.telegram.ui.screens.base.BaseFragment
import ru.nelshin.telegram.utilits.APP_ACTIVITY
import ru.nelshin.telegram.utilits.AppValueEventListener
import ru.nelshin.telegram.utilits.hideKeyboard
import ru.nelshin.telegram.utilits.replaceFragment
import ru.nelshin.telegram.utilits.showToast

class AddContactsFragment : BaseFragment(R.layout.fragment_add_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private val mRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    private var mListItems = listOf<CommonModel>()

    override fun onResume() {
        listContacts.clear()
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.add_member)
        hideKeyboard()
        initRecyclerView()
        APP_ACTIVITY.findViewById<FloatingActionButton>(R.id.add_contacts_btn_next).setOnClickListener {
            if(listContacts.isEmpty()){
                showToast(getString(R.string.add_at_least_one_participant))
            } else listContacts.forEach{
                    replaceFragment(CreateGroupFragment(listContacts))
            }
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = APP_ACTIVITY.findViewById(R.id.add_contacts_recycle_view)
        mAdapter = AddContactsAdapter()

        // 1 запрос: Получаем лист
        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            mListItems = dataSnapshot.children.map { it.getCommonModel() }
            mListItems.forEach { model ->

                // 2 запрос по этой модели мы отправлемся в ноду юзерс и получаем все его данные
                mRefUsers.child(model.id).addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                    val newModel = dataSnapshot1.getCommonModel()

                    //3 запрос: Мы обращаемся в сообщения, находим этот контакт, обращаемся к последнему элементу и считываем данные
                    mRefMessages.child(model.id).limitToLast(1)
                        .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                            val tempList = dataSnapshot2.children.map {it.getCommonModel()}

                            if(tempList.isEmpty()){
                                newModel.lastMessage = getString(R.string.chat_cleared)
                            } else {
                                newModel.lastMessage = tempList[0].text
                            }

                            if(newModel.fullname.isEmpty()){
                                newModel.fullname = newModel.phone
                            }
                            mAdapter.updateListItems(newModel)
                        })
                })
            }
        })
        mRecyclerView.adapter = mAdapter
    }
    companion object{
        val listContacts = mutableListOf<CommonModel>()
    }
}