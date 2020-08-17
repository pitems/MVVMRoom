package com.example.mvvmroom.viewmodel

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmroom.db.Subscriber
import com.example.mvvmroom.db.repository.SubscriberRepository
import com.example.mvvmroom.util.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SubscriberViewModel(private val repository: SubscriberRepository) :ViewModel(), Observable {

    val subscribers = repository.subscribers
    private var isUpdateOrDelete = false
    private lateinit var subsciberToUpdateorDelete:Subscriber

    @Bindable
    val inputName = MutableLiveData<String>()
    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val saveButtonText= MutableLiveData<String>()
    @Bindable
    val clearDeleteButtontext = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message :LiveData<Event<String>>
    get()=statusMessage

    init {
        //The text should change dynamically
        saveButtonText.value= "Save"
        clearDeleteButtontext.value="Clear All"
    }

    fun saveorUpdate(){
        if(inputName.value==null){
            statusMessage.value= Event("Please Enter Subscriber Name")
        }else if(inputEmail.value==null){
            statusMessage.value= Event("Please Enter Subscriber Email ")
        }else if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()){
            statusMessage.value= Event("Please Enter a correct Email Address ")
        }else {
            if(isUpdateOrDelete){
                subsciberToUpdateorDelete.name=inputName.value!!
                subsciberToUpdateorDelete.email=inputEmail.value!!
                update(subsciberToUpdateorDelete)
            }else{
                val name = inputName.value!!
                val email = inputEmail.value!!

                insert(Subscriber(0,name,email))
                inputName.value=null
                inputEmail.value=null
            }
        }




    }

    fun clearAllorDelete(){
        if(isUpdateOrDelete){
            delete(subsciberToUpdateorDelete)
        }else{
            clearAll()
        }

    }

    fun insert(subscriber: Subscriber): Job =viewModelScope.launch{
        val newRowId =  repository.insert(subscriber)
        if(newRowId>-1){
            statusMessage.value= Event("Subscriber Inserted Successfully $newRowId")
        }else{
            statusMessage.value= Event("Error Occurred")
        }

    }

    fun update(subscriber: Subscriber): Job =viewModelScope.launch{
        val noOFRows =repository.update(subscriber)
        if(noOFRows>0){
            inputName.value=null
            inputEmail.value=null
            isUpdateOrDelete=false
            saveButtonText.value="Save"
            clearDeleteButtontext.value="Clear All"
            statusMessage.value= Event("Subscriber Updated Successfully $noOFRows")
        }else{
            statusMessage.value= Event("Error Occurred")
        }


    }

    fun delete(subscriber: Subscriber): Job =viewModelScope.launch{
        val rowDeleted=repository.delete(subscriber)
        if(rowDeleted>0){
            inputName.value=null
            inputEmail.value=null
            isUpdateOrDelete=false
            saveButtonText.value="Save"
            clearDeleteButtontext.value="Clear All"
            statusMessage.value= Event("Subscriber Deleted Successfully ${rowDeleted}")
        }else{
            statusMessage.value= Event("Error Occurred")
        }

    }

    fun clearAll()= viewModelScope.launch {
        val noRowsDeleted=repository.deleteAll()
        if(noRowsDeleted>0){
            statusMessage.value= Event("All Subscribers Deleted Successfully ${noRowsDeleted}")
        }else{
            statusMessage.value= Event("Error Occurred")
        }

    }

    fun initUpdateAndDelete(subscriber: Subscriber){
        inputName.value=subscriber.name
        inputEmail.value=subscriber.email
        isUpdateOrDelete=true
        subsciberToUpdateorDelete= subscriber
        saveButtonText.value="Update"
        clearDeleteButtontext.value="Delete"
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }


}