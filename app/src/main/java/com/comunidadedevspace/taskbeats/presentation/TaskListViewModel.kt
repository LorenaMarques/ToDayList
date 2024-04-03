package com.comunidadedevspace.taskbeats.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.taskbeats.ToDayListApplication
import com.comunidadedevspace.taskbeats.data.Task
import com.comunidadedevspace.taskbeats.data.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskListViewModel(private val taskDao: TaskDao) : ViewModel() {

    val taskListLiveData: LiveData<List<Task>> = taskDao.getAll()

    fun executeTasks(taskAction: TaskAction) {
        when (taskAction.actionType) {
            ActionType.DELETE -> deleteById(taskAction.task!!.id)

            ActionType.CREATE -> insertIntoDataBase(taskAction.task!!)

            ActionType.UPDATE -> updateIntoDataBase(taskAction.task!!)

            ActionType.DELETE_ALL -> deleteAll()
        }
    }

    private fun deleteById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.deleteItemById(id)
        }
    }

    private fun insertIntoDataBase(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.insert(task)
        }
    }

    private fun updateIntoDataBase(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.update(task)
        }
    }

    private fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.deleteAll()
        }
    }


    companion object {
        fun create(application: Application): TaskListViewModel {
            val dataBaseInstance = (application as ToDayListApplication).getAppDataBase()
            val dao = dataBaseInstance.taskDao()
            return TaskListViewModel(dao)
        }
    }
}