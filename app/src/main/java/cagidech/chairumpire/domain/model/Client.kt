package cagidech.chairumpire.domain.model

data class Client(
    val id: Long = 0,
    val name: String,
    val phone: String,
    val isMember: Boolean,
)
