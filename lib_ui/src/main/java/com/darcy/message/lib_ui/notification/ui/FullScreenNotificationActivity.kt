package com.darcy.message.lib_ui.notification.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class FullScreenNotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.darcy.message.lib_ui.R.layout.lib_ui_activity_full_screen_notification)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.darcy.message.lib_ui.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 处理通知内容
        val intent = intent
        val title = intent.getStringExtra("title")?: "default title"
        val message = intent.getStringExtra("message") ?: "default message"


        // 显示通知内容
        val titleTextView = findViewById<TextView>(com.darcy.message.lib_ui.R.id.tvTitle)
        val messageTextView = findViewById<TextView>(com.darcy.message.lib_ui.R.id.tvContent)
        titleTextView.text = title
        messageTextView.text = message
    }
}