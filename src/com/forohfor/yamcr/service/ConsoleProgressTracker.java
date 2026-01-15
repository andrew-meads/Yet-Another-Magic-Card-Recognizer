package com.forohfor.yamcr.service;

/**
 * Console-based progress tracker that outputs progress to stdout.
 * Suitable for CLI applications.
 */
public class ConsoleProgressTracker implements ProgressTracker {
    
    private String taskName;
    private int totalSteps;
    private int currentStep;
    private boolean taskRunning;
    private String lastSubtaskLine = "";
    
    @Override
    public boolean startTask(String taskName, int totalSteps) {
        if (taskRunning) {
            System.err.println("Cannot start new task '" + taskName + "': another task is already running");
            return false;
        }
        
        this.taskName = taskName;
        this.totalSteps = totalSteps;
        this.currentStep = 0;
        this.taskRunning = true;
        this.lastSubtaskLine = "";
        
        System.out.println("Starting: " + taskName + " (" + totalSteps + " steps)");
        return true;
    }
    
    @Override
    public void progressTask() {
        if (!taskRunning) return;
        
        currentStep++;
        int percentage = (int) ((currentStep * 100.0) / totalSteps);
        
        // Clear the current line and print progress on a new line
        if (!lastSubtaskLine.isEmpty()) {
            System.out.print("\r" + " ".repeat(lastSubtaskLine.length()) + "\r");
        }
        System.out.println("Progress: " + currentStep + "/" + totalSteps + " (" + percentage + "%)");
        lastSubtaskLine = "";
    }
    
    @Override
    public void setSubtaskName(String subtaskName) {
        if (!taskRunning) return;
        
        String line = " → " + subtaskName;
        
        // Overwrite the current line with carriage return
        // Add padding to clear any leftover characters from a longer previous line
        int padding = Math.max(0, lastSubtaskLine.length() - line.length());
        System.out.print("\r" + line + " ".repeat(padding));
        System.out.flush();
        
        lastSubtaskLine = line;
    }
    
    @Override
    public void completeTask() {
        if (!taskRunning) return;
        
        // Clear the subtask line and print completion on a new line
        if (!lastSubtaskLine.isEmpty()) {
            System.out.print("\r" + " ".repeat(lastSubtaskLine.length()) + "\r");
        }
        System.out.println("Completed: " + taskName);
        taskRunning = false;
        lastSubtaskLine = "";
    }
}
