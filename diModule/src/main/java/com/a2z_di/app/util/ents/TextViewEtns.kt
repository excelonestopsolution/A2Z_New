package com.a2z_di.app.util.ents

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView

fun TextView.blink() {
    val anim: Animation = AlphaAnimation(0.0f, 1.0f)
    anim.duration = 500 //You can manage the blinking time with this parameter
    anim.startOffset = 20
    anim.repeatMode = Animation.REVERSE
    anim.repeatCount = Animation.INFINITE
    this.startAnimation(anim)
}