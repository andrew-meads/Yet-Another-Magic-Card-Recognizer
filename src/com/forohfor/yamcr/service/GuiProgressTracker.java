package com.forohfor.yamcr.service;

import com.forohfor.yamcr.ui.OperationBar;

/**
 * GUI-based progress tracker that wraps an OperationBar component.
 * Used by the main GUI application.
 */
public class GuiProgressTracker implements ProgressTracker {
    
    private final OperationBar operationBar;
    
    public GuiProgressTracker(OperationBar operationBar) {
        this.operationBar = operationBar;
    }
    
    @Override
    public boolean startTask(String taskName, int totalSteps) {
        return operationBar.setTask(taskName, totalSteps);
    }
    
    @Override
    public void progressTask() {
        operationBar.progressTask();
    }
    
    @Override
    public void setSubtaskName(String subtaskName) {
        operationBar.setSubtaskName(subtaskName);
    }
    
    @Override
    public void completeTask() {
        operationBar.endTask();
    }
}
