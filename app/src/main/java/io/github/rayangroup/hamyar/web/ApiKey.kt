package io.github.rayangroup.hamyar.web

@Suppress("SpellCheckingInspection")
object ApiKey {
    const val AUTHORIZATION = "Authorization"
    private const val BEARER = "Bearer"

    var KEYS = mutableMapOf(
        "$BEARER 123" to true,
        "$BEARER 456" to true,
        "$BEARER 789" to true,
    )

    const val GITHUB_TOKEN =
        "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"
}