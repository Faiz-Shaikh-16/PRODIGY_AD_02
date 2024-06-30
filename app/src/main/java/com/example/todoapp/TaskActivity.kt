package com.example.todoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.lifecycle.lifecycleScope
import com.example.todoapp.databinding.ActivityTaskBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

const val DB_NAME = "todo.db"

class TaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var b: ActivityTaskBinding

    lateinit var myCalendar: Calendar
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    private val labels = arrayListOf("Personal", "Business", "Insurance", "Shopping", "Banking","Health","Work","Travel")

    val db by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.etDate.setOnClickListener(this)
        b.etTime.setOnClickListener(this)
        b.btnSave.setOnClickListener(this)

        setUpSpinner()
    }

    private fun setUpSpinner() {
        val adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, labels)

        labels.sort()

        b.spCategory.adapter = adapter
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.etDate -> {
                setListener()
            }

            R.id.etTime -> {
                setTimeListener()
            }

            R.id.btnSave -> {
                saveTask()
            }
        }
    }

    private fun saveTask() {
        val title = b.etTaskTitle.text.toString()
        val description = b.etTask.text.toString()
        val date = b.etDate.text.toString()
        val time = b.etTime.text.toString()
        val category = b.spCategory.selectedItem.toString()

        if (title.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()) {
            val task = TodoModel(
                title = title,
                description = description,
                date = date,
                time = time,
                category = category
            )

            lifecycleScope.launch {
                val taskId = db.todoDao().insertTask(task)
                Log.d("TaskInsertion", "Task inserted: $task with ID: $taskId")
                finish()
            }
        } else {
            Log.d("TaskNotInserted","failed to insert the tasks")
        }
    }


    private fun setTimeListener() {
        myCalendar = Calendar.getInstance()

        timeSetListener =
            TimePickerDialog.OnTimeSetListener() { _: TimePicker, hourOfDay: Int, minute: Int ->
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                myCalendar.set(Calendar.MINUTE, minute)
                updateTime()
            }

        val timePickerDialog = TimePickerDialog(
            this,
            timeSetListener,
            myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE),
            false
        )

        timePickerDialog.show()
    }

    private fun updateTime() {
        //Sat, 15 Jun 2003
        val myFormat = "h:mm a"
        val sdf = SimpleDateFormat(myFormat)
        b.etTime.setText(sdf.format(myCalendar.time))

    }

    private fun setListener() {
        myCalendar = Calendar.getInstance()

        dateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDate()
            }

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {
        //Sat, 15 Jun 2003
        val myFormat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myFormat)
        b.etDate.setText(sdf.format(myCalendar.time))

        b.timeInpLay.visibility = View.VISIBLE
    }
}