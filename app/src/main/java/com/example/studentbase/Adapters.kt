package com.example.studentbase

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.lo_student_update.view.*
import kotlinx.android.synthetic.main.lo_students.view.*

class StudentAdapter(mCtx: Context, var students : ArrayList<Student>) : RecyclerView.Adapter<StudentAdapter.ViewHolder>(){

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txtStudentName = itemView.txtStudentName
        val txtCredit = itemView.txtCredit
        val btnUpdatde = itemView.btnUpdate
        val btnDelete = itemView.btnDelete
    }

    private val mCtx = mCtx

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentAdapter.ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(R.layout.lo_students, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        return students.size
    }

    override fun onBindViewHolder(holder: StudentAdapter.ViewHolder, position: Int) {
        val student : Student = students[position]
        holder.txtStudentName.text = student.studentName
        holder.txtCredit.text = student.credit.toString()

        holder.btnDelete.setOnClickListener {
            val studentName = student.studentName

            var alertDialog = AlertDialog.Builder(mCtx)
                .setTitle("Warning")
                .setMessage("Are you sure to Delete : $studentName ?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    if (MainActivity.dbHandler.deleteStudent(student.studentID)){
                        students.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, students.size)
                        Toast.makeText(mCtx, "Student $studentName", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(mCtx, "Error Deleting", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->  })
                .setIcon(R.drawable.ic_baseline_warning_24)
                .show()
        }

        holder.btnUpdatde.setOnClickListener {
            val inflater = LayoutInflater.from(mCtx)
            val view = inflater.inflate(R.layout.lo_student_update, null)

            val txtStudName : TextView = view.findViewById(R.id.editUpStudentName)
            val txtCredit : TextView = view.findViewById(R.id.editUpCredit)

            txtStudName.text = student.studentName
            txtCredit.text = student.credit.toString()

            val builder = AlertDialog.Builder(mCtx)
                .setTitle("Update Student Info")
                .setView(view)
                .setPositiveButton("Update", DialogInterface.OnClickListener { dialog, which ->
                    val isUpdate = MainActivity.dbHandler.updateStudent(
                        student.studentID.toString(),
                        view.editUpStudentName.text.toString(),
                        view.editUpCredit.text.toString())
                    if (isUpdate){
                        students[position].studentName = view.editUpStudentName.text.toString()
                        students[position].credit = view.editUpCredit.text.toString().toDouble()
                        notifyDataSetChanged()
                        Toast.makeText(mCtx, "Updated Successfull", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(mCtx, "Error Updating", Toast.LENGTH_SHORT).show()
                    }
                }).setNegativeButton("Calcel", DialogInterface.OnClickListener { dialog, which ->

                })
            val alert = builder.create()
            alert.show()
        }

    }

}