package com.example.studentbase

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    companion object{
        lateinit var dbHandler: DBHandler
    }

    val nameStr = "name"
    val ageStr = "age"

    var currentStatus = nameStr
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHandler = DBHandler(this, null, null, 1)

        viewStudents()

        fab.setOnClickListener {
            val i = Intent(this, AddStudentActivity::class.java)
            startActivity(i)
        }
    }

    private fun viewStudents(){
        val list = dbHandler.getStudents(this)
        val adapter = StudentAdapter(this, list)
        val rv: RecyclerView = findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager
        rv.adapter = adapter
    }

    

    override fun onCreateOptionsMenu(menu : Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val item = menu.findItem(R.id.action_one)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id==R.id.action_one){
            sortDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    fun sortDialog() {

        var list = dbHandler.getStudents(this)
        list.sortedWith(compareBy({it.studentName}))
        var list2 = ArrayList<Student>()
        for (i in list){
            list2.add(i)
        }
        var adapter = StudentAdapter(this, list)
        val options = arrayOf("Name", "Age")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sort by")
            .setItems(options, DialogInterface.OnClickListener { _, which ->
                if (which==0){
                    adapter = StudentAdapter(this, list2)
                    adapter.notifyDataSetChanged()
                    } else {
                    list.sortedWith(compareBy { it.credit })
                    adapter.notifyDataSetChanged()
                    }
            })
            .show()
    }

    override fun onResume() {

        viewStudents()
        super.onResume()
    }


}