package quizzo.app.util

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class Animations {
    companion object {

        fun delayAnimator(duration: Long): ValueAnimator {
            val valueAnimator = ValueAnimator.ofInt(100)
            valueAnimator.duration = duration
            return valueAnimator
        }

        fun moveUpDown(target: View, value: Float, duration: Long): ObjectAnimator {
            val objectAnimator = ObjectAnimator.ofFloat(target, "translationY", value, 0f)
            objectAnimator.duration = duration
            objectAnimator.interpolator = AccelerateDecelerateInterpolator()
            return objectAnimator
        }

        fun moveLeftRight(target: View, value: Float, duration: Long): ObjectAnimator {
            val objectAnimator = ObjectAnimator.ofFloat(target, "translationX", value, 0f)
            objectAnimator.duration = duration
            objectAnimator.interpolator = AccelerateDecelerateInterpolator()
            return objectAnimator
        }

        fun fadeIn(target: View, duration: Long): ObjectAnimator {
            val objectAnimator = ObjectAnimator.ofFloat(target, "alpha", 0f, 1f)
            objectAnimator.duration = duration
            return objectAnimator
        }

        fun fadeOut(target: View, duration: Long): ObjectAnimator {
            val objectAnimator = ObjectAnimator.ofFloat(target, "alpha", 1f, 0f)
            objectAnimator.duration = duration
            return objectAnimator
        }


    }
}