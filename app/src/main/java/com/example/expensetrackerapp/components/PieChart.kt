package com.example.expensetrackerapp.components

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.expensetrackerapp.ui.theme.DarkAmber
import com.example.expensetrackerapp.ui.theme.DarkDeepBlue
import com.example.expensetrackerapp.ui.theme.DarkDeepGreen
import com.example.expensetrackerapp.ui.theme.DarkIndigo

import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.*


//val getPieChartData = listOf(
//    CategoryExpense("Food", 34.68F),
//    CategoryExpense("Transport", 16.60F),
//    CategoryExpense("Aise hi", 16.15F),
//    CategoryExpense("Woh", 15.62F),
//)
@Composable
fun PieChart(
    getPieChartData: List<CategoryExpense>
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Expenses by categories",
                style = TextStyle.Default,
                fontFamily = FontFamily.Default,
                fontStyle = FontStyle.Normal,
                fontSize = 20.sp
            )
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .size(320.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
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
                            .padding(5.dp), update = {
                            updatePieChartWithData(it, pieChartData, context )
                        })
                }
            }
        }
    }
}

fun updatePieChartWithData(
    chart: PieChart,
    data: List<CategoryExpense>,
    context: Context
) {
    val entries = ArrayList<PieEntry>()
    for (i in data.indices) {
        val item = data[i]
        entries.add(PieEntry(item.totalExpense ?: 0.toFloat(), item.category ?: ""))
    }
    val ds = PieDataSet(entries, "")
    ds.colors = arrayListOf(
        DarkDeepBlue.toArgb(),
        DarkAmber.toArgb(),
        DarkDeepGreen.toArgb(),
        DarkIndigo.toArgb(),
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
    chart.legend.textColor = Color.White.toArgb()
    chart.setEntryLabelColor(Color.Black.toArgb())
    chart.invalidate()
}
