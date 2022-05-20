package mjy.audiorecorder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SoundVisualizerView(
    context: Context,
    attrs: AttributeSet
) : View(context, attrs) {

    private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_500)
        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND
    }
    private val visualizeRepeatAction: Runnable = object : Runnable {
        override fun run() {
            if (!isReplaying) {
                val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0
                drawingAmplitudes = listOf(currentAmplitude) + drawingAmplitudes
            } else {
                replayingPosition++
            }
            invalidate()

            handler?.postDelayed(this, ACTION_INTERVAL)
        }
    }

    private var drawingWidth: Int = 0
    private var drawingHeight: Int = 0
    private var isReplaying: Boolean = false
    private var replayingPosition: Int = 0
    private var drawingAmplitudes: List<Int> = emptyList()
    var onRequestCurrentAmplitude: (() -> Int)? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingWidth = w
        drawingHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return

        val centerY = drawingHeight / 2F
        var startX = drawingWidth.toFloat()

        drawingAmplitudes
            .let { amplitude ->
                if (isReplaying) {
                    amplitude.takeLast(replayingPosition)
                } else {
                    amplitude
                }
            }
            .forEach { amplitude ->
            val lineLength = amplitude / MAX_AMPLITUDE * drawingHeight * 0.8F

            startX -= LINE_SPACE
            if (startX < 0) return@forEach

            canvas.drawLine(
                startX,
                centerY - lineLength / 2F,
                startX,
                centerY + lineLength / 2F,
                amplitudePaint
            )
        }
    }

    fun startVisualizing(isReplaying: Boolean) {
        this.isReplaying = isReplaying
        handler?.post(visualizeRepeatAction)
    }

    fun stopVisualizing() {
        replayingPosition = 0
        handler?.removeCallbacks(visualizeRepeatAction)
    }

    fun clearVisualizeing() {
        drawingAmplitudes = emptyList()
        invalidate()
    }

    companion object {
        private const val LINE_WIDTH = 10F
        private const val LINE_SPACE = 15F
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat()
        private const val ACTION_INTERVAL = 20L
    }
}