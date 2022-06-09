package com.example.medic.Presentation.View.Adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.medic.MainActivity
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.medic.R
import com.example.medic.Presentation.View.Adapters.ImageSliderAdapter.ImageSliderViewHolder
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.activity.result.ActivityResultCallback
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.ImageView
import java.io.FileNotFoundException

class ImageSliderAdapter constructor(
    var images: MutableList<String?>,
    adding: Boolean,
    var mActivity: MainActivity?
) : RecyclerView.Adapter<ImageSliderViewHolder>() {
    var imageView: ImageView? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageSliderViewHolder {
        return ImageSliderViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.image_element, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        if (images.get(position) == null) {
            holder.view.findViewById<View>(R.id.imageContent).setVisibility(View.GONE)
            holder.view.findViewById<View>(R.id.addButtonImage).setVisibility(View.VISIBLE)
            holder.view.findViewById<View>(R.id.addButtonImage)
                .setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View) {
                        if (mActivity != null) {
                            mActivity!!.getActivityResultRegistry().register("key",
                                OpenDocument(),
                                object : ActivityResultCallback<Uri> {
                                    override fun onActivityResult(result: Uri) {
                                        mActivity!!.getApplicationContext().getContentResolver()
                                            .takePersistableUriPermission(
                                                result,
                                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                                            )
                                        images.add(images.size - 1, result.toString())
                                        notifyDataSetChanged()
                                    }
                                }).launch(arrayOf("image/*"))
                        }
                    }
                })
        } else {
            holder.view.findViewById<View>(R.id.addButtonImage).setVisibility(View.GONE)
            holder.view.findViewById<View>(R.id.imageContent).setVisibility(View.VISIBLE)
            if (mActivity != null) {
                try {
                    imageView = holder.view.findViewById(R.id.imageContent)
                    imageView!!.setImageBitmap(
                        BitmapFactory.decodeFileDescriptor(
                            mActivity!!.getApplicationContext().getContentResolver()
                                .openFileDescriptor(
                                    Uri.parse(images.get(position)), "r"
                                )!!.getFileDescriptor()
                        )
                    )
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ImageSliderViewHolder constructor(var view: View) : RecyclerView.ViewHolder(
        view
    )

    init {
        if (adding) {
            images.add(null)
        }
    }
}