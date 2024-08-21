package av.kochekov.playlistmaker.common.presentation

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import av.kochekov.playlistmaker.library.domain.playlists.models.PlaylistModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlaylistList(
    modifier: Modifier = Modifier,
    list: List<PlaylistModel>,
    onItemClicked: (playlist: PlaylistModel) -> Unit?,
    columns: Int = 2
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(columns),
        content = {
            items(list) {playlist ->
                Box (
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onItemClicked(playlist) },
                ) {
                    PlaylistItem(
                        modifier = Modifier.align(Alignment.Center),
                        playlist = playlist
                    )
                }
            }
        }
    )
}

@Preview(
    name = "Light Mode",
    showSystemUi = true)
@Preview(
    name = "Dark Mode",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PlaylistListPreview() {
    val list by remember {
        mutableStateOf(
            listOf(
                PlaylistModel(
                    uuid = "UUID 1",
                    artwork = "Art 1",
                    name = "Name 1",
                    tracksCount = 0
                ),
                PlaylistModel(
                    uuid = "UUID 2",
                    artwork = "Art 2",
                    name = "Name 2",
                    tracksCount = 1
                ),
                PlaylistModel(
                    uuid = "UUID 3",
                    artwork = "Art 3",
                    name = "Name 3",
                    tracksCount = 2
                ),
                PlaylistModel(
                    uuid = "UUID 4",
                    artwork = "Art 4",
                    name = "Name 4",
                    tracksCount = 7
                )
            )
        )
    }
    PlaylistList(list = list, onItemClicked = {})
}