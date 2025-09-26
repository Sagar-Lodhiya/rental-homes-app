package com.rentalhomes.data.network.model.responseModel

data class GetNotificationListResponse(
    var status: Int = 0,
    var message: String? = "",
    var data: ArrayList<Data>? = null,
) {
    data class Data(
        var notificationId: Int = 0,
        var notificationType: Int = 0,
        var roomId: String? = "",
        var userName: String? = "",
        var userId: Int = 0,
        var text: String? = "",
        var date: String? = "",
        var isRead: Boolean = false
    )
}
