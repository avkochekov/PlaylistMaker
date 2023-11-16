package av.kochekov.playlistmaker.playlist.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import av.kochekov.playlistmaker.images.domain.ImagesRepository
import av.kochekov.playlistmaker.playlist.domain.PlaylistInteractor
import av.kochekov.playlistmaker.common.data.models.Playlist
import java.util.*

class PlaylistViewModel(
    private val imagesRepository: ImagesRepository,
    private val interactor: PlaylistInteractor
    ) : ViewModel() {
    private val fragmentState = MutableLiveData<Playlist>()
    private var initState: Playlist? = null

    init {
        initState = Playlist(
            udi = "",
            artwork = "",
            name = "",
            description = ""
        )
        fragmentState.postValue(
            initState?.copy()
        )
    }

    fun openPlaylist(udi: String){
//        interactor.ge
    }

    fun setArtwork(path: String){
        if (path != fragmentState.value?.artwork)
            fragmentState.value?.artwork = path
    }

    fun setName(text: String){
        if (text != fragmentState.value?.name)
            fragmentState.value?.name = text
    }

    fun setDescription(text: String){
        if (text != fragmentState.value?.description)
            fragmentState.value?.description = text
    }

    fun fragmentState(): LiveData<Playlist> {
        return fragmentState
    }

    fun savePlayList(){
        fragmentState.value?.apply {
            if (udi.isEmpty()) {
                udi = UUID.randomUUID().toString()
                artwork = imagesRepository.saveImage(
                    path = Uri.parse(artwork),
                    name = udi
                ).toString()
            }
            interactor.savePlaylist(this)
        }
        initState = fragmentState.value?.copy()
        }

    fun isChanged(): Boolean{
        if (fragmentState.value?.artwork != initState?.artwork)
            return true
        if (fragmentState.value?.name != initState?.name)
            return true
        if (fragmentState.value?.description != initState?.description)
            return true
        return false
    }
}