package com.android.blocker

import android.Manifest
import android.annotation.SuppressLint
import android.app.role.RoleManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telecom.TelecomManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val PERMISSION_REQUEST_CODE = 100
        private const val ROLE_REQUEST_CODE = 101
        const val ACTION_CALL_LOG = "com.android.blocker.CALL_LOG"
        private const val PREFS_NAME = "CallLogsPrefs"
        private const val KEY_LOGS = "logs"
    }

    private val logReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "Broadcast received: ${intent?.action}")
            val logMessage = intent?.getStringExtra("log_message") ?: run {
                Log.w(TAG, "Log message is null")
                return
            }
            Log.d(TAG, "Received log: $logMessage")
            addLogToPrefs(logMessage)
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Meminta izin
        requestPermissions()

        // Meminta menjadi Default Phone App
        requestDefaultPhoneRole()

        // Inisialisasi tombol dengan MaterialButton
        val btnSetDefaultPhone = findViewById<MaterialButton>(R.id.btnSetDefaultPhone)
        val btnViewLogs = findViewById<MaterialButton>(R.id.btnViewLogs)
        val btnAbout = findViewById<MaterialButton>(R.id.btnAbout)

        // Listener untuk tombol "Set as Default Phone App"
        btnSetDefaultPhone.setOnClickListener {
            openDefaultAppsSettings()
        }

        // Listener untuk tombol "View Logs"
        btnViewLogs.setOnClickListener {
            val intent = Intent(this, LogActivity::class.java)
            startActivity(intent)
        }

        // Listener untuk tombol "About"
        btnAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        // Daftarkan BroadcastReceiver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(logReceiver, IntentFilter(ACTION_CALL_LOG), RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(logReceiver, IntentFilter(ACTION_CALL_LOG))
        }
        Log.d(TAG, "BroadcastReceiver registered")
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(logReceiver)
        Log.d(TAG, "BroadcastReceiver unregistered")
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ANSWER_PHONE_CALLS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS
        )

        if (!hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
        } else {
            Log.d(TAG, "All permissions granted")
            Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestDefaultPhoneRole() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val roleManager = getSystemService(RoleManager::class.java)
                if (roleManager?.isRoleAvailable(RoleManager.ROLE_DIALER) == true &&
                    !roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
                    Log.d(TAG, "Requesting Default Phone App role")
                    val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                    startActivityForResult(intent, ROLE_REQUEST_CODE)
                } else if (roleManager?.isRoleHeld(RoleManager.ROLE_DIALER) == true) {
                    Log.d(TAG, "Already the default phone app")
                    Toast.makeText(this, "Already the default phone app", Toast.LENGTH_SHORT).show()
                }
            } else {
                val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
                if (telecomManager.defaultDialerPackage != packageName) {
                    Log.d(TAG, "Requesting Default Phone App role (pre-Q)")
                    val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                        .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
                    startActivityForResult(intent, ROLE_REQUEST_CODE)
                } else {
                    Log.d(TAG, "Already the default phone app")
                    Toast.makeText(this, "Already the default phone app", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error requesting default phone role: ${e.message}")
            Toast.makeText(this, "Error setting phone app: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openDefaultAppsSettings() {
        try {
            val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
            } else {
                Intent(Settings.ACTION_APPLICATION_SETTINGS)
            }
            startActivity(intent)
            Toast.makeText(this, "Select 'com.android.blocker' as Default Phone App", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e(TAG, "Error opening default apps settings: ${e.message}")
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ROLE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Successfully set as default phone app")
                Toast.makeText(this, "Set as default phone app", Toast.LENGTH_SHORT).show()
            } else {
                Log.w(TAG, "Failed to set as default phone app, resultCode: $resultCode")
                Toast.makeText(this, "Please set as default phone app to block calls", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Log.d(TAG, "Permissions granted")
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
            } else {
                Log.w(TAG, "Some permissions denied")
                Toast.makeText(this, "Some permissions denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addLogToPrefs(logMessage: String) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = prefs.edit()
        val logs = prefs.getStringSet(KEY_LOGS, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        val logEntry = "${getCurrentTime()} - $logMessage"
        logs.add(logEntry)
        editor.putStringSet(KEY_LOGS, logs)
        editor.apply()
        Log.d(TAG, "Log saved to prefs: $logEntry")
        Log.d(TAG, "Total logs in prefs: ${logs.size}")
    }

    private fun getCurrentTime(): String {
        return android.text.format.DateFormat.format("dd/MM/yyyy HH:mm:ss", System.currentTimeMillis()).toString()
    }
}