/*
 * Copyright (c) 2012 Todoroo Inc
 *
 * See the file "LICENSE" for the full license governing this code.
 */
package org.tasks.data

import com.natpryce.makeiteasy.MakeItEasy.with
import com.todoroo.andlib.utility.DateUtilities
import com.todoroo.astrid.data.Task
import com.todoroo.astrid.service.TaskDeleter
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.tasks.injection.InjectingTestCase
import org.tasks.injection.ProductionModule
import org.tasks.makers.TaskMaker.PARENT
import org.tasks.makers.TaskMaker.newTask
import javax.inject.Inject

@UninstallModules(ProductionModule::class)
@HiltAndroidTest
class TaskDaoTests : InjectingTestCase() {

    @Inject lateinit var taskDao: TaskDao
    @Inject lateinit var taskDeleter: TaskDeleter

    /** Test various task fetch conditions  */
    @Test
    fun testTaskConditions() = runBlocking {
        // create normal task
        var task = Task()
        task.title = "normal"
        taskDao.createNew(task)

        // create blank task
        task = Task()
        task.title = ""
        taskDao.createNew(task)

        // create hidden task
        task = Task()
        task.title = "hidden"
        task.hideUntil = DateUtilities.now() + 10000
        taskDao.createNew(task)

        // create task with deadlines
        task = Task()
        task.title = "deadlineInFuture"
        task.dueDate = DateUtilities.now() + 10000
        taskDao.createNew(task)
        task = Task()
        task.title = "deadlineInPast"
        task.dueDate = DateUtilities.now() - 10000
        taskDao.createNew(task)

        // create completed task
        task = Task()
        task.title = "completed"
        task.completionDate = DateUtilities.now() - 10000
        taskDao.createNew(task)

        // check is active
        assertEquals(5, taskDao.getActiveTasks().size)

        // check is visible
        assertEquals(5, taskDao.getActiveTasks().size)
    }

    /** Test task deletion  */
    @Test
    fun testTDeletion() = runBlocking {
        assertEquals(0, taskDao.getAll().size)

        // create task "happy"
        val task = Task()
        task.title = "happy"
        taskDao.createNew(task)
        assertEquals(1, taskDao.getAll().size)

        // delete
        taskDeleter.delete(task)
        assertEquals(0, taskDao.getAll().size)
    }

    /** Test passing invalid task indices to various things  */
    @Test
    fun testInvalidIndex() = runBlocking {
        assertEquals(0, taskDao.getAll().size)
        assertNull(taskDao.fetch(1))
        taskDeleter.delete(listOf(1L))

        // make sure db still works
        assertEquals(0, taskDao.getAll().size)
    }

    @Test
    fun findChildrenInList() = runBlocking {
        val parent = taskDao.createNew(newTask())
        val child = taskDao.createNew(newTask(with(PARENT, parent)))
        assertEquals(listOf(child), taskDao.getChildren(listOf(parent, child)))
    }

    @Test
    fun findRecursiveChildrenInList() = runBlocking {
        val parent = taskDao.createNew(newTask())
        val child = taskDao.createNew(newTask(with(PARENT, parent)))
        val grandchild = taskDao.createNew(newTask(with(PARENT, child)))
        assertEquals(
                listOf(child, grandchild, grandchild),
                taskDao.getChildren(listOf(parent, child, grandchild)))
    }

    @Test
    fun findRecursiveChildrenInListAfterSkippingParent() = runBlocking {
        val parent = taskDao.createNew(newTask())
        val child = taskDao.createNew(newTask(with(PARENT, parent)))
        val grandchild = taskDao.createNew(newTask(with(PARENT, child)))
        assertEquals(listOf(child, grandchild), taskDao.getChildren(listOf(parent, grandchild)))
    }

    @Test
    fun dontSetParentToSelf() = runBlocking {
        val parent = taskDao.createNew(newTask())
        val child = taskDao.createNew(newTask())

        taskDao.setParent(parent, listOf(parent, child))

        assertEquals(0, taskDao.fetch(parent)!!.parent)
        assertEquals(parent, taskDao.fetch(child)!!.parent)
    }
}