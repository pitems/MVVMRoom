package com.example.mvvmroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmroom.databinding.ActivityMainBinding
import com.example.mvvmroom.db.Subscriber

import com.example.mvvmroom.db.SubscriberDatabase
import com.example.mvvmroom.db.repository.SubscriberRepository
import com.example.mvvmroom.viewmodel.MyRecyclerViewAdapter
import com.example.mvvmroom.viewmodel.SubscriberViewModel
import com.example.mvvmroom.viewmodel.SubscriberViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel:SubscriberViewModel
    private lateinit var adapter: MyRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory= SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this,factory).get(SubscriberViewModel::class.java)

        binding.myViewModel= subscriberViewModel
        binding.lifecycleOwner=this
        initRecyclerView()

        subscriberViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                Toast.makeText(this,it,Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun initRecyclerView(){
        adapter = MyRecyclerViewAdapter({selectedItem:Subscriber -> listItemClicked(selectedItem)})
        binding.subscriberRecyclerView.adapter=adapter
        binding.subscriberRecyclerView.layoutManager= LinearLayoutManager(this)
        displaySubscribersList()
    }
    private fun displaySubscribersList(){
        subscriberViewModel.subscribers.observe(this, Observer {
        adapter.setList(it)
        adapter.notifyDataSetChanged()

        })
    }

    private fun listItemClicked(subscriber: Subscriber){
       // Toast.makeText(this,"selected name is ${subscriber.name}",Toast.LENGTH_LONG).show()
        subscriberViewModel.initUpdateAndDelete(subscriber)
    }
}