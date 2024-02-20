package av.kochekov.playlistmaker.player.presentation.custom_ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import av.kochekov.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : ImageView(context, attrs, defStyleAttr, defStyleRes) {

    private var imagePlay: Bitmap? = null
    private var imageStop: Bitmap? = null
    private var imageBitmap: Bitmap? = null
    private var isPlay: Boolean = false

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                imagePlay = getDrawable(R.styleable.PlaybackButtonView_playButtonImage)?.toBitmap()
                imageStop = getDrawable(R.styleable.PlaybackButtonView_stopButtonImage)?.toBitmap()
                imageBitmap = imagePlay
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val minViewSize = resources.getDimension(R.dimen.playbackButtonViewMinSize).toInt()

        setMeasuredDimension(minViewSize, minViewSize)
    }

    override fun onDraw(canvas: Canvas) {
        if (imageBitmap != null) {
            val rect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
            canvas.drawBitmap(imageBitmap!!, null, rect, null)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result = super.onTouchEvent(event)

        when (event?.action) {
            MotionEvent.ACTION_UP -> changeState()
        }
        return result
    }

    private fun changeState() {
        isPlay = !isPlay
        setPlayState(isPlay)
    }

    private fun setPlayState(isPlay: Boolean) {
        imageBitmap = if (isPlay) imageStop else imagePlay
        invalidate()
    }
}