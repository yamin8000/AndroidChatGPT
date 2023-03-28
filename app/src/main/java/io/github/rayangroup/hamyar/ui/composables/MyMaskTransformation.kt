package io.github.rayangroup.hamyar.ui.composables

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import io.github.rayangroup.hamyar.util.log

class MyMaskTransformation(
    private val suggestion: String
) : VisualTransformation {

    private var suffix: String = ""

    override fun filter(text: AnnotatedString): TransformedText {
        suffix = if (suggestion.length > text.length)
            suggestion.takeLast(suggestion.length - text.length)
        else ""
        return TransformedText(
            offsetMapping = offsetMapping,
            text = buildAnnotatedString {
                withStyle(SpanStyle(Color.Unspecified)) {
                    append(text)
                }
                withStyle(SpanStyle(Color.Gray)) {
                    append(suffix)
                }
            }
        )
    }

    private val offsetMapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset < suggestion.length || suggestion.isEmpty()) return offset
            return suggestion.length - suffix.length
        }

        override fun transformedToOriginal(offset: Int): Int {
            offset.toString().log()
            if (offset == 0) return 0
            if (suggestion.length - suffix.length in 1 until offset)
                return suggestion.length - suffix.length
            return offset
        }
    }
}