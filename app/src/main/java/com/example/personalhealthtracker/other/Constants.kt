package com.example.personalhealthtracker.other

object Constants {
    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACK_RUNNING_FRAGMENT = "ACTION_SHOW_TRACK_RUNNING_FRAGMENT"

    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"

    // at least NOTIFICATION_ID should be 1 or a higher value
    const val NOTIFICATION_ID = 1

    const val REQUEST_CODE_POST_NOTIFICATIONS_PERMISSION = 1001
}