package av.kochekov.playlistmaker.images.domain

import android.net.Uri

interface ImagesRepository {
    fun saveImage(path: Uri, dir: String = "", name: String) : Uri

    fun saveImage(path: String, dir: String = "", name: String) : Uri

    fun getImage(dir: String = "", name: String) : Uri

    fun removeImage(dir: String = "", name: String) : Boolean
}