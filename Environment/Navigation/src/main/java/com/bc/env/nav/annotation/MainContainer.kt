package com.bc.env.nav.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class MainContainer(
    val start: Boolean = false
)
