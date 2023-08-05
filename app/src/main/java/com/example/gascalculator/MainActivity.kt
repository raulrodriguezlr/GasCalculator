package com.example.gascalculator


import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


// Importa el color definido en color.kt


class MainActivity : ComponentActivity() {

    private lateinit var buttonClick: Button
    private lateinit var textMessage: TextView
    private lateinit var pasajeros: Spinner
    private lateinit var kmHechos: EditText
    private lateinit var precioGas: EditText
    private lateinit var consumo: EditText
    private lateinit var adView: AdView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonClick = findViewById(R.id.button)
        textMessage = findViewById(R.id.textView9)
        pasajeros = findViewById(R.id.spinner)
        kmHechos = findViewById(R.id.editTextNumber)
        precioGas= findViewById(R.id.editTextNumberDecimal)
        consumo= findViewById(R.id.editTextNumberDecimal2)
        MobileAds.initialize(this) {}

        adView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)


        /*
        // En tu función onCreate u otra función adecuada
        buttonClick.setOnLongClickListener {
            // Código a ejecutar cuando se realice una pulsación larga en el botón
            textMessage.setTextColor(Pink40.toArgb())
            true // Devuelve 'true' para indicar que el evento se ha consumido
        }*/
        val opciones = listOf("1 pasajero", "2 pasajeros", "3 pasajeros","4 pasajeros","5 pasajeros","6 pasajeros","7 pasajeros","8 pasajeros","9 pasajeros" ) // Agrega las opciones que desees
        showSpinner(opciones)


        buttonClick.setOnClickListener {
            val resultado=recogerdatos()
            if (resultado!=null){

                val resultadoFormateado = String.format("%.2f", resultado)

                textMessage.text = "Cada persona tiene que pagar: %.2f €".format(resultado)

            }

        }
    }
    private fun recogerdatos(): Float? {
        val kmRealizados = kmHechos.text.toString().toIntOrNull()
        val precioGasolina=precioGas.text.toString().toFloatOrNull()
        val coste= consumo.text.toString().toFloatOrNull()

        // Verificar si el valor ingresado es numérico o no
        if (kmRealizados != null&&precioGasolina != null&&coste != null) {
            var pasajerosSeleccionados =0
            when(pasajeros.selectedItem.toString()){
                "1 pasajero"->pasajerosSeleccionados=1
                "2 pasajeros"-> pasajerosSeleccionados=2
                "3 pasajeros"-> pasajerosSeleccionados=3
                "4 pasajeros"-> pasajerosSeleccionados=4
                "5 pasajeros"-> pasajerosSeleccionados=5
                "6 pasajeros"-> pasajerosSeleccionados=6
                "7 pasajeros"-> pasajerosSeleccionados=7
                "8 pasajeros"-> pasajerosSeleccionados=8
                "9 pasajeros"-> pasajerosSeleccionados=9
            }


            return calculo(kmRealizados,pasajerosSeleccionados, precioGasolina,coste)

        } else {
            // El valor ingresado no es un número válido
            showAlertDialog("Error", "Por favor, introduzca todos los valores.")
            return null
        }


    }
    private fun calculo(km:Int,pasajeros:Int,precio:Float,consumo:Float):Float{
        val litrosPorKm=  (consumo * km)/100
        val precioTotal= litrosPorKm*precio
        val precioPersona=precioTotal/pasajeros
        return precioPersona
    }
    private fun showAlertDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK", null)
        builder.show()
    }
    fun TextView.setTextColorInt(colorInt: Int) {
        setTextColor(colorInt)
    }

    private fun showSpinner(opciones: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        pasajeros.adapter = adapter

        // Aplica el estilo personalizado al adaptador
        pasajeros.adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.setTextAppearance(R.style.SpinnerItemStyle) // Aquí aplicamos el estilo
                return view
            }
            /*
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val textView = view.findViewById<TextView>(android.R.id.text1)
                    textView.setTextAppearance(R.style.SpinnerItemStyle) // Aquí aplicamos el estilo también para las opciones desplegables
                    return view
                }*/
        }
    }



    override fun onPause() {
        adView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }

    /*
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                AprendizajeTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Greeting("Android")
                    }
                }
            }
        }*/
}





