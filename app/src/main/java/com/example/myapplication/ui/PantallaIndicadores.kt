package com.example.myapplication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.IndicadoresViewModel
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

private val formatoEntero = object : ValueFormatter() {
    override fun getFormattedValue(value: Float): String = value.toInt().toString()
}


@Composable
fun GraficoBarrasProyectosPorLinea(
    datos: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
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
fun GraficoCircularProyectosPorEstado(
    datos: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
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
fun PantallaIndicadores(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: IndicadoresViewModel = viewModel()
    val uiState = viewModel.uiState

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("← Volver")
        }

        Text(
            text = "Indicadores de investigación",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Proyectos por línea de investigación",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        GraficoBarrasProyectosPorLinea(uiState.proyectosPorLinea)

        Text(
            text = "Proyectos por estado",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )
        GraficoCircularProyectosPorEstado(uiState.proyectosPorEstado)
    }
}