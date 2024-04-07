package com.pnt.query_file

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FilesAdapter(
    private var context: Context, private var docxFiles: MutableList<String>
) : RecyclerView.Adapter<FilesAdapter.FilesViewHolder>() {
    class FilesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var fileName: TextView = view.findViewById(R.id.file_name)
        var filepath: TextView = view.findViewById(R.id.file_path)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.display_files_layout, parent, false)
        return FilesViewHolder(view)
    }

    override fun getItemCount(): Int = docxFiles.size

    override fun onBindViewHolder(holder: FilesViewHolder, position: Int) {
        val path = docxFiles[position]
        val docFile = File(path)
        val filename = docFile.name
        val filepath = docFile.path

        holder.fileName.text = filename
        holder.filepath.text = filepath
    }
}