package ru.nelshin.telegram.ui.screens.groups

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView
import ru.nelshin.telegram.R
import ru.nelshin.telegram.database.createGroupToDatabase
import ru.nelshin.telegram.models.CommonModel
import ru.nelshin.telegram.ui.screens.base.BaseFragment
import ru.nelshin.telegram.ui.screens.main_list.MainListFragment
import ru.nelshin.telegram.utilits.APP_ACTIVITY
import ru.nelshin.telegram.utilits.getPlurals
import ru.nelshin.telegram.utilits.hideKeyboard
import ru.nelshin.telegram.utilits.replaceFragment
import ru.nelshin.telegram.utilits.showToast

class CreateGroupFragment(private var listContacts:List<CommonModel>):BaseFragment(R.layout.fragment_create_group) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private var mUri = Uri.EMPTY
    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group)
        hideKeyboard()
        initRecyclerView()
        APP_ACTIVITY.findViewById<CircleImageView>(R.id.create_group_photo).setOnClickListener { addPhoto() }
        APP_ACTIVITY.findViewById<FloatingActionButton>(R.id.create_group_btn_complete).setOnClickListener {
            val nameGroup = APP_ACTIVITY.findViewById<EditText>(R.id.create_group_input_name).text.toString()
            if(nameGroup.isEmpty()){
                showToast(getString(R.string.enter_group_name))
            } else {
                createGroupToDatabase(nameGroup, mUri, listContacts){
                    replaceFragment(MainListFragment())
                }
            }
        }
        APP_ACTIVITY.findViewById<EditText>(R.id.create_group_input_name).requestFocus()
        APP_ACTIVITY.findViewById<TextView>(R.id.create_group_counts).text = getPlurals(listContacts.size)
    }

    private fun addPhoto() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250, 250)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    private fun initRecyclerView() {
        mRecyclerView = APP_ACTIVITY.findViewById(R.id.create_group_recycle_view)
        mAdapter = AddContactsAdapter()
        mRecyclerView.adapter = mAdapter
        listContacts.forEach { mAdapter.updateListItems(it) }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
            mUri = CropImage.getActivityResult(data).uri
            APP_ACTIVITY.findViewById<CircleImageView>(R.id.create_group_photo).setImageURI(mUri)
        }
    }
}