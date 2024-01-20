package com.example.cameraxtest.photogallery

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cameraxtest.GalleryActivity
import com.example.cameraxtest.R

class ImageAdapter(private var context: Activity,private var imageList: ArrayList<Image>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
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
        Glide.with(context).load(image.imagePath).apply(RequestOptions().centerCrop()).into(holder.image!!)
        holder.image?.setOnClickListener {
            var act = context as GalleryActivity
            act.supportFragmentManager.beginTransaction()
                .replace(R.id.galleryFragmentContainerView,ImageFragment(image.imagePath))
                .setReorderingAllowed(true)
                .commit()
        }
    }
}
