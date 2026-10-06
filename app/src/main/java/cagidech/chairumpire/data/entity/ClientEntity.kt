package cagidech.chairumpire.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ClientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val isMember: Boolean,
)
