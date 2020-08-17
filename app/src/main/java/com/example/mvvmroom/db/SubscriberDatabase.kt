package com.example.mvvmroom.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subscriber::class],version = 1)
abstract class SubscriberDatabase : RoomDatabase(){

    //We should have only one instance of this
    abstract val subscriberDAO:SubscriberDAO

    companion object{
        @Volatile //Visible to all objects
        private var INSTANCE: SubscriberDatabase? =null
        fun getInstance(context: Context):SubscriberDatabase{
            synchronized(this){
                var instance= INSTANCE
                if(instance==null){
                    instance= Room.databaseBuilder(
                        context.applicationContext,
                        SubscriberDatabase::class.java,
                        "subscriber_data_database"
                    ).build()
                }
                return instance
            }
        }
    }
}