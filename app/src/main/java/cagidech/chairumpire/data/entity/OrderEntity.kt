package cagidech.chairumpire.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = CASCADE
        )
    ]
)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val clientId: Long,
    val racketType: String,
    val stringType: String,
    val tension: String,
    val dropOffDateTime: String,
    val pickUpDateTime: String,
    val stringerName: String,
    val dateBilled: String
)
