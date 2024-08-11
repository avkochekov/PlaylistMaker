package av.kochekov.playlistmaker.common.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.common.domain.TrackModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TrackItem(
    track: TrackModel,
    onClicked: (TrackModel) -> Unit?
){
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .clickable {
                onClicked(track)
            }
    ){
        Row (
            modifier = Modifier
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
        ) {
            GlideImage(
                modifier = Modifier
                    .clip(RoundedCornerShape(2.dp))
                    .size(45.dp),
                model = track.artworkUrl100,
                contentDescription = track.trackName,
                contentScale = ContentScale.Fit,
                loading = placeholder(R.drawable.placeholder),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                CustomText(track.trackName)
                Row {
                    CustomText(
                        text = track.artistName,
                        fontSize = 11.sp
                    )
                    Image(
                        painter = painterResource(id = R.drawable.circle_separator),
                        contentDescription = ""
                    )
                    CustomText(
                        text = track.duration,
                        fontSize = 11.sp
                    )
                }
            }
            Image(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_24x24_arrow_forward),
                contentDescription = ""
            )
        }
    }
}