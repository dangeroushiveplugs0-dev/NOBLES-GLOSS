package com.noblesgloss.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noblesgloss.app.scene.AddSceneObjectCommand
import com.noblesgloss.app.scene.RemoveSceneObjectCommand
import com.noblesgloss.app.scene.SceneHistory
import com.noblesgloss.app.scene.SceneObject
import com.noblesgloss.app.scene.SceneObjectKind
import com.noblesgloss.app.scene.TransformSceneObjectCommand

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
    var history by remember { mutableStateOf(SceneHistory()) }
    val scene = history.current
    val selected = scene.objects.firstOrNull { it.id == scene.selectedObjectId }

    MaterialTheme {
        Surface(Modifier.fillMaxSize(), color = Lacquer) {
            Column(
                Modifier.fillMaxSize().background(Lacquer).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("NOBLES-GLOSS", color = Ivory, fontSize = 19.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.5.sp)
                        Text("DIMITTRESCUI / STUDIO", color = Brass, fontSize = 10.sp, letterSpacing = 1.6.sp)
                    }
                    Box(
                        Modifier.size(42.dp).background(Panel, RoundedCornerShape(12.dp))
                            .border(1.dp, Brass.copy(alpha = .5f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) { Text("N", color = Ivory, fontSize = 22.sp) }
                }

                Row(
                    Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(12.dp)).padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("SCENE", color = Ivory, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("${scene.objects.size} objects", color = Muted, fontSize = 12.sp)
                    Text("Object mode", color = Brass, fontSize = 11.sp)
                }

                Row(
                    Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SceneAction("＋ Cube") {
                        val next = SceneObject(name = "Cube ${scene.objects.count { it.kind == SceneObjectKind.CUBE } + 1}", kind = SceneObjectKind.CUBE)
                        history = history.execute(AddSceneObjectCommand(next))
                    }
                    SceneAction("＋ Sphere") {
                        val next = SceneObject(name = "Sphere ${scene.objects.count { it.kind == SceneObjectKind.SPHERE } + 1}", kind = SceneObjectKind.SPHERE)
                        history = history.execute(AddSceneObjectCommand(next))
                    }
                    SceneAction("＋ Plane") {
                        val next = SceneObject(name = "Plane ${scene.objects.count { it.kind == SceneObjectKind.PLANE } + 1}", kind = SceneObjectKind.PLANE)
                        history = history.execute(AddSceneObjectCommand(next))
                    }
                    SceneAction("Undo", enabled = history.canUndo) { history = history.undo() }
                    SceneAction("Redo", enabled = history.canRedo) { history = history.redo() }
                    SceneAction("Delete", enabled = selected != null) {
                        selected?.let { history = history.execute(RemoveSceneObjectCommand(it, scene.selectedObjectId)) }
                    }
                }

                if (scene.objects.isNotEmpty()) {
                    Column(
                        Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(12.dp)).padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        scene.objects.takeLast(3).reversed().forEach { obj ->
                            Row(
                                Modifier.fillMaxWidth()
                                    .clickable { history = history.copy(current = history.current.select(obj.id)) }
                                    .background(if (obj.id == scene.selectedObjectId) Burgundy.copy(alpha = .55f) else Color.Transparent, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${if (obj.id == scene.selectedObjectId) "◆" else "◇"}  ${obj.name}", color = if (obj.id == scene.selectedObjectId) Ivory else Muted, fontSize = 11.sp)
                                Text(obj.kind.name, color = Brass, fontSize = 9.sp)
                            }
                        }
                    }
                }

                if (selected != null) {
                    Column(
                        Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(12.dp)).padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            "TRANSFORM  ·  ${selected.name}",
                            color = Brass,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            "X ${"%.1f".format(selected.transform.x)}   Y ${"%.1f".format(selected.transform.y)}   Z ${"%.1f".format(selected.transform.z)}",
                            color = Ivory,
                            fontSize = 11.sp
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            SceneAction("X −") {
                                val before = selected.transform
                                history = history.execute(
                                    TransformSceneObjectCommand(selected.id, before, before.copy(x = before.x - 0.25f))
                                )
                            }
                            SceneAction("X +") {
                                val before = selected.transform
                                history = history.execute(
                                    TransformSceneObjectCommand(selected.id, before, before.copy(x = before.x + 0.25f))
                                )
                            }
                            SceneAction("Y −") {
                                val before = selected.transform
                                history = history.execute(
                                    TransformSceneObjectCommand(selected.id, before, before.copy(y = before.y - 0.25f))
                                )
                            }
                            SceneAction("Y +") {
                                val before = selected.transform
                                history = history.execute(
                                    TransformSceneObjectCommand(selected.id, before, before.copy(y = before.y + 0.25f))
                                )
                            }
                        }
                    }
                }

                Viewport3D(objects = scene.objects, modifier = Modifier.weight(1f).fillMaxWidth())

                Text(
                    "LIVE SCENE VIEWPORT · MESH / CAMERA / LIGHT PROXIES PENDING",
                    Modifier.align(Alignment.CenterHorizontally),
                    color = Muted,
                    fontSize = 9.sp,
                    letterSpacing = .4.sp
                )

                Row(
                    Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(14.dp)).padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Select", "Move", "Shape", "Material").forEachIndexed { index, label ->
                        Column(
                            Modifier.weight(1f)
                                .background(if (index == 0) Burgundy else Color.Transparent, RoundedCornerShape(10.dp))
                                .padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(listOf("⌖", "↔", "◇", "◈")[index], color = if (index == 0) Ivory else Brass, fontSize = 18.sp)
                            Text(label, color = if (index == 0) Ivory else Muted, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SceneAction(label: String, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 7.dp),
        shape = RoundedCornerShape(9.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Burgundy,
            contentColor = Ivory,
            disabledContainerColor = Panel,
            disabledContentColor = Muted
        )
    ) {
        Text(label, fontSize = 11.sp, maxLines = 1)
    }
}
