package com.example.expensetrackerapp.components

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.expensetrackerapp.R
import com.example.expensetrackerapp.model.CategoryExpense
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetrackerapp.ui.theme.DarkAmber
import com.example.expensetrackerapp.ui.theme.DarkCrimson
import com.example.expensetrackerapp.ui.theme.DarkCyan
import com.example.expensetrackerapp.ui.theme.DarkDeepBlue
import com.example.expensetrackerapp.ui.theme.DarkDeepGreen
import com.example.expensetrackerapp.ui.theme.DarkIndigo
import com.example.expensetrackerapp.ui.theme.DarkLime
import com.example.expensetrackerapp.ui.theme.DarkMagenta

import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.*

@Composable
fun PieChart(
    getPieChartData: List<CategoryExpense>
) {
    val context = LocalContext.current
    val darkMode = isSystemInDarkTheme()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {

        Crossfade(targetState = getPieChartData, label = "") { pieChartData ->
            AndroidView(factory = { context ->
                PieChart(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    this.description.isEnabled = false
                    this.isDrawHoleEnabled = false
                    this.legend.isEnabled = true
                    this.legend.textSize = 14F
                    this.legend.horizontalAlignment =
                        Legend.LegendHorizontalAlignment.CENTER

                    ContextCompat.getColor(context, R.color.white)
                }
            },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(2.dp), update = {
                    updatePieChartWithData(it, pieChartData, context, darkMode)
                })
        }
    }
}

fun updatePieChartWithData(
    chart: PieChart,
    data: List<CategoryExpense>,
    context: Context,
    darkMode: Boolean
) {
    val entries = ArrayList<PieEntry>()
    for (i in data.indices) {
        val item = data[i]
        if(item.totalExpense==0.0f) continue
        entries.add(PieEntry(item.totalExpense ?: 0.toFloat(), item.category ?: ""))
    }
    val ds = PieDataSet(entries, "")
    ds.colors = arrayListOf(
        DarkMagenta.toArgb(),
        DarkAmber.toArgb(),
        DarkIndigo.toArgb(),
        DarkLime.toArgb(),
        DarkDeepBlue.toArgb(),
        DarkDeepGreen.toArgb(),
        DarkCyan.toArgb(),
        DarkCrimson.toArgb(),
    )
    ds.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
    ds.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
    ds.sliceSpace = 2f
    ContextCompat.getColor(context, R.color.white)
    ds.valueTextSize = 18f
    ds.valueTypeface = Typeface.DEFAULT_BOLD
    ds.valueTextColor = Color.Black.toArgb()


    val d = PieData(ds)

    chart.data = d
    if(darkMode) {
        chart.legend.textColor = Color.White.toArgb()
    }
    else {
        chart.legend.textColor = Color.Black.toArgb()
    }
    chart.setEntryLabelColor(Color.Black.toArgb())
    chart.setEntryLabelTextSize(14f)
    chart.invalidate()
}
