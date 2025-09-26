package com.rentalhomes.data.network.model.requestModel

data class CreateCustomTemplateRequest(
    var userId: Int,
    var propertyId: Int,
    var data: ArrayList<Data>,
) {
    data class Data(
        var featureId: Int,
        var visible: Int,
    ) {

    }
}
