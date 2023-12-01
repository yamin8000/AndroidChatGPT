/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     About.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     About.kt Last modified at 2023/12/1
 *     This file is part of AndroidChatGPT/AndroidChatGPT.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     AndroidChatGPT/AndroidChatGPT.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     AndroidChatGPT/AndroidChatGPT.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with AndroidChatGPT.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.chatgpt.content.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import io.github.yamin8000.chatgpt.R
import io.github.yamin8000.chatgpt.ui.composables.PersianText
import io.github.yamin8000.chatgpt.ui.composables.Ripple
import io.github.yamin8000.chatgpt.ui.composables.ScaffoldWithTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutContent(
    onBackClick: () -> Unit
) {
    ScaffoldWithTitle(
        title = stringResource(R.string.about),
        onBackClick = onBackClick,
        content = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    val uriHandler = LocalUriHandler.current
                    val sourceUri = stringResource(R.string.github_source)
                    val licenseUri = stringResource(R.string.license_link)
                    Ripple(
                        onClick = { uriHandler.openUri(licenseUri) },
                        content = {
                            Image(
                                painterResource(id = R.drawable.ic_gplv3),
                                stringResource(id = R.string.gplv3_image_description),
                                modifier = Modifier
                                    .padding(32.dp)
                                    .fillMaxWidth(),
                                contentScale = ContentScale.FillWidth,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                            )
                        }
                    )
                    PersianText(
                        text = stringResource(id = R.string.license_header),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Ripple(
                        onClick = { uriHandler.openUri(sourceUri) },
                        content = {
                            Text(
                                text = sourceUri,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    )
                    PersianText(
                        text = stringResource(id = R.string.about_app),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }
    )
}