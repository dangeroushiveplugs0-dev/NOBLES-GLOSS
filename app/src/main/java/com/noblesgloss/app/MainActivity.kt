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
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.noblesgloss.app.materials.MaterialGraph
import com.noblesgloss.app.materials.MaterialNode
import com.noblesgloss.app.materials.MaterialNodeKind
import com.noblesgloss.app.scene.AddSceneObjectCommand
import com.noblesgloss.app.scene.RemoveSceneObjectCommand
import com.noblesgloss.app.scene.SceneDocument
import com.noblesgloss.app.scene.SceneHistory
import com.noblesgloss.app.scene.SceneObject
import com.noblesgloss.app.scene.SceneObjectKind
import com.noblesgloss.app.scene.Transform3D
import com.noblesgloss.app.scene.TransformSceneObjectCommand
import java.util.UUID

private val Lacquer = Color(0xFF101012)
private val Panel = Color(0xFF1B191C)
private val Ivory = Color(0xFFF0E8DD)
private val Muted = Color(0xFFB2A6A1)
private val Burgundy = Color(0xFF7C2035)
private val Brass = Color(0xFFC3A16A)

private enum class StudioTool(val label: String, val glyph: String) {
    SELECT("Select", "⌖"),
    MOVE("Transform", "↔"),
    SHAPE("Add", "◇"),
    MATERIAL("Material", "◈")
}

enum class TransformMode { MOVE, ROTATE, SCALE }

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
    var history by remember {
        val starter = SceneObject(name = "Cube 1", kind = SceneObjectKind.CUBE)
        mutableStateOf(
            SceneHistory(
                current = SceneDocument(objects = listOf(starter), selectedObjectId = starter.id)
            )
        )
    }
    var activeTool by remember { mutableStateOf(StudioTool.SELECT) }
    var transformMode by remember { mutableStateOf(TransformMode.MOVE) }
    var menuExpanded by remember { mutableStateOf(false) }
    var panelOpen by remember { mutableStateOf(false) }
    var materialGraph by remember { mutableStateOf(MaterialGraph()) }

    val scene = history.current
    val selected = scene.objects.firstOrNull { it.id == scene.selectedObjectId }

    fun addObject(kind: SceneObjectKind) {
        val count = scene.objects.count { it.kind == kind } + 1
        val label = when (kind) {
            SceneObjectKind.CUBE -> "Cube"
            SceneObjectKind.SPHERE -> "Sphere"
            SceneObjectKind.PLANE -> "Plane"
            SceneObjectKind.CAMERA -> "Camera"
            SceneObjectKind.LIGHT -> "Light"
            SceneObjectKind.MESH -> "Mesh"
        }
        history = history.execute(AddSceneObjectCommand(SceneObject(name = "$label $count", kind = kind)))
    }

    fun editSelected(update: (Transform3D) -> Transform3D) {
        val current = selected ?: return
        history = history.execute(
            TransformSceneObjectCommand(current.id, current.transform, update(current.transform))
        )
    }

    fun addMaterialNode(kind: MaterialNodeKind) {
        val title = when (kind) {
            MaterialNodeKind.PRINCIPLED_SURFACE -> "Principled BSDF"
            MaterialNodeKind.OUTPUT -> "Material Output"
            MaterialNodeKind.CONSTANT_FLOAT -> "Value"
            MaterialNodeKind.CONSTANT_COLOR -> "Color"
            MaterialNodeKind.NOISE -> "Noise Texture"
            MaterialNodeKind.VORONOI -> "Voronoi Texture"
            MaterialNodeKind.MATH -> "Math"
            MaterialNodeKind.MIX -> "Mix"
            MaterialNodeKind.NORMAL_MAP -> "Normal Map"
            MaterialNodeKind.FRESNEL -> "Fresnel"
            MaterialNodeKind.UV_COORDINATES -> "UV Coordinates"
            MaterialNodeKind.IMAGE_TEXTURE -> "Image Texture"
        }
        materialGraph = materialGraph.copy(
            nodes = materialGraph.nodes + MaterialNode(id = UUID.randomUUID().toString(), kind = kind, title = title)
        )
    }

    MaterialTheme {
        Surface(Modifier.fillMaxSize(), color = Lacquer) {
            Column(
                Modifier.fillMaxSize()
                    .background(Lacquer)
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth().height(42.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                        Text("NOBLES-GLOSS", color = Ivory, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.1.sp)
                        Text("DIMITTRESCUI  /  STUDIO", color = Brass, fontSize = 9.sp, letterSpacing = 1.2.sp)
                    }
                    Box {
                        Box(
                            Modifier.size(38.dp)
                                .background(Panel, RoundedCornerShape(11.dp))
                                .border(1.dp, Brass.copy(alpha = .5f), RoundedCornerShape(11.dp))
                                .clickable { menuExpanded = true },
                            contentAlignment = Alignment.Center
                        ) { Text("⋮", color = Ivory, fontSize = 23.sp) }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            modifier = Modifier.background(Panel)
                        ) {
                            DropdownMenuItem(text = { Text("Add cube", color = Ivory) }, onClick = { addObject(SceneObjectKind.CUBE); menuExpanded = false })
                            DropdownMenuItem(text = { Text("Add sphere", color = Ivory) }, onClick = { addObject(SceneObjectKind.SPHERE); menuExpanded = false })
                            DropdownMenuItem(text = { Text("Add plane", color = Ivory) }, onClick = { addObject(SceneObjectKind.PLANE); menuExpanded = false })
                            DropdownMenuItem(text = { Text("Add camera proxy", color = Ivory) }, onClick = { addObject(SceneObjectKind.CAMERA); menuExpanded = false })
                            DropdownMenuItem(text = { Text("Add light proxy", color = Ivory) }, onClick = { addObject(SceneObjectKind.LIGHT); menuExpanded = false })
                            DropdownMenuItem(
                                text = { Text("Undo", color = if (history.canUndo) Ivory else Muted) },
                                enabled = history.canUndo,
                                onClick = { history = history.undo(); menuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Redo", color = if (history.canRedo) Ivory else Muted) },
                                enabled = history.canRedo,
                                onClick = { history = history.redo(); menuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete selected", color = if (selected != null) Ivory else Muted) },
                                enabled = selected != null,
                                onClick = { selected?.let { history = history.execute(RemoveSceneObjectCommand(it, scene.selectedObjectId)) }; menuExpanded = false }
                            )
                        }
                    }
                }

                Row(
                    Modifier.fillMaxWidth().height(35.dp)
                        .background(Panel, RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("SCENE", color = Brass, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Row(
                        Modifier.weight(1f).horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        scene.objects.forEach { obj ->
                            Box(
                                Modifier.background(
                                    if (obj.id == scene.selectedObjectId) Burgundy else Color(0xFF29262A),
                                    RoundedCornerShape(7.dp)
                                ).clickable { history = history.copy(current = history.current.select(obj.id)) }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Text(obj.name, color = if (obj.id == scene.selectedObjectId) Ivory else Muted, fontSize = 10.sp, maxLines = 1)
                            }
                        }
                        if (scene.objects.isEmpty()) Text("No objects · use Add", color = Muted, fontSize = 10.sp)
                    }
                    Text("${scene.objects.size}", color = Muted, fontSize = 10.sp)
                }

                if (panelOpen) when (activeTool) {
                    StudioTool.SELECT -> {
                        Row(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(10.dp)).padding(7.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            SmallAction("＋ Cube") { addObject(SceneObjectKind.CUBE) }
                            SmallAction("＋ Sphere") { addObject(SceneObjectKind.SPHERE) }
                            SmallAction("＋ Plane") { addObject(SceneObjectKind.PLANE) }
                        }
                    }
                    StudioTool.MOVE -> {
                        Column(
                            Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(10.dp)).padding(7.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
                                TransformMode.values().forEach { mode ->
                                    SmallAction(mode.name, active = transformMode == mode) { transformMode = mode }
                                }
                                Spacer(Modifier.weight(1f))
                                Text(selected?.name ?: "No selection", color = Muted, fontSize = 10.sp)
                            }
                            Row(
                                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                when (transformMode) {
                                    TransformMode.MOVE -> {
                                        SmallAction("X −") { editSelected { it.copy(x = it.x - .25f) } }
                                        SmallAction("X +") { editSelected { it.copy(x = it.x + .25f) } }
                                        SmallAction("Y −") { editSelected { it.copy(y = it.y - .25f) } }
                                        SmallAction("Y +") { editSelected { it.copy(y = it.y + .25f) } }
                                        SmallAction("Z −") { editSelected { it.copy(z = it.z - .25f) } }
                                        SmallAction("Z +") { editSelected { it.copy(z = it.z + .25f) } }
                                    }
                                    TransformMode.ROTATE -> {
                                        SmallAction("X −15°") { editSelected { it.copy(rotationX = it.rotationX - 15f) } }
                                        SmallAction("X +15°") { editSelected { it.copy(rotationX = it.rotationX + 15f) } }
                                        SmallAction("Y −15°") { editSelected { it.copy(rotationY = it.rotationY - 15f) } }
                                        SmallAction("Y +15°") { editSelected { it.copy(rotationY = it.rotationY + 15f) } }
                                        SmallAction("Z −15°") { editSelected { it.copy(rotationZ = it.rotationZ - 15f) } }
                                        SmallAction("Z +15°") { editSelected { it.copy(rotationZ = it.rotationZ + 15f) } }
                                    }
                                    TransformMode.SCALE -> {
                                        SmallAction("− Scale") {
                                            editSelected { it.copy(scaleX = (it.scaleX - .1f).coerceAtLeast(.1f), scaleY = (it.scaleY - .1f).coerceAtLeast(.1f), scaleZ = (it.scaleZ - .1f).coerceAtLeast(.1f)) }
                                        }
                                        SmallAction("+ Scale") {
                                            editSelected { it.copy(scaleX = it.scaleX + .1f, scaleY = it.scaleY + .1f, scaleZ = it.scaleZ + .1f) }
                                        }
                                    }
                                }
                            }
                            Text(
                                selected?.let { "X %.2f  Y %.2f  Z %.2f".format(it.transform.x, it.transform.y, it.transform.z) } ?: "Select an object to edit",
                                color = Muted,
                                fontSize = 10.sp
                            )
                        }
                    }
                    StudioTool.SHAPE -> {
                        Row(
                            Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(10.dp)).padding(7.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            SmallAction("＋ Cube") { addObject(SceneObjectKind.CUBE) }
                            SmallAction("＋ Sphere") { addObject(SceneObjectKind.SPHERE) }
                            SmallAction("＋ Plane") { addObject(SceneObjectKind.PLANE) }
                            SmallAction("Delete", enabled = selected != null) {
                                selected?.let { history = history.execute(RemoveSceneObjectCommand(it, scene.selectedObjectId)) }
                            }
                        }
                    }
                    StudioTool.MATERIAL -> {
                        Column(
                            Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(10.dp)).padding(7.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("MATERIAL GRAPH", color = Brass, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                Text("${materialGraph.nodes.size} nodes", color = Muted, fontSize = 10.sp)
                            }
                            Row(
                                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                SmallAction("Principled") { addMaterialNode(MaterialNodeKind.PRINCIPLED_SURFACE) }
                                SmallAction("Noise") { addMaterialNode(MaterialNodeKind.NOISE) }
                                SmallAction("Voronoi") { addMaterialNode(MaterialNodeKind.VORONOI) }
                                SmallAction("Mix") { addMaterialNode(MaterialNodeKind.MIX) }
                                SmallAction("Output") { addMaterialNode(MaterialNodeKind.OUTPUT) }
                                SmallAction("Clear", enabled = materialGraph.nodes.isNotEmpty()) { materialGraph = MaterialGraph() }
                            }
                            Text(
                                if (materialGraph.nodes.isEmpty()) "Add nodes to start building a graph. Shader rendering is not connected yet."
                                else materialGraph.nodes.takeLast(3).joinToString("  ·  ") { it.title } + if (materialGraph.nodes.size > 3) "  ·  …" else "",
                                color = Muted,
                                fontSize = 9.sp,
                                maxLines = 2
                            )
                        }
                    }
                }

                Viewport3D(
                    objects = scene.objects,
                    selectedObjectId = scene.selectedObjectId,
                    gizmoMode = transformMode,
                    onGizmoModeChange = { transformMode = it },
                    onGizmoDrag = { id, axis, amount ->
                        val obj = history.current.objects.firstOrNull { it.id == id }
                        if (obj != null) {
                            val before = obj.transform
                            val step = amount * 0.012f
                            val after = when (transformMode) {
                                TransformMode.MOVE -> when (axis) {
                                    "X" -> before.copy(x = before.x + step)
                                    "Y" -> before.copy(y = before.y - step)
                                    else -> before.copy(z = before.z + step)
                                }
                                TransformMode.ROTATE -> when (axis) {
                                    "X" -> before.copy(rotationX = before.rotationX + step * 30f)
                                    "Y" -> before.copy(rotationY = before.rotationY + step * 30f)
                                    else -> before.copy(rotationZ = before.rotationZ + step * 30f)
                                }
                                TransformMode.SCALE -> {
                                    val next = (before.scaleX + step).coerceAtLeast(0.1f)
                                    before.copy(scaleX = next, scaleY = next, scaleZ = next)
                                }
                            }
                            if (after != before) history = history.execute(TransformSceneObjectCommand(id, before, after))
                        }
                    },
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )

                Row(
                    Modifier.fillMaxWidth().height(54.dp)
                        .background(Panel, RoundedCornerShape(13.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StudioTool.values().forEach { tool ->
                        Column(
                            Modifier.weight(1f).fillMaxHeight()
                                .background(if (activeTool == tool) Burgundy else Color.Transparent, RoundedCornerShape(9.dp))
                                .clickable {
                                    if (activeTool == tool) panelOpen = !panelOpen else { activeTool = tool; panelOpen = true }
                                }
                                .padding(vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(tool.glyph, color = if (activeTool == tool) Ivory else Brass, fontSize = 18.sp)
                            Text(tool.label, color = if (activeTool == tool) Ivory else Muted, fontSize = 9.sp, maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SmallAction(
    label: String,
    enabled: Boolean = true,
    active: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (active) Burgundy else Color(0xFF302B30),
            contentColor = Ivory,
            disabledContainerColor = Panel,
            disabledContentColor = Muted
        ),
        modifier = Modifier.heightIn(min = 32.dp)
    ) {
        Text(label, fontSize = 10.sp, maxLines = 1)
    }
}
