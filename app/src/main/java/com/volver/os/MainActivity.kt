
package com.volver.os

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.*

class MainActivity : Activity() {

    private val bg = Color.rgb(10, 13, 22)
    private val accent = Color.rgb(112, 145, 255)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        createHome()
    }

    private fun createHome() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(42, 28, 42, 25)
            setBackgroundColor(bg)
        }

        val header = TextView(this).apply {
            text = "VOLVER OS"
            textSize = 30f
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
        }
        root.addView(header)

        val welcome = TextView(this).apply {
            text = "Добро пожаловать домой"
            textSize = 29f
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
        }

        root.addView(welcome,
            LinearLayout.LayoutParams(-1, 110).apply {
                topMargin = 25
            })

        val subtitle = TextView(this).apply {
            text = "Твой телевизор. Твои развлечения."
            textSize = 17f
            setTextColor(Color.LTGRAY)
        }
        root.addView(subtitle)

        root.addView(TextView(this).apply {
            text = "ТВОИ ПРИЛОЖЕНИЯ"
            textSize = 16f
            setTextColor(accent)
            typeface = Typeface.DEFAULT_BOLD
        }, LinearLayout.LayoutParams(-1, 65))

        val scroll = HorizontalScrollView(this)
        val cards = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        addCard(cards, "▶", "YouTube") {
            openApp("com.google.android.youtube.tv",
                    "com.google.android.youtube")
        }

        addCard(cards, "◎", "Браузер") {
            openBrowser()
        }

        addCard(cards, "▦", "Volver Store") {
            showMessage("Volver Store скоро появится!")
        }

        addCard(cards, "♫", "Музыка") {
            showMessage("Музыкальный раздел скоро появится!")
        }

        addCard(cards, "⚙", "Настройки") {
            startActivity(Intent(
                android.provider.Settings.ACTION_SETTINGS
            ))
        }

        scroll.addView(cards)
        root.addView(scroll)

        root.addView(TextView(this).apply {
            text = "VOLVER OS • VERSION 0.1 ALPHA"
            textSize = 12f
            setTextColor(Color.GRAY)
        }, LinearLayout.LayoutParams(-1, 65))

        setContentView(root)
        cards.getChildAt(0)?.requestFocus()
    }

    private fun addCard(
        parent: LinearLayout,
        icon: String,
        title: String,
        action: () -> Unit
    ) {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            isFocusable = true
            isClickable = true
            background = rounded(
                Color.rgb(32, 40, 58), 22
            )
            setOnClickListener { action() }
        }

        card.addView(TextView(this).apply {
            text = icon
            textSize = 38f
            setTextColor(accent)
            gravity = Gravity.CENTER
        })

        card.addView(TextView(this).apply {
            text = title
            textSize = 17f
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
        })

        card.onFocusChangeListener =
            View.OnFocusChangeListener { view, focused ->
                view.background = rounded(
                    if (focused)
                        Color.rgb(60, 83, 135)
                    else
                        Color.rgb(32, 40, 58), 22
                )
                view.animate()
                    .scaleX(if (focused) 1.06f else 1f)
                    .scaleY(if (focused) 1.06f else 1f)
                    .setDuration(150)
                    .start()
            }

        parent.addView(card,
            LinearLayout.LayoutParams(190, 150).apply {
                marginEnd = 16
            })
    }

    private fun rounded(color: Int, radius: Int) =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius.toFloat()
        }

    private fun openApp(vararg packages: String) {
        for (pkg in packages) {
            val intent = packageManager
                .getLaunchIntentForPackage(pkg)
            if (intent != null) {
                startActivity(intent)
                return
            }
        }
        openBrowser()
    }

    private fun openBrowser() {
        try {
            startActivity(Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com")
            ))
        } catch (e: Exception) {
            showMessage("Браузер не найден")
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            this, message, Toast.LENGTH_LONG
        ).show()
    }
}
