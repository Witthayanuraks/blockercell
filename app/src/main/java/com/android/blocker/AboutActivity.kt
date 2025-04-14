package com.android.blocker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Mengisi informasi aplikasi
        val aboutInfo = findViewById<TextView>(R.id.about_info)
        val appInfo = """
            Version: 1.0
            Developer: Rehan
            
            Description:
            Call Blocker adalah aplikasi yang dirancang untuk memblokir panggilan dari nomor yang tidak dikenal secara otomatis. Aplikasi ini membantu menjaga privasi Anda dan mengurangi gangguan dari panggilan yang tidak diinginkan.
            
            Contact:
            Email: rehanrahmansaputra23@gmail.com
             
        """.trimIndent()
        aboutInfo.text = appInfo

        // Tombol kembali
        val btnBack = findViewById<MaterialButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish() // Menutup aktivitas dan kembali ke MainActivity
        }
    }
}