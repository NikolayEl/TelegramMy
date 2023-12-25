package ru.nelshin.telegram.utilits

import ru.nelshin.telegram.R

enum class AppStates(val state:String) {
    ONLINE(APP_ACTIVITY.getString(R.string.setting_status_online)),
    OFFLINE(APP_ACTIVITY.getString(R.string.setting_status_was_recently)),
    TYPING(APP_ACTIVITY.getString(R.string.setting_status_typing));

    companion object{
        fun updateState(appStates: AppStates){
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_STATE)
                .setValue(appStates.state)
                .addOnSuccessListener { USER.state = appStates.state }
                .addOnFailureListener{ showToast(it.message.toString())}
        }
    }
}