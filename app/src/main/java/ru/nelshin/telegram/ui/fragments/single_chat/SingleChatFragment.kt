package ru.nelshin.telegram.ui.fragments.single_chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView
import ru.nelshin.telegram.R
import ru.nelshin.telegram.databinding.FragmentSingleChatBinding
import ru.nelshin.telegram.models.CommonModel
import ru.nelshin.telegram.models.User
import ru.nelshin.telegram.ui.fragments.BaseFragment
import ru.nelshin.telegram.utilits.APP_ACTIVITY
import ru.nelshin.telegram.utilits.AppValueEventListener
import ru.nelshin.telegram.utilits.CURRENT_UID
import ru.nelshin.telegram.utilits.NODE_MESSAGES
import ru.nelshin.telegram.utilits.NODE_USERS
import ru.nelshin.telegram.utilits.REF_DATABASE_ROOT
import ru.nelshin.telegram.utilits.TYPE_TEXT
import ru.nelshin.telegram.utilits.getUserModel
import ru.nelshin.telegram.utilits.downloadAndSetImage
import ru.nelshin.telegram.utilits.getCommonModel
import ru.nelshin.telegram.utilits.sendMessage
import ru.nelshin.telegram.utilits.showToast

class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mBinding: FragmentSingleChatBinding

    private lateinit var mListenerInfoToolbat: AppValueEventListener
    private lateinit var mReceivingUser: User
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecycleView: RecyclerView
    private lateinit var mMessagesListener:AppValueEventListener
    private var mListMessages = emptyList<CommonModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        initToolBar()
        initRecycleView()
    }

    private fun initRecycleView() {
        mRecycleView = mBinding.chatRecycleView
        mAdapter = SingleChatAdapter()
        mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES)
            .child(CURRENT_UID)
            .child(contact.id)
        mRecycleView.adapter = mAdapter
        mMessagesListener = AppValueEventListener {dataSnapshot ->
            mListMessages = dataSnapshot.children.map { it.getCommonModel() }
            mAdapter.setList(mListMessages)
            mRecycleView.smoothScrollToPosition(mAdapter.itemCount)
        }
        mRefMessages.addValueEventListener(mMessagesListener)
    }

    private fun initToolBar() {
        mToolbarInfo = APP_ACTIVITY.mToolbar.findViewById<ConstraintLayout>(R.id.toolbar_info)
        mToolbarInfo.visibility = View.VISIBLE
        mListenerInfoToolbat = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initInfoToolbar()
        }

        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefUser.addValueEventListener(mListenerInfoToolbat)
        mBinding.chatBtnSendMessage.setOnClickListener {
            val message = mBinding.chatInputMessage.text.toString()
            if (message.isEmpty()) {
                showToast("Enter message")
            } else sendMessage(message, contact.id, TYPE_TEXT) {
                mBinding.chatInputMessage.setText("")
            }
        }
    }

    private fun initInfoToolbar() {
        if (mReceivingUser.fullname.isEmpty()) {
            mToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_fullname).text =
                contact.fullname
        } else mToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_fullname).text =
            mReceivingUser.fullname
        mToolbarInfo.findViewById<CircleImageView>(R.id.toolbar_chat_image)
            .downloadAndSetImage(mReceivingUser.photoUrl)
        mToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_status).text =
            mReceivingUser.state
    }

    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        mRefUser.removeEventListener(mListenerInfoToolbat)
        mRefMessages.removeEventListener(mMessagesListener)
    }
}