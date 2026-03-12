package com.example.autostartme

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.autostartme.databinding.FragmentSecondBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefs: AutostartPreferences
    private lateinit var adapter: AppPickerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = AutostartPreferences(requireContext())
        adapter = AppPickerAdapter(requireContext().packageManager) { packageName ->
            if (prefs.isAppAdded(packageName)) {
                Snackbar.make(binding.root, R.string.app_already_added, Snackbar.LENGTH_SHORT).show()
            } else {
                prefs.addApp(packageName)
                Snackbar.make(binding.root, R.string.app_added, Snackbar.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        binding.recyclerAppPicker.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAppPicker.adapter = adapter

        binding.searchEditText.addTextChangedListener { text ->
            adapter.filter(text?.toString() ?: "")
        }

        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        binding.progressBar.visibility = View.VISIBLE
        val pm = requireContext().packageManager

        viewLifecycleOwner.lifecycleScope.launch {
            val apps = withContext(Dispatchers.IO) {
                val intent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_LAUNCHER)
                }
                pm.queryIntentActivities(intent, 0)
                    .sortedBy { it.loadLabel(pm).toString().lowercase() }
            }
            binding.progressBar.visibility = View.GONE
            adapter.submitList(apps)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
