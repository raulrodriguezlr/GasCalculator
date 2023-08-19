package com.example.gascalculator


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


// Importa el color definido en color.kt


class MainActivity : ComponentActivity() {

    private lateinit var buttonClick:Button
    private lateinit var textMessage: TextView
    private lateinit var pasajeros: Spinner
    private lateinit var kmHechos: EditText
    private lateinit var precioGas: EditText
    private lateinit var consumo: EditText
    private lateinit var adView: AdView
    private lateinit var ventanas:CheckBox
    private lateinit var aire:CheckBox
    private lateinit var infoWindow:ImageView
    private lateinit var infoAir:ImageView
    private lateinit var toolTipText:TextView



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonClick = findViewById(R.id.button)
        textMessage = findViewById(R.id.textView9)
        pasajeros = findViewById(R.id.spinner)
        kmHechos = findViewById(R.id.editTextNumber)
        precioGas= findViewById(R.id.editTextNumberDecimal)
        consumo= findViewById(R.id.editTextNumberDecimal2)

        infoWindow = findViewById<ImageView>(R.id.infoWindow)
        infoAir = findViewById<ImageView>(R.id.infoAir)


        val rootView = window.decorView.rootView as FrameLayout
        val overlayView = layoutInflater.inflate(R.layout.tooltip, null)
        toolTipText = overlayView.findViewById<TextView>(R.id.toolTipText)
        windowInfo(rootView,overlayView)

        //val inflater = LayoutInflater.from(this)
        //val overlayView = inflater.inflate(R.layout.tooltip, rootView, false)




        //inicializador de google ads
            MobileAds.initialize(this) {}
            adView = findViewById(R.id.adView)
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)

        ventanas = findViewById<CheckBox>(R.id.ventanas)
        aire = findViewById<CheckBox>(R.id.aire)

        val opciones = listOf("1 pasajero", "2 pasajeros", "3 pasajeros","4 pasajeros","5 pasajeros","6 pasajeros","7 pasajeros","8 pasajeros","9 pasajeros" ) // Agrega las opciones que desees
        showSpinner(opciones)
        var multiplier:Float =1.0f

        ventanas.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                multiplier = 1.06f
            }
            else{
                multiplier = 1.0f

            }
        }
        aire.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                multiplier = 1.1f
            }
            else{
                multiplier = 1.0f

            }
        }

        buttonClick.setOnClickListener {
            val resultado= recogerdatos()?.times(multiplier)
            if (resultado!=null){

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
    private fun windowInfo(rootView:ViewGroup,overlayView:View){
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        // Calcula la posición para centrar la superposición en el icono
        //layoutParams.leftMargin = infoWindow.left + infoWindow.width / 2 - overlayView.width / 2
        //layoutParams.topMargin = infoWindow.top + infoWindow.height / 2 - overlayView.height / 2

        overlayView.translationX= (infoWindow.left + infoWindow.width / 2 - overlayView.width / 2).toFloat()
        overlayView.translationY= (infoWindow.top + infoWindow.height / 2 - overlayView.height / 2).toFloat()



        rootView.addView(overlayView)


        // Configurar el OnClickListener para el icono de información
        infoWindow.setOnClickListener {
            toolTipText.text = "Ventana"
            if (overlayView.parent == null) {
                rootView.addView(overlayView)
            }
        }
        // Configurar el OnClickListener para cerrar la superposición
        overlayView.setOnClickListener {
            rootView.removeView(overlayView)
        }


    }
    private fun airInfo(rootView:ViewGroup,overlayView:View){
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,

        )

        // Calcula la posición para centrar la superposición en el icono
        layoutParams.leftMargin = infoAir.left + infoAir.width / 2 - overlayView.width / 2
        layoutParams.topMargin = infoAir.top + infoAir.height / 2 - overlayView.height / 2
        overlayView.layoutParams = layoutParams
        rootView.addView(overlayView)


        // Configurar el OnClickListener para el icono de información
        infoAir.setOnClickListener {
            toolTipText.text = "Aire"

            if (overlayView.parent == null) {
                rootView.addView(overlayView)
            }
        }

        // Configurar el OnClickListener para cerrar la superposición
        overlayView.setOnClickListener {
            rootView.removeView(overlayView)
        }


    }



    private fun calculo(km:Int,pasajeros:Int,precio:Float,consumo:Float):Float{
        val litrosPorKm=  (consumo * km)/100
        val precioTotal= litrosPorKm*precio

        return precioTotal/pasajeros
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


}





