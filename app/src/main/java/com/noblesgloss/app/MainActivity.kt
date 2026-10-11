package com.noblesgloss.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Lacquer = Color(0xFF101012)
private val Panel = Color(0xFF1B191C)
private val PanelRaised = Color(0xFF252126)
private val Ivory = Color(0xFFF0E8DD)
private val Muted = Color(0xFFB2A6A1)
private val Burgundy = Color(0xFF7C2035)
private val Brass = Color(0xFFC3A16A)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(16, 16, 18)
        window.navigationBarColor = android.graphics.Color.rgb(16, 16, 18)
        window.decorView.systemUiVisibility = 0
        setContent { Dimit trescuiApp() }
    }
}

@Composable
private fun Dimit trescuiApp() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Lacquer) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Lacquer)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Header()
                Spacer(Modifier.height(14.dp))
                WorkspaceBar()
                Spacer(Modifier.height(12.dp))
                ViewportPlaceholder(modifier = Modifier.weight(1f))
                Spacer(Modifier.height(12.dp))
                ToolRail()
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "FOUNDATION BUILD  •  3D RENDERER NOT CONNECTED",
                    color = Muted,
                    fontSize = 9.sp,
                    letterSpacing = 1.1.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("NOBLES-GLOSS", color = Ivory, fontSize = 19.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.6.sp)
            Text("DIMITTRESCUI  /  STUDIO", color = Brass, fontSize = 9.sp, letterSpacing = 1.8.sp)
        }
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(PanelRaised, RoundedCornerShape(12.dp))
                .border(1.dp, Brass.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("N", color = Ivory, fontSize = 22.sp, fontWeight = FontWeight.Light)
        }
    }
}

@Composable
private fun WorkspaceBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Panel, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("SCENE", color = Ivory, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
        Text("Object mode", color = Muted, fontSize = 11.sp)
        Text("⋮", color = Brass, fontSize = 20.sp)
    }
}

@Composable
private fun ViewportPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Panel, RoundedCornerShape(16.dp))
            .border(1.dp, Brass.copy(alpha = 0.24f), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .border(1.dp, Burgundy, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("◇", color = Brass, fontSize = 38.sp)
            }
            Text("THE VIEWPORT", color = Ivory, fontSize = 12.sp, fontWeight = FontWeight.Medium, letterSpacing = 2.sp)
            Text("Scene renderer is the next integration step", color = Muted, fontSize = 10.sp)
        }
        Text(
            "PERSPECTIVE  •  GRID OFF",
            modifier = Modifier.align(Alignment.BottomStart).padding(14.dp),
            color = Muted,
            fontSize = 9.sp,
            letterSpacing = 0.8.sp
        )
        Text(
            "XYZ",
            modifier = Modifier.align(Alignment.TopEnd).padding(14.dp),
            color = Brass,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ToolRail() {
    val tools = listOf("Select", "Move", "Shape", "Material")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Panel, RoundedCornerShape(14.dp))
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        tools.forEachIndexed { index, label ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(if (index == 0) Burgundy else Color.Transparent, RoundedCornerShape(10.dp))
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(listOf("⌖", "↔", "◇", "◈")[index], color = if (index == 0) Ivory else Brass, fontSize = 18.sp)
                Text(label, color = if (index == 0) Ivory else Muted, fontSize = 10.sp)
            }
        }
    }
}
