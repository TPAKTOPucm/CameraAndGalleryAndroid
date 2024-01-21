package com.example.cameraxtest.photogallery

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.cameraxtest.GalleryActivity
import com.example.cameraxtest.R

class ImageAdapter(private var activity: Activity, private var imageList: ArrayList<String>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    class ImageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var image: ImageView? = null
        init {
            image=itemView.findViewById(R.id.row_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_recycler_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image=imageList[position]
        holder.image?.setImageURI(Uri.parse(image))
        holder.image?.setOnClickListener {
            val activity = activity as GalleryActivity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.galleryFragmentContainerView,ImageFragment(image))
                .setReorderingAllowed(true)
                .commit()
        }
    }
}
