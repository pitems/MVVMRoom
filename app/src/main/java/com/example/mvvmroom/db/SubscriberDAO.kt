package com.example.mvvmroom.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDAO {

    //Incase the id conflict in the project we can either replace it or ignore it as a strategy
    @Insert(onConflict = OnConflictStrategy.REPLACE) //Remember that suspend is for async task
    suspend fun insertSubscriber(subscriber: Subscriber): Long

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber): Int

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber) :Int

    @Query("DELETE FROM subscriber_data_table")
    suspend fun deleteAll():Int

    //Because this returns a livedata object there's no need to make it suspend it already works on another thread
    @Query("SELECT * FROM subscriber_data_table")
    fun getAllSubscribers():LiveData<List<Subscriber>>
}