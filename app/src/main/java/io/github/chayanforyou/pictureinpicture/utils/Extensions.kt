package io.github.chayanforyou.pictureinpicture.utils

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.DimenRes

fun View.setMargin(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    val params = (layoutParams as? ViewGroup.MarginLayoutParams)
    params?.setMargins(
        left ?: params.leftMargin,
        top ?: params.topMargin,
        right ?: params.rightMargin,
        bottom ?: params.bottomMargin)
    layoutParams = params
}

fun View.setMarginRes(@DimenRes left: Int? = null, @DimenRes top: Int? = null, @DimenRes right: Int? = null, @DimenRes bottom: Int? = null) {
    setMargin(
        if (left == null) null else resources.getDimensionPixelSize(left),
        if (top == null) null else resources.getDimensionPixelSize(top),
        if (right == null) null else resources.getDimensionPixelSize(right),
        if (bottom == null) null else resources.getDimensionPixelSize(bottom),
    )
}

fun View.setLayoutGravity(gravity: Int) {
    val lp = layoutParams ?: return
    when (lp) {
        is FrameLayout.LayoutParams -> lp.gravity = gravity
        is LinearLayout.LayoutParams -> lp.gravity = gravity
        is RelativeLayout.LayoutParams -> {
            lp.addRule(gravity, RelativeLayout.TRUE)
        }
    }
    layoutParams = lp
}

fun View.setViewSize(dimensionInPixel: Float) {
    val dimensionInDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, resources.displayMetrics).toInt()
    layoutParams.height = dimensionInDp
    layoutParams.width = dimensionInDp
    requestLayout()
}

fun View.setViewSizeRes(@DimenRes dimensionResId: Int) {
    val dimensionInPx = resources.getDimensionPixelSize(dimensionResId)
    layoutParams.height = dimensionInPx
    layoutParams.width = dimensionInPx
    requestLayout()
}
