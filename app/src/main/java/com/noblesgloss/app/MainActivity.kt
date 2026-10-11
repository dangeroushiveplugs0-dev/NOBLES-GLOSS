package com.noblesgloss.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.sceneview.SceneView
import io.github.sceneview.math.Size
import io.github.sceneview.node.CubeNode
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Lacquer = Color(0xFF101012)
private val Panel = Color(0xFF1B191C)
private val Ivory = Color(0xFFF0E8DD)
private val Muted = Color(0xFFB2A6A1)
private val Burgundy = Color(0xFF7C2035)
private val Brass = Color(0xFFC3A16A)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(16, 16, 18)
        window.navigationBarColor = android.graphics.Color.rgb(16, 16, 18)
        setContent { DIMITTRESCUIApp() }
    }
}

@Composable
private fun DIMITTRESCUIApp() {
    MaterialTheme {
        Surface(Modifier.fillMaxSize(), color = Lacquer) {
            Column(Modifier.fillMaxSize().background(Lacquer).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("NOBLES-GLOSS", color = Ivory, fontSize = 19.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.5.sp)
                        Text("DIMITTRESCUI / STUDIO", color = Brass, fontSize = 10.sp, letterSpacing = 1.6.sp)
                    }
                    Box(Modifier.size(42.dp).background(Panel, RoundedCornerShape(12.dp)).border(1.dp, Brass.copy(alpha = .5f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                        Text("N", color = Ivory, fontSize = 22.sp)
                    }
                }
                Row(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(12.dp)).padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("SCENE", color = Ivory, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("Object mode", color = Muted, fontSize = 12.sp)
                    Text("⋮", color = Brass, fontSize = 20.sp)
                }
                Viewport3D(modifier = Modifier.weight(1f).fillMaxWidth())
                Row(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(14.dp)).padding(6.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Select", "Move", "Shape", "Material").forEachIndexed { index, label ->
                        Column(Modifier.weight(1f).background(if (index == 0) Burgundy else Color.Transparent, RoundedCornerShape(10.dp)).padding(vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(listOf("⌖", "↔", "◇", "◈")[index], color = if (index == 0) Ivory else Brass, fontSize = 18.sp)
                            Text(label, color = if (index == 0) Ivory else Muted, fontSize = 10.sp)
                        }
                    }
                }
                Text("FOUNDATION BUILD · RENDERER NOT CONNECTED", Modifier.align(Alignment.CenterHorizontally), color = Muted, fontSize = 9.sp, letterSpacing = .6.sp)
            }
        }
    }
}
