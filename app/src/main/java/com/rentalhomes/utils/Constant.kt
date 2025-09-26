package com.rentalhomes.utils

object Constant {
    const val STATUS_FAIL: Short = 0
    const val STATUS_SUCCESS: Short = 1
    const val STATUS_DELETE_USER: Short = 2
    const val STATUS_INACTIVE_USER: Short = 3
    const val STATUS_TOKEN_EXPIRE: Short = 4
    const val IMAGE_REQUEST_CODE = 7
    const val VIDEO_REQUEST_CODE = 8

    const val SPLASH_SCREEN_DELAY: Long = 3000
    const val REQUEST_IMAGE = 100
    const val REQUEST_QR = 101

    const val VIEW_TYPE_ITEM = 0
    const val VIEW_TYPE_LOADING = 1

    const val QR_PREFIX = ""

    const val DEVICE_TYPE_VALUE = 2

    const val SERVER_KEY =
        "AAAAxi7nGKs:APA91bHbOk1WyiQTnCQdxNFizsmoGN_6ZFJninJxnAG9TyOIVNxHvWfFMerxE3BV5ODDsDFeUQ0msc-9iue5A7r_IwSdeS0XvATERCUc1MWDLJJMgHwNOWpcMUTKD7s91UuplP6-PzJZ"


    const val FCM_BASEURL_SEND = "https://fcm.googleapis.com/fcm/"
    const val FCM_SEND = "send"

    const val CHAT = 12
    const val ROOM_TYPE_GROUP: Long = 1
    const val ROOM_TYPE_SINGLE: Long = 0

    //    Notification
    const val FCM_N_TYPE = "notificationType"
    const val FCM_BODY = "body"

    const val MESSAGE_BROADCAST_ACTION = "messageArrived"

    //    Intent
    const val CHAT_ROOM_MODEL = "ChatRoomModel"
    const val USER_MODEL = "UserModel"
    const val USER_LIST = "USER_LIST"
    const val FROM = "FROM"
    const val FROM_BUYER_DETAIL = "FROM_BUYER_DETAIL"
    const val CHAT_TYPE = "CHAT_TYPE"
    const val GROUP = "GROUP"
    const val SINGLE = "SINGLE"
    const val FROM_CHAT = "FROM_CHAT"
    const val FROM_USER_LIST = "FROM_USER_LIST"
    const val IS_FROM_NOTIFICATION = "IS_FROM_NOTIFICATION"
    const val OTHER_USER_ID = "otherUserId"
    var CURRENT_CHAT_ID = ""
    const val CHAT_ID = "ChatId"

    const val STORAGE_IMAGE = "images"
    const val STORAGE_VIDEO = "videos"
    const val STORAGE_THUMBNAILS = "thumbnails"

    const val USER_PROFILE_PIC = "userProfilePictures"
    const val GROUP_PROFILE_PIC = "groupProfilePictures"

    const val ACTION_GROUP_CREATED: Long = 1
    const val ACTION_GROUP_MEMBER_ADDED: Long = 2
    const val ACTION_GROUP_MEMBER_REMOVED: Long = 3

    const val FILE_UPLOAD = 212

    const val FILE_UPLOAD_RESULT = "FILE_UPLOAD_RESULT"
    const val FILE_TYPE = "FILE_TYPE"
    const val DELETE_FILE = "DELETE_FILE"

    const val IMAGE = "IMAGE"
    const val VIDEO = "VIDEO"

    const val SERVICE_DATA = "service_data"
    const val CHAT_MESSAGE_DATA = "chat_message_data"
    const val DUMMY_MESSAGE = "dummy_message"

    const val OPERATION_ADDED = 1
    const val OPERATION_MODIFIED = 2
    const val OPERATION_REMOVED = 3
    const val PAGE_SIZE_LIMIT = 100

    /*Chat Module*/
    const val Image_Request_Code = 7
    const val Video_Request_Code = 8

    //    const val SPLASH_SCREEN_DELAY: Long = 3000

    const val EMAIL = "email"
    const val MEDIA = "media"
    const val NAME = "name"
    const val BIRTHDATE = "birthDate"
    const val DOB = "dob"
    const val ID = "id"
    const val ROOM_NAME = "roomName"
    const val USER_ID = "userId"
    const val TOKEN = "token"
    const val PROFILE_PICTURE = "profilePic"
    const val PROFILE_COLOR = "profileColor"
    const val USERS = "users"
    const val PROPERTIES = "properties"
    const val ROOMS = "rooms"
    const val MESSAGES = "messages"
    const val MESSAGE = "message"
    const val SENDER_ID = "senderId"
    const val SENDER_NAME = "senderName"
    const val THUMBNAIL = "thumbnail"
    const val TIMESTAMP = "timestamp"
    const val SERVER_TIMESTAMP = "serverTimeStamp"
    const val UNREAD_BY = "unReadBy"
    const val RECEIVED_BY = "receivedReadBy"
    const val IS_GROUP = "isGroup"
    const val ACTION = "action"
    const val ACTION_DONE_BY = "actionDoneBy"
    const val ACTION_DONE_ON = "actionDoneOn"
    const val NEWDAYMSSGTHREAD = "newDayMssgThread"
    const val CHERUB = "Cherub"
    const val DEVICE_TYPE = "deviceType"
    const val USER_STATE = "state"
    const val ONLINE = "online"
    const val OFFLINE = "offline"
    const val LAST_CHANGED = "last_changed"
    const val PROFILE_THUMB = "profileThumb"
    const val LAST_UPDATED = "lastUpdated"
    const val PROPERTY_UNREAD_COUNT = "propertyUnreadCount"
    const val NOTIFICATION_COUNT = "notificationCount"
    const val CHAT_COUNT = "chatCount"

    //    Intent
    const val STORAGE_AUDIO = "audios"
    const val STORAGE_DOCUMENTS = "documents"

    const val PERMISSION_REQUEST_GALLERY = 2121

    const val MediaPosition = "MediaPosition"
    const val VIDEO_PATH = "video_path"
    const val VIDEO_COMPRESSOR_APPLICATION_DIR_NAME = "VideoCompressor"
    const val VIDEO_COMPRESSOR_COMPRESSED_VIDEOS_DIR = "/Compressed Videos/"
    const val VIDEO_COMPRESSOR_TEMP_DIR = "/Temp/"
    const val VIDEO_ONE = 15
    const val QUALITY: Int = 100
}