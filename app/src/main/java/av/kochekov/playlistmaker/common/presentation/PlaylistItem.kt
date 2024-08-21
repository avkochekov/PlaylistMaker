package av.kochekov.playlistmaker.common.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.library.domain.playlists.models.PlaylistModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlaylistItem(
    modifier: Modifier = Modifier,
    playlist: PlaylistModel
) {
    Column (
        modifier = modifier
    ) {
        GlideImage(
            modifier = Modifier
                .clip(RoundedCornerShape(2.dp))
                .size(160.dp),
            model = playlist.artwork,
            contentDescription = playlist.name,
            contentScale = ContentScale.Fit,
            loading = placeholder(R.drawable.placeholder),
        )
        CustomText(
            text = playlist.name,
            fontSize = 12.sp
        )
        CustomText(
            text = pluralStringResource(
                id = R.plurals.tracks,
                count = playlist.tracksCount,
                playlist.tracksCount
            ),
            fontSize = 12.sp
        )
    }
}

@Preview(
    name = "Light Mode",
    showSystemUi = true)
@Preview(
    name = "Dark Mode",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PlaylistItemPreview() {
    val model = PlaylistModel(
        uuid = "UUID 1",
        artwork = "Art 1",
        name = "Name 1",
        tracksCount = 4
    )
    PlaylistItem(playlist = model)
}