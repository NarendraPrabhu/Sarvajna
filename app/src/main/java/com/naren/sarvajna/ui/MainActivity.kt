package com.naren.sarvajna.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.Toast
import com.naren.sarvajna.R
import com.naren.sarvajna.data.Tripadi
import com.naren.sarvajna.databinding.ViewTripadiBinding
import com.naren.sarvajna.vm.TripadiViewModel

class MainActivity : AppCompatActivity(), TripadiViewModel.Events, TripadiViewModel.DatabaseEvents, SearchView.OnQueryTextListener {

    var listView : ListView? = null
    var tripadiViewModel : TripadiViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var factory = object :  ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>) : T {
                return TripadiViewModel(this@MainActivity, this@MainActivity) as T
            }
        }
        tripadiViewModel = ViewModelProviders.of(this, factory).get(TripadiViewModel::class.java)
        listView = findViewById<ListView>(R.id.list)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        var searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        searchView?.setOnQueryTextListener(this@MainActivity)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_search -> {}
            R.id.menu_favorite -> {
                var checked = !item.isChecked
                item.setChecked(checked)
                item.setIcon(if (checked) R.drawable.favorite_selected else R.drawable.favorite_normal)
                tripadiViewModel?.changeFavorites(checked)
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    inner class TripadiAdapter : BaseAdapter () {

        var items : List<Tripadi>? = null

        fun setData(data : List<Tripadi>?) : Unit {
            this.items = data
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var binding : ViewTripadiBinding?
            if(p1 == null){
                binding = ViewTripadiBinding.inflate(this@MainActivity.layoutInflater)
                var v = binding.root
                v.tag = binding
            } else {
                binding = p1.tag as ViewTripadiBinding?
            }
            var tripadi = getItem(p0)
            binding?.setTripadi(tripadi)
            binding?.events = this@MainActivity
            return binding?.root!!
        }

        override fun getItem(p0: Int): Tripadi? {
            return items?.get(p0)
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return if (items == null) 0 else items?.size!!
        }
    }

    override fun copy(tripadi: Tripadi) {
        if (tripadi != null) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Kagga", tripadi.toString())
            clipboard.primaryClip = clip
            Toast.makeText(this, R.string.warning_kagga_copied, Toast.LENGTH_SHORT).show()
        }
    }

    override fun share(tripadi: Tripadi) {
        if(tripadi != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/*"
            intent.putExtra(Intent.EXTRA_TEXT, true.toString())
            val pm = packageManager
            if (pm != null) {
                val infos = pm.queryIntentActivities(intent, 0)
                if (infos == null) {
                    Toast.makeText(this, R.string.warning_no_app, Toast.LENGTH_SHORT).show()
                    return
                }
                startActivity(Intent.createChooser(intent, getString(R.string.select_app)))
            }
        }
    }

    override fun edit(tripadi: Tripadi) {
        if(tripadi != null){
            tripadiViewModel?.update(tripadi);
        }
    }

    override fun queried(tripadis: List<Tripadi>?) {
        runOnUiThread {
          if (tripadis != null) {
              var adapter = TripadiAdapter()
              adapter.setData(tripadis)
              listView?.adapter = adapter
          }
        }
    }

    override fun updated(unit : Unit?) {

    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        if(!p0?.isEmpty()!!){
            tripadiViewModel?.changeQuery("%".plus(p0).plus("%"))
        }
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
    }

}
