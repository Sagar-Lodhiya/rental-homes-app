package com.rentalhomes.data.network.model.responseModel

data class AddItemsInTemplateResponse(
    var status: Int = 0,
    var message: String? = "",
    var category: String? = "",
    var data: ArrayList<Data>? = null,
) {

    data class Data(
        var featureId: Int = 0,
        var featureName: String? = "",
    ) {

    }
}
