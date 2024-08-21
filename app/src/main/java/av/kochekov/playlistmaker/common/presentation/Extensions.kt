package av.kochekov.playlistmaker.common.presentation

import android.content.Context
import android.util.TypedValue
import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource

@Composable
fun getColor(color: Int): Color {
    return colorResource(LocalContext.current.getColorFromAttrs(color).resourceId)
}

fun Context.getColorFromAttrs(attr: Int): TypedValue {
    return TypedValue().apply {
        theme.resolveAttribute(attr, this, true)
    }
}

@Composable
fun pluralResource(
    @PluralsRes resId: Int,
    quantity: Int,
    vararg formatArgs: Any? = emptyArray()
): String {
    return LocalContext.current.resources
        .getQuantityString(resId, quantity, *formatArgs)
}