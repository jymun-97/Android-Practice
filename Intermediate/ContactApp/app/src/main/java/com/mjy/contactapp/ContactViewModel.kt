package com.mjy.contactapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val _contactItemList = MutableLiveData<ArrayList<ContactItem>>()
    val contactItemList: LiveData<ArrayList<ContactItem>>
        get() = _contactItemList

    var itemList: ArrayList<ContactItem> = ArrayList<ContactItem>()
        .apply {
            add(ContactItem("test1", "111-0000-0000"))
            add(ContactItem("test2", "222-0000-0000"))
            add(ContactItem("test3", "333-0000-0000"))
            add(ContactItem("test4", "444-0000-0000"))
            add(ContactItem("test5", "555-0000-0000"))
            add(ContactItem("test6", "666-0000-0000"))
        }

    init {
        _contactItemList.value = itemList
    }

    fun addContactItem(
        name: String,
        number: String
    ) {
        itemList.add(ContactItem(name, number))
        _contactItemList.value = itemList

        Log.d("LOG", "${contactItemList.value}")
    }
}