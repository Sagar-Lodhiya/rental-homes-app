package com.rentalhomes.data.network.model.responseModel

class LinkUserListResponse(
    val status: Int = 0,
    val message: String? = "",
    val data:ArrayList<Data>
) {

    data class Data(
        val userId: Int = 0,
        val profilePic: String = "",
        val profileThumb: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val mobile: String = "",

    ) {
    }
}