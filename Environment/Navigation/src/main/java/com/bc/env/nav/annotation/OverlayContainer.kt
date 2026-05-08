package com.bc.env.nav.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class OverlayContainer(
    val start: Boolean = false
)
