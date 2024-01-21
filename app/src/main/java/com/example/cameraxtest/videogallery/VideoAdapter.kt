package com.example.cameraxtest.videogallery

import android.app.Activity
import android.media.ThumbnailUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.cameraxtest.GalleryActivity
import com.example.cameraxtest.R

class VideoAdapter(private var activity: Activity, private var videoPathList: ArrayList<String>) : RecyclerView.Adapter<VideoAdapter.ImageViewHolder>() {
    class ImageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var preview: ImageView? = null
        init {
            preview=itemView.findViewById(R.id.row_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_recycler_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videoPathList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val videoPath=videoPathList[position]
        holder.preview?.setImageBitmap(ThumbnailUtils.createVideoThumbnail(videoPath,1))
        holder.preview?.setOnClickListener {
            val activity = activity as GalleryActivity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.galleryFragmentContainerView,VideoFragment(videoPath))
                .setReorderingAllowed(true)
                .commit()
        }
    }
}
