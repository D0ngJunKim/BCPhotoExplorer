package com.ssg.env.ds.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayoutBaseScope
import androidx.constraintlayout.compose.HorizontalAnchorable
import androidx.constraintlayout.compose.VerticalAnchorable

enum class SpaceToken(internal val size: Dp) {
    Zero(0.dp),
    XXXS(4.dp),
    XXS(8.dp),
    XS(12.dp),
    SM(16.dp),
    MD(20.dp),
    LG(24.dp),
    XL(28.dp),
    XXL(32.dp),
    XXXL(36.dp),
    XXXXL(40.dp)
}

@Stable
fun Modifier.padding(all: SpaceToken = SpaceToken.Zero) = this
    .padding(all.size)

@Stable
fun Modifier.padding(
    horizontal: SpaceToken = SpaceToken.Zero,
    vertical: SpaceToken = SpaceToken.Zero,
) = this
    .padding(
        horizontal = horizontal.size,
        vertical = vertical.size
    )

@Stable
fun Modifier.padding(
    start: SpaceToken = SpaceToken.Zero,
    top: SpaceToken = SpaceToken.Zero,
    end: SpaceToken = SpaceToken.Zero,
    bottom: SpaceToken = SpaceToken.Zero,
) = this
    .padding(
        start = start.size,
        top = top.size,
        end = end.size,
        bottom = bottom.size
    )

@Stable
fun Modifier.padding(
    paddingValues: SpaceTokenValues
) = this
    .padding(
        paddingValues = paddingValues
    )

@Stable
fun Arrangement.spacedBy(space: SpaceToken) = this.spacedBy(space.size)

@Stable
fun SpaceTokenValues(
    all: SpaceToken,
): SpaceTokenValues = SpaceTokenValuesImpl(all)

@Stable
fun SpaceTokenValues(
    horizontal: SpaceToken = SpaceToken.Zero,
    vertical: SpaceToken = SpaceToken.Zero,
): SpaceTokenValues =
    SpaceTokenValues(horizontal, vertical, horizontal, vertical)

@Stable
fun SpaceTokenValues(
    start: SpaceToken = SpaceToken.Zero,
    top: SpaceToken = SpaceToken.Zero,
    end: SpaceToken = SpaceToken.Zero,
    bottom: SpaceToken = SpaceToken.Zero,
): SpaceTokenValues = SpaceTokenValuesImpl(start, top, end, bottom)

@Stable
interface SpaceTokenValues : PaddingValues

@Immutable
private class SpaceTokenValuesImpl(
    @Stable val start: SpaceToken = SpaceToken.Zero,
    @Stable val top: SpaceToken = SpaceToken.Zero,
    @Stable val end: SpaceToken = SpaceToken.Zero,
    @Stable val bottom: SpaceToken = SpaceToken.Zero,
) : SpaceTokenValues {

    init {
        check(
            (start.size.value >= 0f) and (start.size.value >= 0f) and (start.size.value >= 0f) and (start.size.value >= 0f)
        ) {
            "Padding must be non-negative"
        }
    }

    override fun calculateLeftPadding(layoutDirection: LayoutDirection) =
        if (layoutDirection == LayoutDirection.Ltr) start.size else end.size

    override fun calculateTopPadding() = top.size

    override fun calculateRightPadding(layoutDirection: LayoutDirection) =
        if (layoutDirection == LayoutDirection.Ltr) end.size else start.size

    override fun calculateBottomPadding() = bottom.size

    override fun equals(other: Any?): Boolean {
        if (other !is SpaceTokenValuesImpl) return false
        return start == other.start &&
                top == other.top &&
                end == other.end &&
                bottom == other.bottom
    }

    override fun hashCode() =
        ((start.hashCode() * 31 + top.hashCode()) * 31 + end.hashCode()) * 31 + bottom.hashCode()

    override fun toString() = "PaddingValues(start=$start, top=$top, end=$end, bottom=$bottom)"
}