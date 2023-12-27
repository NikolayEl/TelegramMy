package ru.nelshin.telegram.utilits

import ru.nelshin.telegram.R
import ru.nelshin.telegram.database.AUTH
import ru.nelshin.telegram.database.CHILD_STATE
import ru.nelshin.telegram.database.CURRENT_UID
import ru.nelshin.telegram.database.NODE_USERS
import ru.nelshin.telegram.database.REF_DATABASE_ROOT
import ru.nelshin.telegram.database.USER

enum class AppStates(val state: String) {
    ONLINE(APP_ACTIVITY.getString(R.string.setting_status_online)),
    OFFLINE(APP_ACTIVITY.getString(R.string.setting_status_was_recently)),
    TYPING(APP_ACTIVITY.getString(R.string.setting_status_typing));

    companion object {
        fun updateState(appStates: AppStates) {
            if (AUTH.currentUser != null) {
                REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_STATE)
                    .setValue(appStates.state)
                    .addOnSuccessListener { USER.state = appStates.state }
                    .addOnFailureListener { showToast(it.message.toString()) }
            }
        }
    }
}