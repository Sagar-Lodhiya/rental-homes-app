package com.rentalhomes.data.network.model.responseModel

import java.util.*
import kotlin.collections.ArrayList

class PropertyChatListResponse(
    var address:String?=null,
    var city: String?=null,
    var propertyImage:String?=null,
    var propertyThumb:String?=null,
    var userId:String?=null,
    var lastUpdated: Date?=null,
    var id:String?=null,
    var propertyCreated:String?=null,
    var chatUsers: ArrayList<String>?=null,
    var propertyUnreadCount :Long?=null
)