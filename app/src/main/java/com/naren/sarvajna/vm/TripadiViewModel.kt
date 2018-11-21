package com.naren.sarvajna.vm

import android.app.Activity
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.naren.sarvajna.data.Tripadi
import com.naren.sarvajna.data.TripadiDao
import com.naren.sarvajna.data.TripadiDatabase
import kotlin.concurrent.thread

class TripadiViewModel (activity: Activity, private val databaseEvents: DatabaseEvents)  : AndroidViewModel(activity.application) {

    interface Events {
        fun copy(tripadi : Tripadi)
        fun share(tripadi: Tripadi)
        fun edit(tripadi: Tripadi)
    }

    interface DatabaseEvents{
        fun queried(tripadis : List<Tripadi>?)
        fun updated(unit : Unit?)
    }

    var tripadiDao : TripadiDao? = null
    var query : MutableLiveData<String> = MutableLiveData()
    var favorite : MutableLiveData<Boolean> = MutableLiveData()

    init {
        tripadiDao = TripadiDatabase.getInstance(activity.applicationContext)?.tripadiDao()
        query.observe(activity as LifecycleOwner, Observer { queryString ->
            val q = queryString?.toString() ?: "%%"
            query(q, favorite.value?.and(true) ?: false)
        })
        favorite.observe(activity as LifecycleOwner, Observer { isFavorite ->
            val f = isFavorite?.and(true) ?: false
            query(query.value?.toString() ?: "%%", f)
        })
    }

    private fun query(q : String, f : Boolean) : Unit {
        thread {
            databaseEvents.queried(if (f) tripadiDao?.query(q, if (f) 1 else 0) else tripadiDao?.query(q))
        }
    }

    fun changeQuery(q : String) {
        query.value = q
    }

    fun changeFavorites(b : Boolean) {
        favorite.value = b
    }

    fun update(tripadi: Tripadi) {
        thread {
            databaseEvents.updated(tripadiDao?.update(tripadi))
        }
    }

    fun refresh() {
        query(query?.value?.toString() ?: "%%", favorite?.value?.and(true) ?: false)
    }
}