package av.kochekov.playlistmaker.images.data

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import av.kochekov.playlistmaker.images.domain.ImagesRepository
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ImagesRepositoryImpl(private val context: Context) : ImagesRepository {
    override fun saveImage(path: Uri, dir: String, name: String) : Uri {
        if (path.toString().isEmpty())
            return Uri.EMPTY
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), dir)
        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, name)
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = context.contentResolver.openInputStream(path)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return getImage(dir, name)
    }

    override fun getImage(dir: String, name: String) : Uri {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), dir)
        return File(filePath, name).toUri()
    }

    override fun removeImage(dir: String, name: String) : Boolean {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), dir)
        return File(filePath, name).delete()
    }
}