package com.example.constraintlayout

import android.content.Intent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() , TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private lateinit var edtPessoas: EditText
    private lateinit var txtResultado: TextView
    private lateinit var shareContent: FloatingActionButton


    private var ttsSucess: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //define o layout da tela
        edtConta = findViewById<EditText>(R.id.edtConta) //armazena o valor nessa variável
        edtConta.addTextChangedListener(this) //
        edtPessoas = findViewById(R.id.edtPessoas)
        edtPessoas.addTextChangedListener(this)
        txtResultado = findViewById(R.id.resultado)
        tts = TextToSpeech(this, this)
        shareContent = findViewById(R.id.btnShare)


    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
       Log.d("PDM24","Antes de mudar")

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PDM24","Mudando")
    }

    override fun afterTextChanged(s: Editable?) {
        Log.d("PDM24", "Depois de mudar")

        calcularValor()
    }

    fun clickFalar(v: View){

        val resultadoTexto = txtResultado.text.toString()

        if (tts.isSpeaking) {
            tts.stop()
        }
        if(ttsSucess) {
            Log.d ("PDM23", tts.language.toString())
            tts.speak(resultadoTexto, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun compartilhar(v: View) {
        val resultadoTexto = txtResultado.text.toString()

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, resultadoTexto)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Compartilhar via")
        startActivity(shareIntent)
    }

    fun calcularValor() {
        try {
            val conta = edtConta.text.toString().toDouble()
            val pessoas = edtPessoas.text.toString().toDouble()

            if (pessoas > 0) {
                val resultado = conta / pessoas
                txtResultado.text = "O valor para cada é: %.2f".format(resultado)
            } else {
                txtResultado.text = "Número de pessoas deve ser maior que zero."
            }
        } catch (e: NumberFormatException) {
            txtResultado.text = "Por favor, insira valores válidos."
        }
    }

    override fun onDestroy() {
            // Release TTS engine resources
            tts.stop()
            tts.shutdown()
            super.onDestroy()
        }

    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // TTS engine is initialized successfully
                tts.language = Locale.getDefault()
                tts.setLanguage(Locale("pt", "BR"))
                ttsSucess=true
                Log.d("PDM23","Sucesso na Inicialização")
            } else {
                // TTS engine failed to initialize
                Log.e("PDM23", "Failed to initialize TTS engine.")
                ttsSucess=false
            }
        }


}

