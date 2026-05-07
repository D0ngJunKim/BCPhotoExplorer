package com.ssg.env.ds.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun Dp.asSp(): TextUnit = with(LocalDensity.current) { this@asSp.toSp() }

@Composable
fun TextUnit.asDp(): Dp = with(LocalDensity.current) { this@asDp.toDp() }