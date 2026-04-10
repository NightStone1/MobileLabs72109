package ru.bibko.last4_2109

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: HabitDbHelper
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnLoad: Button
    private lateinit var btnDeleteAll: Button
    private lateinit var listViewHabits: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = HabitDbHelper(this)

        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        btnAdd = findViewById(R.id.btnAdd)
        btnLoad = findViewById(R.id.btnLoad)
        btnDeleteAll = findViewById(R.id.btnDeleteAll)
        listViewHabits = findViewById(R.id.listViewHabits)

        btnAdd.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                val createdAt = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
                    .format(Date())

                dbHelper.insertHabit(title, description, createdAt)
                Toast.makeText(this, "Запись добавлена", Toast.LENGTH_SHORT).show()

                etTitle.text.clear()
                etDescription.text.clear()
            }
        }

        btnLoad.setOnClickListener {
            loadHabits()
        }

        btnDeleteAll.setOnClickListener {
            dbHelper.deleteAllHabits()
            loadHabits()
            Toast.makeText(this, "Все записи удалены", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadHabits() {
        val habits = dbHelper.getAllHabitsSortedByTitle()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, habits)
        listViewHabits.adapter = adapter
    }
}