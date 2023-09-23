package com.rasel.moviedb.ui.dashboard.view.fragment

import android.content.Context
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.opencsv.CSVReader
import com.rasel.moviedb.R
import com.rasel.moviedb.data.preference.AppPrefsManager
import com.rasel.moviedb.databinding.FragmentGraphBinding
import com.rasel.moviedb.ui.dashboard.view_model.HomeViewModel
import com.rasel.moviedb.utils.NetworkChangeReceiver
import com.rasel.moviedb.utils.changeStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject


@AndroidEntryPoint
class GraphFragment : Fragment(), NetworkChangeReceiver.ConnectionChangeCallback {

    private lateinit var mContext: Context
    private lateinit var binding: FragmentGraphBinding
    private lateinit var networkChangeReceiver: NetworkChangeReceiver


    @Inject
    lateinit var preferences: AppPrefsManager

    private val viewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.changeStatusBarColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorSecondary
            ), true
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGraphBinding.inflate(inflater, container, false)

        //network change callback to track network state
        networkChangeReceiver = NetworkChangeReceiver()
        networkChangeReceiver.setConnectionChangeCallback(this)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chart = binding.spikeChart

        // Configure the chart
        chart.setDrawGridBackground(false)
        chart.description.isEnabled = false
        chart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        chart.axisRight.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Read data from CSV and add it to the chart
        val data = readDataFromCSV()
        if (data.isNotEmpty()) {
            val entries1 = ArrayList<Entry>()
            val entries2 = ArrayList<Entry>()
            val entries3 = ArrayList<Entry>()
            val entries4 = ArrayList<Entry>()
            val entries5 = ArrayList<Entry>()

            for ((index, rowData) in data.withIndex()) {
                if (index == 0) continue
                val values = rowData.split(",") // Split the row into columns
                if (values.size >= 3) {
                    val date = values[0].trim()
                    val value1 = values[1].trim().toFloat()
                    val value2 = values[2].trim().toFloat()
                    val value3 = values[3].trim().toFloat()
                    val value4 = values[4].trim().toFloat()
                    val value5 = values[5].trim().toFloat()
                    entries1.add(Entry(index.toFloat(), value1))
                    entries2.add(Entry(index.toFloat(), value2))
                    entries3.add(Entry(index.toFloat(), value3))
                    entries4.add(Entry(index.toFloat(), value4))
                    entries5.add(Entry(index.toFloat(), value5))
                }
            }

            val dataSet1 = LineDataSet(entries1, "Open")
            dataSet1.color = Color.RED
            dataSet1.lineWidth = 2f
            dataSet1.setCircleColor(Color.RED)
            dataSet1.setDrawCircleHole(false)
            dataSet1.setDrawCircles(true)

            val dataSet2 = LineDataSet(entries2, "High")
            dataSet2.color = Color.BLUE
            dataSet2.lineWidth = 2f
            dataSet2.setCircleColor(Color.BLUE)
            dataSet2.setDrawCircleHole(false)
            dataSet2.setDrawCircles(true)

            val dataSet3 = LineDataSet(entries3, "Low")
            dataSet2.color = Color.YELLOW
            dataSet2.lineWidth = 2f
            dataSet2.setCircleColor(Color.YELLOW)
            dataSet2.setDrawCircleHole(false)
            dataSet2.setDrawCircles(true)

            val dataSet4 = LineDataSet(entries4, "Close")
            dataSet2.color = Color.GREEN
            dataSet2.lineWidth = 2f
            dataSet2.setCircleColor(Color.GREEN)
            dataSet2.setDrawCircleHole(false)
            dataSet2.setDrawCircles(true)

            val lineData = LineData(dataSet1, dataSet2, dataSet3, dataSet4)
            chart.data = lineData
            chart.invalidate()
        }
    }

    private fun readDataFromCSV(): List<String> {
        val data = ArrayList<String>()
        try {
            val inputStream =
                resources.openRawResource(R.raw.stock) // Place your CSV file in the res/raw folder
            val reader = BufferedReader(InputStreamReader(inputStream))
            val csvReader = CSVReader(reader)
            var nextLine: Array<String>?
            while (csvReader.readNext().also { nextLine = it } != null) {
                nextLine?.joinToString(separator = ",")?.let {
                    data.add(it)
                }
            }
            csvReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }

    override fun onStart() {
        super.onStart()
        mContext.registerReceiver(
            networkChangeReceiver,
            IntentFilter(NetworkChangeReceiver.ACTION_CONNECTIVITY_CHANGE)
        )
    }

    override fun onStop() {
        mContext.unregisterReceiver(networkChangeReceiver)
        super.onStop()
    }

    companion object {
        @JvmStatic
        fun newInstance(): GraphFragment {
            return GraphFragment()
        }
    }

    override fun onConnectionChange(isConnected: Boolean) {
        if (isConnected) {

        }
    }


}