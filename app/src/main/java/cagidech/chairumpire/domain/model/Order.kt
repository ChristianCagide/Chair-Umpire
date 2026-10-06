package cagidech.chairumpire.domain.model

data class Order(
    val id: Long = 0,
    val client: Client,
    val racketType: String,
    val stringType: String,
    val tension: String,
    val dropOffDateTime: String,
    val pickUpDateTime: String,
    val stringerName: String,
    val dateBilled: String
)
