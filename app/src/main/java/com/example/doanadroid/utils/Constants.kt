package com.example.todolist.utils

object Constants{
    const val CALENDAR_TABLE_NAME = "ROOM_CALENDAR"
    const val DATABASE_TABLE = "CALENDAR_DATABASE_ROOM"

    const val COMPLETE_COLUMN = "Hoàn thành"
    const val LIST_ALL_COLUMN = "Danh sách tất cả"
    const val DEFAULT_COLUMN = "Mặc định"
    const val INDIVIDUAL_COLUMN = "Cá nhân"
    const val SHOPPING_COLUMN = "Mua sắm"
    const val WORK_COLUMN = "Làm việc"

    const val BUNDEL_SEND_CALENDAR = "BUNDEL_SEND_CALENDAR"

    const val STATUS_ADD = "ADD"
    const val STATUS_UPDATE = "UPDATE"
    const val STATUS_SAVE_ADD = "SAVE_ADD"
    const val STATUS_SAVE_UPDATE = "SAVE_UPDATE"

    object Action{
        const val ACTION_ADD = 0
        const val ACTION_ADD_SAVE = 1
        const val ACTION_UPDATE_SAVE = 2
    }
    object StatusNotification{
        const val DELETE = "DELETE"
        const val COMPLETE = "COMPLETE"
        const val NO_YET = "NO_YET"
    }

    const val COMPLETE = "Hoàn thành"
    const val NO_YET ="Chưa hoàn thành"

//    const val LIST_ALL = "Danh sách tất cả"
//    const val DEFAULT = "Mặc Định"
//    const val INDIVIDUAL = "Cá Nhân"
//    const val SHOPPING = "Mua Sắm"
//    const val WORK = "Làm Việc"

    const val ACTION_LOGIN = "ACTION_LOGIN"
    const val ACTION_UN_LOGIN = "ACTION_UN_LOGIN"


}