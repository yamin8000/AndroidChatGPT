package io.github.rayan_group.hamyar.ad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.github.rayan_group.hamyar.R

@Composable
fun TapsellAdContent(
    modifier: Modifier = Modifier,
    onCreated: (ViewGroup) -> Unit,
    onUpdate: (ViewGroup) -> Unit
) {
    val context = LocalContext.current
    Surface {
        AndroidView(
            modifier = modifier,
            update = onUpdate,
            factory = {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.standard_banner, null, false)
                    .findViewById<ViewGroup>(R.id.standardBanner)
                onCreated(view)
                view
            }
        )

    }
}