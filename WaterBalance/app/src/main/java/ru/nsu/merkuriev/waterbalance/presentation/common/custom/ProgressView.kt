package ru.nsu.merkuriev.waterbalance.presentation.common.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import ru.nsu.merkuriev.waterbalance.R
import kotlin.math.max


class ProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val mFilledRect = Rect()
    private var mBackgroundDrawable: Drawable? = null
    private var mProgressDrawable: Drawable? = null

    private var level: Float = 0f
    private var currentLevel: Float = 0f

    private var isAnimated = true

    private var animator: ValueAnimator? = null

    init {
        if (attrs != null) {
            val typedArray =
                context.obtainStyledAttributes(
                    attrs,
                    R.styleable.ProgressView,
                    0,
                    0
                )

            mProgressDrawable =
                typedArray.getDrawable(R.styleable.ProgressView_progress_background)

            mBackgroundDrawable =
                typedArray.getDrawable(R.styleable.ProgressView_empty_background)

            level =
                typedArray.getFloat(R.styleable.ProgressView_progress_level, 0f)

            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var w = mBackgroundDrawable?.minimumWidth ?: 1
        var h = mBackgroundDrawable?.minimumHeight ?: 1

        w += paddingLeft + paddingRight
        h += paddingTop + paddingBottom

        w = max(w, suggestedMinimumWidth)
        h = max(h, suggestedMinimumHeight)

        setMeasuredDimension(
            resolveSizeAndState(w, widthMeasureSpec, 0),
            resolveSizeAndState(h, heightMeasureSpec, 0)
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // draw background
        mBackgroundDrawable?.setBounds(0, 0, width, height)
        mBackgroundDrawable?.draw(canvas!!)

        mFilledRect.set(mBackgroundDrawable?.bounds ?: mFilledRect)
        mFilledRect.top = (height * (MAX_LEVEL - currentLevel) / MAX_LEVEL).toInt()

        // draw progress drawable
        if (mFilledRect.width() > 0 && mFilledRect.height() > 0) {
            mProgressDrawable?.setBounds(0, 0, width, height)
            canvas?.clipRect(mFilledRect)
            mProgressDrawable?.draw(canvas!!)
        }
    }

    fun setLevel(value: Float) {

        if (value !in MIN_LEVEL..MAX_LEVEL) {
            return
        }

        val startLevel = level
        level = value

        if (isAnimated) {
            startAnimation(startLevel)
        } else {
            invalidate()
        }
    }

    fun getLevel(): Float = level

    fun setIsAnimated(shouldAnimate: Boolean) {
        isAnimated = shouldAnimate
    }

    private fun startAnimation(startLevel: Float) {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(startLevel, level).apply {
            duration = TIME_TO_ANIMATE
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                currentLevel = valueAnimator.animatedValue as Float
                invalidate()
            }
        }
        animator?.start()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()

        return ProgressViewSavedState(superState).apply {
            this.savedLevel = level
            this.savedCurrentLevel = currentLevel
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as? ProgressViewSavedState

        super.onRestoreInstanceState(savedState?.superState)

        level = savedState?.savedLevel ?: 0f
        currentLevel = savedState?.savedCurrentLevel ?: 0f

        invalidate()
    }

    private class ProgressViewSavedState : BaseSavedState {
        var savedLevel: Float = 0f
        var savedCurrentLevel: Float = 0f

        constructor(superState: Parcelable?) : super(superState)

        constructor(source: Parcel) : super(source) {
            savedLevel = source.readFloat()
            savedCurrentLevel = source.readFloat()
        }

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.writeFloat(savedLevel)
            out?.writeFloat(savedCurrentLevel)
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<ProgressViewSavedState> {
                override fun createFromParcel(source: Parcel) = ProgressViewSavedState(source)
                override fun newArray(size: Int): Array<ProgressViewSavedState?> =
                    arrayOfNulls(size)
            }
        }
    }

    companion object {
        const val MAX_LEVEL = 100f
        const val MIN_LEVEL = 0f

        const val TIME_TO_ANIMATE = 300L
    }
}