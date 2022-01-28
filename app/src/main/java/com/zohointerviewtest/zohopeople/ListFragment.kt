package com.zohointerviewtest.zohopeople

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import com.zohointerviewtest.zohopeople.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private val viewModel: PeopleListViewModel by viewModels()

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted. Before proceeding with weather updates, check if GPS is on
                checkIfLocationIsOn()
            } else {
                // Permission is denied
                updateUIWithoutWeather()
            }
        }

    // Register the enable location callback, which handles the user's response to the
    // system's turn-on location dialog.
    private val requestToEnableLocation = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // GPS/Location Turned on - now, weather can be updated - call displayWeather()
            observeWeather()
        } else {
            // User was prompted to turn on location, but chose not to
            // TODO: Save last location and to display weather of that location with
            //  a prompt to user that location is turned off
            updateUIWithoutWeather()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Bind the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)
        checkLocationPermission()
        return binding.root
    }

    private fun observeWeather() {
        viewModel.currentLocation.observe(viewLifecycleOwner) { location ->
            Log.d("TEST", "Lat: ${location.latitude} Long: ${location.longitude}")
            binding.tvTest.text = location.latitude.toString()
        }
    }

    private fun checkIfLocationIsOn() {
        // Now that location permission is granted, ensure GPS is turned on in user's device
        activity?.let {
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            val task = LocationServices.getSettingsClient(it)
                .checkLocationSettings(builder.build())
            task.addOnSuccessListener { response ->
                val states = response.locationSettingsStates
                if (states != null) {
                    if (states.isLocationPresent) {
                        // GPS is enabled, now display weather
                        observeWeather()
                    }
                }
            }
            task.addOnFailureListener { e ->
                if (e is ResolvableApiException) {
                    try {
                        // If GPS is not enabled, show Snackbar with button to turn on location/GPS
                        showSnackBar(IntentSenderRequest.Builder(e.resolution).build())
                    } catch (sendEx: IntentSender.SendIntentException) {
                    }
                }
            }
        }
    }

    private fun showSnackBar(intentSenderRequest: IntentSenderRequest) {
        Snackbar.make(
            binding.parentFragmentList,
            R.string.snackbar_enable_gps,
            Snackbar.LENGTH_SHORT
        )
            .setAction(R.string.snackbar_bt_settings) {
                requestToEnableLocation.launch(intentSenderRequest)
            }
            .show()
    }

    private fun updateUIWithoutWeather() {
        // Explain to the user that current weather is unavailable
        // because weather requires location permission that the user has denied.
        binding.tvTest.text = "No location detected"
    }

    private fun checkLocationPermission() {
        // Devices that runs Android 5.1 (API level 22) or lower,
        // permissions are automatically granted - runtime permissions are not required
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                // Check if user has already granted location permission
                ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    checkIfLocationIsOn()
                }
                // Check if user previously denied permission but did not click - 'Never ask again'
                // then display a helpful dialog to let user know why location permission is needed
                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) -> {
                    showPermissionReasonDialog()
                }

                else -> {
                    // Permission not yet granted. Directly request for the permission.
                    // The registered ActivityResultCallback - 'requestPermissionLauncher' gets
                    // the result of this request.
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }
        } else checkIfLocationIsOn() // For devices with Android 5.1 (API level 22) or lower
    }

    private fun showPermissionReasonDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setTitle("Do you want to give access?")
        alertDialogBuilder.setMessage("Relax, your location data is safe. Current location is only used for weather updates.")
        alertDialogBuilder.apply {
            setPositiveButton(
                "OK"
            ) { dialog, _ ->
                // User clicked OK button
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                dialog.dismiss()
            }
            setNegativeButton(
                "CANCEL"
            ) { dialog, _ ->
                // User cancelled the dialog
                updateUIWithoutWeather()
                dialog.cancel()
            }
        }
        alertDialogBuilder.create().show()
    }
}