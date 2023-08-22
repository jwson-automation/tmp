package com.project.tmp.ui.dashboard

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.tmp.ResultAdapter
import com.project.tmp.ResultDto
import com.project.tmp.Setting
import com.project.tmp.Setting.dateFormat
import com.project.tmp.databinding.FragmentDashboardBinding

private const val TAG = "DashboardFragment"

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSpinner()
    }

    private fun getResult(selectedGameType: String) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users") // Replace with your database path

        userRef.orderByChild("gameType").equalTo(selectedGameType)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val resultDtoList = mutableListOf<ResultDto>()

                    for (snapshot in dataSnapshot.children) {

                        Log.d(TAG, "onDataChange: $snapshot")

                        val nickname =
                            snapshot.child("nickname").getValue(String::class.java) ?: "null"
                        val description =
                            snapshot.child("description").getValue(String::class.java) ?: ""
                        val latitude = snapshot.child("latitude").getValue(Int::class.java) ?: 0
                        val longitude = snapshot.child("longitude").getValue(Int::class.java) ?: 0
                        val dateFormatted = snapshot.child("date").getValue(String::class.java) ?: ""

                        val gameType = snapshot.child("snapshot").getValue(String::class.java) ?: ""

                        val date = dateFormat.parse(dateFormatted)

                        val resultDto = ResultDto(nickname, description, latitude, longitude, date, gameType)
                        resultDtoList.add(resultDto)
                    }

                    resultDtoList.sortByDescending { it.date }

                    // Now you have the list of ResultDto objects
                    // You can do something with the resultDtoList here
                    // For example, pass it to your RecyclerView adapter

                    Log.d(TAG, "onDataChange: $resultDtoList")

                    initAdapter(resultDtoList)

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, "Error fetching data: $databaseError")
                    // Handle failure
                }
            })
    }

    private fun initAdapter(resultDtoList: List<ResultDto>) {
        val recyclerView = binding.resultRcv // Get a reference to your RecyclerView from the layout
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = ResultAdapter(resultDtoList) // Create an adapter instance
        recyclerView.adapter = adapter
    }

    private fun initSpinner() {
        // Replace with your game types
        val gameTypeSpinner = binding.spinnerGameType
        val gameTypeAdapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_item, Setting.gameTypes)
        gameTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gameTypeSpinner.adapter = gameTypeAdapter

        gameTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedGameType = Setting.gameTypes[position]
                getResult(selectedGameType)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected if needed
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}