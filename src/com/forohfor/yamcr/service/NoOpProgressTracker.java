package com.forohfor.yamcr.service;

/**
 * No-operation progress tracker that silently ignores all progress updates.
 * Useful for automated operations where progress output is not needed.
 */
public class NoOpProgressTracker implements ProgressTracker {
    
    @Override
    public boolean startTask(String taskName, int totalSteps) {
        return true;
    }
    
    @Override
    public void progressTask() {
        // No-op
    }
    
    @Override
    public void setSubtaskName(String subtaskName) {
        // No-op
    }
    
    @Override
    public void completeTask() {
        // No-op
    }
}
