package com.cloudinary.android_kotlin.download.picasso

import android.content.Context
import android.widget.ImageView
import com.cloudinary.android_kotlin.core.download.DownloadRequestCallback
import com.cloudinary.android_kotlin.download.DownloadRequestBuilderStrategy
import com.cloudinary.android_kotlin.download.DownloadRequestStrategy
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

internal class PicassoDownloadRequestBuilderStrategy internal constructor(context: Context)
    : DownloadRequestBuilderStrategy {

    private val picasso: Picasso = Picasso.Builder(context).build()
    private var requestCreator: RequestCreator? = null
    private var callback: DownloadRequestCallback? = null

    override fun load(url: String): DownloadRequestBuilderStrategy {
        requestCreator = picasso.load(url)
        return this
    }

    override fun load(resourceId: Int): DownloadRequestBuilderStrategy {
        requestCreator = picasso.load(resourceId)
        return this
    }

    override fun placeholder(resourceId: Int): DownloadRequestBuilderStrategy {
        requestCreator?.placeholder(resourceId) ?: throw IllegalStateException("Must call load before.")
        return this
    }

    override fun callback(callback: DownloadRequestCallback): DownloadRequestBuilderStrategy {
        this.callback = callback
        return this
    }

    override fun into(imageView: ImageView): DownloadRequestStrategy {
        callback?.let {
            requestCreator?.into(imageView, object : Callback {
                override fun onSuccess() {
                    it.onSuccess()
                }

                override fun onError(e: Exception) {
                    it.onFailure(e)
                }
            })
        } ?: requestCreator?.into(imageView)

        return PicassoDownloadRequestStrategy(picasso, imageView)
    }
}