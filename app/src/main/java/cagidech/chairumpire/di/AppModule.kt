package cagidech.chairumpire.di

import android.content.Context
import androidx.room.Room
import cagidech.chairumpire.data.dao.ClientDao
import cagidech.chairumpire.data.dao.OrderDao
import cagidech.chairumpire.data.db.ChairUmpireDB
import cagidech.chairumpire.data.repository.ClientRepositoryImpl
import cagidech.chairumpire.data.repository.OrderRepositoryImpl
import cagidech.chairumpire.domain.repository.ClientRepository
import cagidech.chairumpire.domain.repository.OrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): ChairUmpireDB {
        return Room.databaseBuilder(
            context = context,
            ChairUmpireDB::class.java,
            "chair_umpire_database.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideClientDao(database: ChairUmpireDB): ClientDao {
        return database.getClientDao()
    }

    @Provides
    @Singleton
    fun provideOrderDao(database: ChairUmpireDB): OrderDao {
        return database.getOrderDao()
    }

    @Provides
    @Singleton
    fun provideClientRepository(clientDao: ClientDao): ClientRepository {
        return ClientRepositoryImpl(clientDao)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(orderDao: OrderDao): OrderRepository {
        return OrderRepositoryImpl(orderDao)
    }
}
