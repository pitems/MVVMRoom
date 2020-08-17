package com.example.mvvmroom.db.repository

import com.example.mvvmroom.db.Subscriber
import com.example.mvvmroom.db.SubscriberDAO

class SubscriberRepository(private val dao: SubscriberDAO) {

    val subscribers = dao.getAllSubscribers()

    suspend fun insert(subscriber: Subscriber):Long{
       return dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber):Int{
       return dao.updateSubscriber(subscriber)
    }

    suspend fun delete (subscriber: Subscriber):Int{
        return dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll():Int{
       return dao.deleteAll()
    }
}