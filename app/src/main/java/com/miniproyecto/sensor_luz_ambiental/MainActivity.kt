package com.miniproyecto.sensor_luz_ambiental

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatDelegate
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var brillo: Sensor? = null
    private lateinit var texto: TextView
    private lateinit var pb: CircularProgressBar
    private lateinit var gestureDetector: GestureDetector
    private var medicionIniciada = false
    private lateinit var luzArray: ArrayList<Float>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        luzArray = arrayListOf()

        findViewById<Button>(R.id.btn_view_graph).setOnClickListener {
            val intent = Intent(this, GraphActivity::class.java)
            intent.putExtra("luz_data", luzArray.toFloatArray())
            startActivity(intent)
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        texto = findViewById(R.id.tv_text)
        pb = findViewById(R.id.circularProgressBar)

        setUpSensorStuff()

        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                if (!medicionIniciada) {
                    medicionIniciada = true
                    texto.text = "Midiendo luz..."
                    sensorManager.registerListener(this@MainActivity, brillo, SensorManager.SENSOR_DELAY_NORMAL)
                }
                return true
            }
        })
    }

    private fun setUpSensorStuff() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        brillo = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val luz = event.values[0]
            luzArray.add(luz)
            texto.text = "Intensidad de la Luz: \n$luz\n\n${obtenerBrillo(luz)}"
            pb.setProgressWithAnimation(luz)
        }
    }

    private fun obtenerBrillo(brillo: Float): String {
        return when (brillo.toInt()) {
            0 -> "Oscuridad Total"
            in 1..10 -> "Oscuro"
            in 11..50 -> "Poco Oscuro"
            in 51..1000 -> "Normal"
            in 1001..5000 -> "Increíblemente Brillante"
            else -> "Esta Luz Te Cegará"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No es necesario implementar nada aquí
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        medicionIniciada = false
        texto.text = "Toca la pantalla para comenzar"
        pb.setProgressWithAnimation(0f)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            gestureDetector.onTouchEvent(it)
        }
        return super.onTouchEvent(event)
    }
}
