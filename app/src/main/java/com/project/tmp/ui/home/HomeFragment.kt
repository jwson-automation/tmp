package com.project.tmp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import com.project.tmp.databinding.FragmentHomeBinding

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.StartBtn.setOnClickListener {
            onButtonClicked()
        }
    }

    private fun onButtonClicked() {
        // Get the user's nickname and description from your UI
        val nickname = binding.editTextNickname.text.toString()
        val description = binding.editTextDescription.text.toString()

        // Retrieve the user's location using Android's location services (You need to implement this part)
        val latitude = 0.0 // Replace with the actual latitude value
        val longitude = 0.0 // Replace with the actual longitude value

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

        // Save the user's data in the database under the generated key
        userRef.child(userKey).setValue(userMap)
            .addOnSuccessListener {
                // Show a success message if data is saved successfully
                Toast.makeText(requireContext(), "Data sent successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Show an error message if data sending fails
                Toast.makeText(requireContext(), "Error sending data: $e", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}