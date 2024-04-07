package com.pnt.query_file

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pnt.query_file.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.recyclerView.adapter = FilesAdapter(
            context = this@MainActivity,
            docxFiles = getDocFiles()
        )
    }

    // Method To get DOCX files
    private fun getDocFiles(): ArrayList<String> {
        val contentResolver = contentResolver
        val mime = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx")
        val args = arrayOf(mimeType)
        val proj = arrayOf(MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DISPLAY_NAME)
        val sortingOrder = MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        val cursor = contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            proj,
            mime,
            args,
            sortingOrder
        )
        val docFiles = ArrayList<String>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                val path = cursor.getString(index)
                docFiles.add(path)
            }
            cursor.close()
        }

        Log.d(TAG, "getDocFiles: ${docFiles.count()}")

        return docFiles
    }
}