package com.noblesgloss.app

import com.noblesgloss.app.scene.AddSceneObjectCommand
import com.noblesgloss.app.scene.SceneDocument
import com.noblesgloss.app.scene.SceneHistory
import com.noblesgloss.app.scene.SceneObject
import com.noblesgloss.app.scene.SceneObjectKind
import com.noblesgloss.app.scene.RemoveSceneObjectCommand
import com.noblesgloss.app.scene.Transform3D
import com.noblesgloss.app.scene.TransformSceneObjectCommand
import com.noblesgloss.app.materials.MaterialConnection
import com.noblesgloss.app.materials.MaterialGraph
import com.noblesgloss.app.materials.MaterialNode
import com.noblesgloss.app.materials.MaterialNodeKind
import com.noblesgloss.app.materials.MaterialSocket
import com.noblesgloss.app.materials.MaterialValueType
import com.noblesgloss.app.materials.SocketDirection
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test fun foundationSanityCheck() { assertTrue(true) }

    @Test fun addingObjectSelectsItAndUndoRedoWorks() {
        val cube = SceneObject(id = "cube-1", name = "Cube", kind = SceneObjectKind.CUBE)
        val added = SceneHistory().execute(AddSceneObjectCommand(cube))
        assertEquals(listOf(cube), added.current.objects)
        assertEquals("cube-1", added.current.selectedObjectId)
        assertTrue(added.canUndo)

        val undone = added.undo()
        assertTrue(undone.current.objects.isEmpty())
        assertTrue(undone.canRedo)

        val redone = undone.redo()
        assertEquals(listOf(cube), redone.current.objects)
    }
}

    @Test fun transformCanBeChangedAndReverted() {
        val cube = SceneObject(id = "cube-1", name = "Cube", kind = SceneObjectKind.CUBE)
        val initial = SceneDocument().addObject(cube)
        val moved = Transform3D(x = 2f, y = 1f)
        val history = SceneHistory(initial).execute(
            TransformSceneObjectCommand(cube.id, cube.transform, moved)
        )
        assertEquals(moved, history.current.objects.single().transform)
        assertEquals(initial.objects.single().transform, history.undo().current.objects.single().transform)
    }

    @Test fun removingSelectedObjectClearsSelection() {
        val cube = SceneObject(id = "cube-1", name = "Cube", kind = SceneObjectKind.CUBE)
        assertNull(SceneDocument().addObject(cube).removeObject(cube.id).selectedObjectId)
    }

    @Test(expected = IllegalArgumentException::class)
    fun nonFiniteTransformIsRejected() {
        val cube = SceneObject(id = "cube-1", name = "Cube", kind = SceneObjectKind.CUBE)
        SceneDocument().addObject(cube).updateTransform(cube.id, Transform3D(x = Float.NaN))
    }

    @Test fun removingObjectCanBeUndoneAndRedone() {
        val cube = SceneObject(id = "cube-1", name = "Cube", kind = SceneObjectKind.CUBE)
        val second = SceneObject(id = "sphere-1", name = "Sphere", kind = SceneObjectKind.SPHERE)
        val initial = SceneDocument().addObject(cube).addObject(second)
        val removed = SceneHistory(initial).execute(RemoveSceneObjectCommand(second, second.id))

        assertEquals(listOf(cube), removed.current.objects)
        assertEquals(cube.id, removed.current.selectedObjectId)

        val undone = removed.undo()
        assertEquals(listOf(cube, second), undone.current.objects)
        assertEquals(second.id, undone.current.selectedObjectId)
        assertEquals(listOf(cube), undone.redo().current.objects)
    }
