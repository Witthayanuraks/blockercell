package com.android.blocker

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LogActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_NAME = "CallLogsPrefs"
        private const val KEY_LOGS = "logs"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CallLogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        Log.d("LogActivity", "LogActivity created")

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerView) ?: run {
            Log.e("LogActivity", "RecyclerView not found!")
            Toast.makeText(this, "RecyclerView not found!", Toast.LENGTH_LONG).show()
            return
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CallLogAdapter(getLogsFromPrefs())
        recyclerView.adapter = adapter
        Log.d("LogActivity", "RecyclerView initialized")

        // Inisialisasi tombol Hapus Log
        val clearButton = findViewById<Button>(R.id.btnClearLogs)
        if (clearButton == null) {
            Log.e("LogActivity", "Button btnClearLogs not found!")
            Toast.makeText(this, "Button not found!", Toast.LENGTH_LONG).show()
            return
        }

        clearButton.setOnClickListener {
            clearLogs()
            updateUI()
            Toast.makeText(this, "Logs cleared", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
        Log.d("LogActivity", "Resumed, logs count: ${getLogsFromPrefs().size}")
    }

    private fun getLogsFromPrefs(): List<String> {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val logs = prefs.getStringSet(KEY_LOGS, emptySet())?.toList() ?: emptyList()
        Log.d("LogActivity", "Fetched logs: $logs")
        return logs
    }

    private fun clearLogs() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
        Log.d("LogActivity", "Logs cleared")
    }

    private fun updateUI() {
        val logs = getLogsFromPrefs()
        adapter.updateLogs(logs)
        Log.d("LogActivity", "UI updated with ${logs.size} logs")
    }
}