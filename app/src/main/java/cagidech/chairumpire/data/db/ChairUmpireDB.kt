package cagidech.chairumpire.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import cagidech.chairumpire.data.dao.ClientDao
import cagidech.chairumpire.data.dao.OrderDao
import cagidech.chairumpire.data.entity.ClientEntity
import cagidech.chairumpire.data.entity.OrderEntity

@Database(
    version = 1,
    entities = [
        ClientEntity::class,
        OrderEntity::class
    ]
)
abstract class ChairUmpireDB : RoomDatabase() {

    abstract fun getClientDao(): ClientDao
    abstract fun getOrderDao(): OrderDao


}