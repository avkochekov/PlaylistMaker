package av.kochekov.playlistmaker.library.presentation.composeUI

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.common.presentation.CustomText
import av.kochekov.playlistmaker.common.presentation.CustomTopAppBar
import av.kochekov.playlistmaker.common.presentation.getColor
import av.kochekov.playlistmaker.library.domain.playlists.models.PlaylistModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LibraryPage(
    onTrackClicked: (Int) -> Unit?,
    onPlaylistClicked: (PlaylistModel) -> Unit?,
    newPlaylistClicked: () -> Unit?
) {
    // CoroutineScope нам нужен для анимации при переключении табов
    val scope = rememberCoroutineScope()

    // Ключевое состояние Pager, при инициализации которого указывается количество экранов, которые можно переключать
    val pagerState = rememberPagerState(pageCount = { 2 })

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.menu_library),
            )
        },
        backgroundColor = getColor(R.attr.backgroundColor)
    ) {innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
        )
        {
            TabRow(
                backgroundColor = getColor(color = R.attr.backgroundColor),
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                            .height(2.dp)
                            .padding(horizontal = 16.dp)
                            .background(color = getColor(color = R.attr.textColor))
                    )
                },
            ) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    selectedContentColor = colorResource(id = R.color.gray),
                    unselectedContentColor = colorResource(id = R.color.gray),
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    text = { CustomText(text = stringResource(id = R.string.library_favoriteTracks)) },
                )

                Tab(
                    selected = pagerState.currentPage == 1,
                    selectedContentColor = colorResource(id = R.color.gray),
                    unselectedContentColor = colorResource(id = R.color.gray),
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                            Log.d("PAGER", "Current page: ${pagerState.currentPage}")
                        }
                    },
                    text = { CustomText(text = stringResource(id = R.string.library_playLists)) },
                )
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                when(page) {
                    0 -> LibraryFavoritePage(
                        modifier = Modifier
                            .fillMaxSize(),
                        onTrackClicked = onTrackClicked)
                    1 -> LibraryPlaylistPage(
                        modifier = Modifier
                            .fillMaxSize(),
                        onPlaylistClicked = onPlaylistClicked,
                        onNewPlaylistClicked = newPlaylistClicked
                    )
                }
            }
        }
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
fun LibraryPagePreview() {
    LibraryPage(
        onTrackClicked = {},
        onPlaylistClicked = {},
        newPlaylistClicked = {}
    )
}
