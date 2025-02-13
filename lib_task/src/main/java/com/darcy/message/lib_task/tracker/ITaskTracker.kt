package com.darcy.message.lib_task.tracker

import com.darcy.message.lib_task.task.ITask

interface ITaskTracker {
    fun setTaskStatus(iTask: ITask, status: TaskStatus)
    fun getTaskStatus(iTask: ITask): TaskStatus
}

sealed class TaskStatus {
    data object Unknown : TaskStatus()
    data object Pending : TaskStatus()
    data object Running : TaskStatus()
    data object Canceled : TaskStatus()
    data class Success(val result: String) : TaskStatus(), Comparable<TaskStatus> {
        companion object{

        }
        override fun compareTo(other: TaskStatus): Int {
            return when (other) {
                is Success -> {
                    if (result == other.result){
                        0
                    } else {
                        1
                    }
                }
                else -> -1
            }
        }
    }
    data class Error(val cause: Throwable) : TaskStatus(), Comparable<TaskStatus> {
        override fun compareTo(other: TaskStatus): Int {
            return when (other) {
                is Error -> 0
                else -> -1
            }
        }
    }
}