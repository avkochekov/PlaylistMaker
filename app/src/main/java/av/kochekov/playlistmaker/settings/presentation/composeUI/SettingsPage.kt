package av.kochekov.playlistmaker.settings.presentation.composeUI

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.common.presentation.CustomSwitch
import av.kochekov.playlistmaker.common.presentation.CustomTopAppBar
import av.kochekov.playlistmaker.common.presentation.getColor
import av.kochekov.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsPage() {
    val viewModel: SettingsViewModel = koinViewModel()
    val isDarkMode by viewModel.isDarkTheme().observeAsState()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.menu_settings),
            )
        },
        backgroundColor = getColor(R.attr.backgroundColor)
    ) {innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 24.dp),
        )
        {
            CustomSwitch(
                title = stringResource(id = R.string.settings_theme),
                checked = isDarkMode!!,
                onCheckedChanged = {
                    Log.d("COMPOSE", "$isDarkMode")
                    viewModel.switchTheme()
                }
            )
            CustomSettingsItem(
                title = stringResource(R.string.settings_share),
                imageResource = R.drawable.ic_24x24_share,
                onClicked = {
                    viewModel.shareApp()
                }
            )
            CustomSettingsItem(
                title = stringResource(R.string.settings_support),
                imageResource = R.drawable.ic_24x24_support,
                onClicked = {
                    viewModel.openSupport()
                }
            )
            CustomSettingsItem(
                title = stringResource(R.string.settings_license),
                imageResource = R.drawable.ic_24x24_arrow_forward,
                onClicked = {
                    viewModel.openTerms()
                }
            )
        }
    }
}

@Preview(
    name = "Light Mode",
    showSystemUi = true,
    showBackground = true)
@Preview(
    name = "Dark Mode",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_UNDEFINED)
@Composable
fun SettingsPagePreview() {
    SettingsPage()
}