package com.project.tmp.ui.home

import android.R
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.location.LocationServices
import com.project.tmp.Setting.dateFormat
import com.project.tmp.Setting.gameTypes
import com.project.tmp.databinding.FragmentHomeBinding
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.project.tmp.Setting.latitude
import com.project.tmp.Setting.longitude

private const val TAG = "HomeFragment"
private const val LOCATION_PERMISSION_REQUEST_CODE = 123

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                // 위치 업데이트 처리
                val lastLocation = locationResult.lastLocation
                // 여기서 위치 정보를 처리하고 UI를 업데이트할 수 있습니다.
            }
        }
    }

    private fun initSpinner() {
        // Replace with your game types
        val gameTypeSpinner = binding.spinnerGameType
        val gameTypeAdapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_item, gameTypes)
        gameTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gameTypeSpinner.adapter = gameTypeAdapter

        binding.StartBtn.setOnClickListener {
            onSendButtonClicked(gameTypeSpinner.selectedItem as String)
        }
        binding.changeLocationBtn.setOnClickListener {
            onLocationUpdateButtonClicked()

        }
    }


    private fun onLocationUpdateButtonClicked() {
        // 위치서비스
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 위치 권한이 없는 경우 권한 요청
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // 위치 정보 가져오기
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    latitude = location.latitude
                    longitude = location.longitude

                    var coordinatesText = "현재 좌표: $latitude, $longitude"
                    binding.textViewCoordinates.text = coordinatesText

                    Log.d(TAG, "onLocationUpdateButtonClicked: $latitude")

                    // 위의 코드와 동일하게 진행
                } ?: run {
                    Toast.makeText(
                        requireContext(),
                        "Could not get current location.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun onSendButtonClicked(selectedGameType: String) {

        // Get the user's nickname and description from your UI
        val nickname = binding.editTextNickname.text.toString()
        val description = binding.editTextDescription.text.toString()

        val currentDateInMillis = System.currentTimeMillis() // Get the current date in milliseconds

        val currentDateFormatted = dateFormat.format(currentDateInMillis)

        // Send the user's data to the Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users")

        // Generate a unique key for the user
        val userKey = userRef.push().key ?: return

        // Create a HashMap to store the user's data
        val userMap = HashMap<String, Any>()
        userMap["latitude"] = latitude
        userMap["longitude"] = longitude
        userMap["nickname"] = nickname
        userMap["description"] = description
        userMap["date"] = currentDateFormatted
        userMap["gameType"] = selectedGameType


        // Save the user's data in the database under the generated key
        userRef.child(userKey).setValue(userMap)
            .addOnSuccessListener {
                // Show a success message if data is saved successfully
                Toast.makeText(requireContext(), "Data sent successfully!", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener { e ->
                // Show an error message if data sending fails
                Toast.makeText(requireContext(), "Error sending data: $e", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우 버튼 클릭 로직 다시 수행
                onSendButtonClicked(binding.spinnerGameType.selectedItem as String)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Location permission denied.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 임시로 만들어 둔 지역 매퍼!
    private fun getRegionFromCoordinates(latitude: Double, longitude: Double): String {
        if (latitude in 33.0..38.5 && longitude in 124.0 .. 131.0) {
            return "경상북도"
        } else if (latitude in 34.0..38.0 && longitude in 128.5 .. 131.0) {
            return "경상남도"
        } else if (latitude in 37.0..38.5 && longitude in 125.0 .. 127.5) {
            return "강원도"
        } else if (latitude in 36.0..37.5 && longitude in 126.0 .. 127.5) {
            return "충청북도"
        } else if (latitude in 35.5..36.5 && longitude in 126.0 .. 127.5) {
            return "충청남도"
        } else if (latitude in 36.0..37.0 && longitude in 127.0 .. 128.5) {
            return "전라북도"
        } else if (latitude in 34.5..35.5 && longitude in 126.5 .. 128.0) {
            return "전라남도"
        } else if (latitude in 33.0..35.0 && longitude in 126.0 .. 127.5) {
            return "경기도"
        } else if (latitude in 37.0..38.0 && longitude in 126.5 .. 127.5) {
            return "인천광역시"
        } else if (latitude in 37.0..38.0 && longitude in 127.0 .. 128.0) {
            return "서울특별시"
        }
        // 다른 지역들에 대한 조건을 추가로 작성합니다.
        else {
            return "알 수 없음"
        }
    }
}