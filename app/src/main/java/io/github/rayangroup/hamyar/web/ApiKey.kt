package io.github.rayangroup.hamyar.web

@Suppress("SpellCheckingInspection")
object ApiKey {
    const val AUTHORIZATION = "Authorization"
    private const val BEARER = "Bearer"

    var KEYS = mutableMapOf(
        "$BEARER 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" to true
    )

    const val GITHUB_TOKEN =
        "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"
}