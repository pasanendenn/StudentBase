package com.example.studentbase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class DBHandler (context : Context, name : String?, factory : SQLiteDatabase.CursorFactory?, version : Int) :
    SQLiteOpenHelper(context,
        DATABASE_NAME, factory,
        DATABASE_VERSION
    ){

    companion object{
        private val DATABASE_NAME = "MyData.db"
        private val DATABASE_VERSION = 1

        val STUDENTS_TABLE_NAME = "Students"
        val COLUMN_STUDENTID = "studentid"
        val COLUMN_STUDENTNAME = "studentname"
        val COLUMN_CREDIT = "credit"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val STUDENTS_TABLE : String = ("CREATE TABLE $STUDENTS_TABLE_NAME (" +
                "$COLUMN_STUDENTID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_STUDENTNAME TEXT," +
                "$COLUMN_CREDIT DOUBLE DEFAULT 0)")
        db?.execSQL(STUDENTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun getStudents(mCtx: Context):ArrayList<Student>{
        var qry = "Select * From $STUDENTS_TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)
        val students = ArrayList<Student>()

        if (cursor.count == 0){
            Toast.makeText(mCtx, "No Records Found", Toast.LENGTH_SHORT).show()
        } else {
            cursor.moveToFirst()
            while (!cursor.isAfterLast){
                val customer = Student()
                customer.studentID = cursor.getInt(cursor.getColumnIndex(COLUMN_STUDENTID))
                customer.studentName = cursor.getString(cursor.getColumnIndex(COLUMN_STUDENTNAME))
                customer.credit = cursor.getDouble(cursor.getColumnIndex(COLUMN_CREDIT))
                students.add(customer)
                cursor.moveToNext()
            }
            Toast.makeText(mCtx, "${cursor.count.toString()} Records Found", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        db.close()
        return students
    }

    fun addStudent(mCtx: Context, student: Student){
        val values = ContentValues()
        values.put(COLUMN_STUDENTNAME, student.studentName)
        values.put(COLUMN_CREDIT, student.credit)
        val db = this.writableDatabase
        try {
            db.insert(STUDENTS_TABLE_NAME, null, values)
            Toast.makeText(mCtx, "Student added", Toast.LENGTH_SHORT).show()
        } catch (e : Exception){
            Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
        }

        db.close()
    }

    fun deleteStudent(studentID: Int) : Boolean {
        val qry = "Delete from $STUDENTS_TABLE_NAME where $COLUMN_STUDENTID = $studentID"
        val db = this.writableDatabase
        var result = false
        try {
            val cursor = db.execSQL(qry)
            result = true
        } catch (e: Exception){
            Log.e(ContentValues.TAG, "Error Deleting")
        }
        db.close()
        return result
    }

    fun updateStudent (id:String, studentName:String, credit:String) : Boolean{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        var result = false
        contentValues.put(COLUMN_STUDENTNAME, studentName)
        contentValues.put(COLUMN_CREDIT, credit.toDouble())

        try {
            db.update(STUDENTS_TABLE_NAME, contentValues, "$COLUMN_STUDENTID = ?", arrayOf(id))
            result = true
        } catch (e:Exception) {
            Log.e(ContentValues.TAG, "Error Updating")
            result = false
        }
        return true
    }

}