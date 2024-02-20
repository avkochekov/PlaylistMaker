package av.kochekov.playlistmaker.player.presentation.custom_ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import av.kochekov.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
    // При наследовании от ImageView ругается линтер:
    // This custom view should extend androidx.appcompat.widget.AppCompatImageView instead [AppCompatCustomView]
    // как это пофиксить мне пока не понятно
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var imagePlay: Bitmap? = null
    private var imageStop: Bitmap? = null
    private var imageBitmap: Bitmap? = null
    private var imageRect: RectF = RectF()
    private var isPlay: Boolean = false

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                imageStop = getDrawable(R.styleable.PlaybackButtonView_stopButtonImage)?.toBitmap()
                    ?: resources.getDrawable(R.drawable.pause_button).toBitmap()
                imagePlay = getDrawable(R.styleable.PlaybackButtonView_playButtonImage)?.toBitmap()
                    ?: resources.getDrawable(R.drawable.play_button).toBitmap()
                imageBitmap = imagePlay
            } finally {
                recycle()
            }
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val minViewSize = resources.getDimension(R.dimen.playbackButtonViewMinSize).toInt()

        setMeasuredDimension(minViewSize, minViewSize)

        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        imageBitmap?.let { canvas.drawBitmap(it, null, imageRect, null) }
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