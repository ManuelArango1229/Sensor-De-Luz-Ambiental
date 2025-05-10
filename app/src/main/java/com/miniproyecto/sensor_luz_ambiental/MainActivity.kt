package com.miniproyecto.sensor_luz_ambiental

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatDelegate
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var brillo: Sensor? = null
    private lateinit var texto: TextView
    private lateinit var pb: CircularProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        texto = findViewById(R.id.tv_text)
        pb = findViewById(R.id.circularProgressBar)

        setUpSensorStuff()
    }

    private fun setUpSensorStuff() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        brillo = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_LIGHT){
            val luz = event.values[0]

            texto.text = "Intesidad de la Luz: \n$luz\n\n${obtenerBrillo(luz)}"
        }
    }

    private fun obtenerBrillo(brillo: Float): String{
        return when(brillo.toInt()){
            0 -> "Oscuridad Total"
            in 1 .. 10 -> "Oscuro"
            in 11 .. 50 -> "Poco Oscuro"
            in 51 .. 1000 -> "Normal"
            in 1001 .. 5000 -> "Increiblemente Brillante"
            else -> "Esta Luz Te Segara"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, brillo, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }
}
