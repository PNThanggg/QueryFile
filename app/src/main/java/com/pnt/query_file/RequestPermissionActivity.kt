package com.pnt.query_file

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.pnt.query_file.databinding.ActivityRequestPermissionBinding

private const val TAG = "RequestPermissionActivity"

class RequestPermissionActivity : AppCompatActivity() {
    companion object {
        private const val PERMISSION_CODE_MANAGE_EXTERNAL_STORAGE = 210
    }

    private val binding by lazy {
        ActivityRequestPermissionBinding.inflate(layoutInflater)
    }

    private val storageActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Android is 11 (R) or above
                if (Environment.isExternalStorageManager()) {
                    // Manage External Storage Permissions Granted
                    Log.d(TAG, "onActivityResult: Manage External Storage Permissions Granted")
                } else {
                    Toast.makeText(this@RequestPermissionActivity, "Storage Permissions Denied", Toast.LENGTH_SHORT).show()
                }

                startActivity(Intent(this@RequestPermissionActivity, MainActivity::class.java))
            } else {
                // Below android 11
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.requestPermissionButton.setOnClickListener {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE_MANAGE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty()) {
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if (read && write) {
                    Toast.makeText(
                        this@RequestPermissionActivity,
                        "Storage Permissions Granted",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this@RequestPermissionActivity, MainActivity::class.java))
                } else {
                    Toast.makeText(
                        this@RequestPermissionActivity,
                        "Storage Permissions Denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun requestPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    val intent = Intent()
                    intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    val uri = Uri.fromParts("package", this.packageName, null)
                    intent.setData(uri)
                    storageActivityResultLauncher.launch(intent)
                } catch (e: java.lang.Exception) {
                    val intent = Intent()
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    storageActivityResultLauncher.launch(intent)
                }
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                    ), PERMISSION_CODE_MANAGE_EXTERNAL_STORAGE
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "requestPermissions: ${e.message}")
        }
    }
}