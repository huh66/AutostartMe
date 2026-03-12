package com.example.autostartme

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.autostartme.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefs: AutostartPreferences
    private lateinit var adapter: AutostartAppAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = AutostartPreferences(requireContext())
        adapter = AutostartAppAdapter(requireContext().packageManager) { packageName ->
            prefs.removeApp(packageName)
            refreshList()
        }

        binding.recyclerAutostartApps.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAutostartApps.adapter = adapter

        binding.btnOverlayPermission.setOnClickListener {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${requireContext().packageName}")
            )
            startActivity(intent)
        }

        binding.btnBatteryPermission.setOnClickListener {
            val intent = Intent(
                Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                Uri.parse("package:${requireContext().packageName}")
            )
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
        updatePermissionBanner()
    }

    private fun refreshList() {
        val apps = prefs.getApps().sorted()
        adapter.submitList(apps)
        val isEmpty = apps.isEmpty()
        binding.emptyText.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerAutostartApps.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun updatePermissionBanner() {
        val ctx = requireContext()
        val hasOverlay = Settings.canDrawOverlays(ctx)
        val pm = ctx.getSystemService<PowerManager>()
        val hasBattery = pm?.isIgnoringBatteryOptimizations(ctx.packageName) ?: true

        binding.btnOverlayPermission.visibility = if (hasOverlay) View.GONE else View.VISIBLE
        binding.btnBatteryPermission.visibility = if (hasBattery) View.GONE else View.VISIBLE

        binding.permissionBanner.visibility =
            if (hasOverlay && hasBattery) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
