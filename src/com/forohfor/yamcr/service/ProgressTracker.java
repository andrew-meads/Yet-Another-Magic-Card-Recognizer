package com.forohfor.yamcr.service;

/**
 * Interface for tracking progress of long-running operations.
 * Implementations can be GUI-based (progress bars) or CLI-based (console output).
 */
public interface ProgressTracker {
    
    /**
     * Start a new task with a name and total number of steps.
     * @param taskName Name of the task
     * @param totalSteps Total number of steps in the task
     * @return true if the task was started successfully, false if another task is already running
     */
    boolean startTask(String taskName, int totalSteps);
    
    /**
     * Update the current progress by incrementing the step counter.
     */
    void progressTask();
    
    /**
     * Set the name of the current subtask being executed.
     * @param subtaskName Name of the subtask
     */
    void setSubtaskName(String subtaskName);
    
    /**
     * Mark the current task as complete.
     */
    void completeTask();
}
