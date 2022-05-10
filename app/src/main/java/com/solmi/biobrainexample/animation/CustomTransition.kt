package com.solmi.biobrainexample.animation

import android.animation.Animator
import android.view.ViewGroup
import androidx.transition.Transition
import androidx.transition.TransitionValues

class CustomTransition : Transition() {

    override fun captureStartValues(transitionValues: TransitionValues) {}

    override fun captureEndValues(transitionValues: TransitionValues) {}

    /*override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {

    }*/

}