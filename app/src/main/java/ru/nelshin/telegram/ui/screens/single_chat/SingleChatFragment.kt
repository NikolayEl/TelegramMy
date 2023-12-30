package ru.nelshin.telegram.ui.screens.single_chat

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DatabaseReference
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nelshin.telegram.R
import ru.nelshin.telegram.database.CURRENT_UID
import ru.nelshin.telegram.database.NODE_MESSAGES
import ru.nelshin.telegram.database.NODE_USERS
import ru.nelshin.telegram.database.REF_DATABASE_ROOT
import ru.nelshin.telegram.database.TYPE_TEXT
import ru.nelshin.telegram.database.getCommonModel
import ru.nelshin.telegram.database.getMessageKey
import ru.nelshin.telegram.database.getUserModel
import ru.nelshin.telegram.database.sendMessage
import ru.nelshin.telegram.database.uploadFileToStorage
import ru.nelshin.telegram.databinding.FragmentSingleChatBinding
import ru.nelshin.telegram.models.CommonModel
import ru.nelshin.telegram.models.User
import ru.nelshin.telegram.ui.screens.BaseFragment
import ru.nelshin.telegram.ui.message_recycle_view.views.AppViewFactory
import ru.nelshin.telegram.utilits.APP_ACTIVITY
import ru.nelshin.telegram.utilits.AppChildEventListener
import ru.nelshin.telegram.utilits.AppTextWatcher
import ru.nelshin.telegram.utilits.AppValueEventListener
import ru.nelshin.telegram.utilits.AppVoiceRecorder
import ru.nelshin.telegram.utilits.RECORD_AUDIO
import ru.nelshin.telegram.utilits.TYPE_MESSAGE_IMAGE
import ru.nelshin.telegram.utilits.TYPE_MESSAGE_VOICE
import ru.nelshin.telegram.utilits.chekPermission
import ru.nelshin.telegram.utilits.downloadAndSetImage
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
    private lateinit var mMessagesListener: AppChildEventListener
    private var mCountMessages = 15
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAppVoiceRecorder: AppVoiceRecorder

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
        initFields()
        initToolBar()
        initRecycleView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initFields() {
        mAppVoiceRecorder = AppVoiceRecorder()
        mSwipeRefreshLayout = mBinding.chatSwipeRefresh
        mLayoutManager = LinearLayoutManager(this.context)
        mBinding.chatInputMessage.addTextChangedListener(AppTextWatcher {
            val string = mBinding.chatInputMessage.text.toString()
            if (string.isEmpty() || string == "Record") {
                mBinding.chatBtnSendMessage.visibility = View.GONE
                mBinding.chatBtnAttach.visibility = View.VISIBLE
                mBinding.chatBtnVoice.visibility = View.VISIBLE
            } else {
                mBinding.chatBtnSendMessage.visibility = View.VISIBLE
                mBinding.chatBtnAttach.visibility = View.GONE
                mBinding.chatBtnVoice.visibility = View.GONE
            }
        })

        mBinding.chatBtnAttach.setOnClickListener { attachFile() }

        CoroutineScope(Dispatchers.IO).launch {
            mBinding.chatBtnVoice.setOnTouchListener { v, event ->
                if (chekPermission(RECORD_AUDIO)) {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        //TODO record
                        mBinding.chatInputMessage.setText(getString(R.string.hint_chat_input_Message_click))
                        mBinding.chatBtnVoice.setColorFilter(
                            ContextCompat.getColor(
                                APP_ACTIVITY,
                                R.color.colorPrimary
                            )
                        )
                        val messageKey = getMessageKey(contact.id)
                        mAppVoiceRecorder.startRecord(messageKey)
                    } else if (event.action == MotionEvent.ACTION_UP) {
                        //TODO stop record
                        mBinding.chatInputMessage.setText("")
                        mBinding.chatBtnVoice.colorFilter = null
                        mAppVoiceRecorder.stopRecord { file, messageKey ->
                            uploadFileToStorage(Uri.fromFile(file), messageKey, contact.id, TYPE_MESSAGE_VOICE)
                            mSmoothScrollToPosition = true
                        }
                    }
                }
                true
            }
        }
    }

    private fun attachFile() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250, 250)
            .start(APP_ACTIVITY, this)
    }

    private fun initRecycleView() {
        mRecycleView = mBinding.chatRecycleView
        mAdapter = SingleChatAdapter()
        mRefMessages = REF_DATABASE_ROOT
            .child(NODE_MESSAGES)
            .child(CURRENT_UID)
            .child(contact.id)
        mRecycleView.adapter = mAdapter
        mRecycleView.setHasFixedSize(true)
        mRecycleView.isNestedScrollingEnabled = false
        mRecycleView.layoutManager = mLayoutManager

        mMessagesListener = AppChildEventListener {
            val message = it.getCommonModel()
            if (mSmoothScrollToPosition) {
                mAdapter.addItemToBottom(AppViewFactory.getView(message)) {
                    mRecycleView.smoothScrollToPosition(mAdapter.itemCount)
                }
            } else {
                mAdapter.addItemToTop(AppViewFactory.getView(message)) {
                    mSwipeRefreshLayout.isRefreshing = false
                }
            }
        }

        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)

        mRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition() <= 3) {
                    updateData()
                }
            }
        })

        mSwipeRefreshLayout.setOnRefreshListener { updateData() }
    }

    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mRefMessages.removeEventListener(mMessagesListener)
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
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
            mSmoothScrollToPosition = true
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val uri = CropImage.getActivityResult(data).uri
            val messageKey = getMessageKey(contact.id)
            uploadFileToStorage(uri, messageKey, contact.id, TYPE_MESSAGE_IMAGE)
            mSmoothScrollToPosition = true
        }
    }

    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        mRefUser.removeEventListener(mListenerInfoToolbat)
        mRefMessages.removeEventListener(mMessagesListener)
        println()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAppVoiceRecorder.releaseRecrorder()
    }
}