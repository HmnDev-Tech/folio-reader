package com.hmndev.folioreader.ui.theme

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

object FolioMotion {
    // Material 3 Expressive Spring for tactile sheets and pills
    val ExpressiveSpring: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    // Snappy spring for quick responsive actions
    val SnappySpring: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )

    // Gentle fade for full-screen immersive transition
    val ImmersiveFade = tween<Float>(durationMillis = 280)
}
