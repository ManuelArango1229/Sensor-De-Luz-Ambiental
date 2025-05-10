package com.miniproyecto.sensor_luz_ambiental

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class GraphActivity : AppCompatActivity() {

    private lateinit var chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        chart = findViewById(R.id.lightChart)

        val luzData = intent.getFloatArrayExtra("luz_data") ?: floatArrayOf()
        val entries = luzData.mapIndexed { index, value ->

            if (index % 10 == 0) Entry(index.toFloat(), value) else null
        }.filterNotNull()



        val dataSet = LineDataSet(entries, "Luz ambiental (lx)").apply {
            setDrawValues(false)
            setDrawCircles(true)
            circleRadius = 4f
            circleHoleRadius = 2f
            setCircleColor(getColor(R.color.circleColor))

            lineWidth = 2f
            color = getColor(R.color.lineColor)

            setDrawFilled(true)
            fillAlpha = 80
            fillColor = getColor(R.color.fillColor)

            mode = LineDataSet.Mode.CUBIC_BEZIER // curva suave
            valueTextSize = 12f
        }

        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.cubicIntensity = 0.2f


        chart.data = LineData(dataSet)
        chart.description.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.invalidate()
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.setScaleEnabled(true)
        chart.isDragEnabled = true
        chart.setVisibleXRangeMaximum(50f)
        chart.moveViewToX(0f)


        chart.apply {
            data = LineData(dataSet)

            description.isEnabled = false
            axisRight.isEnabled = false

            // Eje X
            xAxis.apply {
                granularity = 1f
                isGranularityEnabled = true
                position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textColor = getColor(R.color.black)
            }

            // Eje Y
            axisLeft.apply {
                textColor = getColor(R.color.black)
                setDrawGridLines(true)
                gridColor = getColor(R.color.purple_700)
            }

            legend.isEnabled = true
            legend.textColor = getColor(R.color.black)

            setTouchEnabled(true)
            setPinchZoom(true)
            animateX(1000)
            invalidate()
        }

    }
}
