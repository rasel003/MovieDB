package com.rasel.moviedb.ui.dashboard.view.fragment

import android.Manifest
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.util.Pair
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.paperflymerchantapp.data.network.Resource
import com.paperflymerchantapp.utils.FileUtils
import com.rasel.moviedb.R
import com.rasel.moviedb.data.preference.AppPrefsManager
import com.rasel.moviedb.databinding.FragmentGraphBinding
import com.rasel.moviedb.ui.dashboard.view_model.MerchantHomeViewModel
import com.rasel.moviedb.utils.NetworkChangeReceiver
import com.rasel.moviedb.utils.changeStatusBarColor
import com.rasel.moviedb.utils.getDateRangePicker
import com.rasel.moviedb.utils.toastError
import com.rasel.moviedb.utils.toastInfo
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.Calendar
import javax.inject.Inject


@AndroidEntryPoint
class GraphFragment : Fragment(), NetworkChangeReceiver.ConnectionChangeCallback{

    private lateinit var mContext: Context
    private lateinit var binding: FragmentGraphBinding
    private lateinit var networkChangeReceiver: NetworkChangeReceiver



    @Inject
    lateinit var preferences: AppPrefsManager

    private val viewModel: MerchantHomeViewModel by viewModels()


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