package com.example.myapplication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.severlapp_cenamad.repository.Repository
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

private fun proyectosPorLinea(): List<Pair<String, Int>> =
    Repository.lineas.map { linea ->
        val idsInvestigadores = Repository.getInvestigadoresByLinea(linea.id).map { it.id }
        val total = Repository.proyectos.count { it.idInvestigador in idsInvestigadores }
        // Etiqueta corta para que quepa en el eje X (ej. "Bioproductos")
        linea.titulo.substringBefore(" ") to total
    }

private fun proyectosPorEstado(): List<Pair<String, Int>> =
    Repository.proyectos
        .groupBy { it.estado }
        .map { (estado, lista) -> estado to lista.size }

private val formatoEntero = object : ValueFormatter() {
    override fun getFormattedValue(value: Float): String = value.toInt().toString()
}


@Composable
fun GraficoBarrasProyectosPorLinea(modifier: Modifier = Modifier) {
    val datos = remember { proyectosPorLinea() }
    val colorTexto = MaterialTheme.colorScheme.onSurface.toArgb()

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp),
        // factory: se ejecuta una sola vez; aquí va la configuración estática
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                axisRight.isEnabled = false
                setFitBars(true)
                animateY(800)
            }
        },

        update = { chart ->
            val entradas = datos.mapIndexed { i, (_, cantidad) ->
                BarEntry(i.toFloat(), cantidad.toFloat())
            }
            val dataSet = BarDataSet(entradas, "Proyectos").apply {
                colors = ColorTemplate.MATERIAL_COLORS.toList()
                valueTextSize = 12f
                valueTextColor = colorTexto
                valueFormatter = formatoEntero
            }
            chart.data = BarData(dataSet).apply { barWidth = 0.6f }

            chart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IndexAxisValueFormatter(datos.map { it.first })
                granularity = 1f
                labelCount = datos.size
                setDrawGridLines(false)
                textColor = colorTexto
            }
            chart.axisLeft.apply {
                axisMinimum = 0f
                granularity = 1f
                textColor = colorTexto
            }
            chart.invalidate()
        }
    )
}


@Composable
fun GraficoCircularProyectosPorEstado(modifier: Modifier = Modifier) {
    val datos = remember { proyectosPorEstado() }
    val colorTexto = MaterialTheme.colorScheme.onSurface.toArgb()

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false
                isDrawHoleEnabled = true
                setHoleColor(android.graphics.Color.TRANSPARENT)
                animateY(800)
            }
        },
        update = { chart ->
            val entradas = datos.map { (estado, cantidad) ->
                PieEntry(cantidad.toFloat(), estado)
            }
            val dataSet = PieDataSet(entradas, "").apply {
                colors = ColorTemplate.VORDIPLOM_COLORS.toList()
                sliceSpace = 3f
                valueTextSize = 14f
                valueTextColor = android.graphics.Color.WHITE
                valueFormatter = formatoEntero
            }
            chart.data = PieData(dataSet)
            chart.setEntryLabelColor(colorTexto)
            chart.legend.textColor = colorTexto
            chart.setCenterText("Estado")
            chart.setCenterTextColor(colorTexto)
            chart.invalidate()
        }
    )
}


@Composable
fun PantallaIndicadores(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Indicadores de investigación",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Proyectos por línea de investigación",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        GraficoBarrasProyectosPorLinea()

        Text(
            text = "Proyectos por estado",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )
        GraficoCircularProyectosPorEstado()
    }
}