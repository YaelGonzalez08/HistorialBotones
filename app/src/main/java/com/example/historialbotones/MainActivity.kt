package com.example.historialbotones

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var buttonHistoryRepository: ButtonHistoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonHistoryRepository = ButtonHistoryRepository(this)

        val boton1 = findViewById<Button>(R.id.botonA)
        val boton2 = findViewById<Button>(R.id.botonB)
        val boton3 = findViewById<Button>(R.id.botonC)
        val boton4 = findViewById<Button>(R.id.botonD)
        val boton5 = findViewById<Button>(R.id.botonF)


        boton1.setOnClickListener {
            saveButtonPressedAndShowHistory("\nBoton 1 ")
        }

        boton2.setOnClickListener {
            saveButtonPressedAndShowHistory("\nBoton 2 ")
        }
        boton3.setOnClickListener {
            saveButtonPressedAndShowHistory("\nBoton 3 ")
        }
        boton4.setOnClickListener {
            saveButtonPressedAndShowHistory("\nBoton 4 ")
        }
        boton5.setOnClickListener {
            saveButtonPressedAndShowHistory("\nBoton 5 ")
        }

        // Mostrar el historial actual
        showButtonHistory()
    }

    private fun saveButtonPressedAndShowHistory(buttonName: String) {
        GlobalScope.launch(Dispatchers.IO) {
            buttonHistoryRepository.saveButtonPressed(buttonName)
            showButtonHistory()
        }
    }

    private fun showButtonHistory() {
        val historyTextView = findViewById<TextView>(R.id.historyTextView)
        buttonHistoryRepository.getButtonHistory().onEach { history ->
            runOnUiThread {

                historyTextView.text = "Historial de botones presionados:\n$history"
            }
        }.launchIn(GlobalScope)
    }
}
val Context.dataStore by preferencesDataStore(name = "button_history")

class ButtonHistoryRepository(context: Context) {
    private val dataStore = context.dataStore
    private val BUTTON_HISTORY_KEY = stringPreferencesKey("button_history_key")

    suspend fun saveButtonPressed(buttonName: String) {
        dataStore.edit { preferences ->
            val currentHistory = preferences[BUTTON_HISTORY_KEY] ?: ""
            val historyList = currentHistory.split(",").toMutableList()
            historyList.add(0, buttonName)
            if (historyList.size > 4) {
                historyList.removeAt(4)
            }

            val updatedHistory = historyList.joinToString(separator = ",")
            preferences[BUTTON_HISTORY_KEY] = updatedHistory
        }
    }

    fun getButtonHistory(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[BUTTON_HISTORY_KEY] ?: ""
        }
    }

}
