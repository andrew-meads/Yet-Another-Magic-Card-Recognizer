package com.forohfor.yamcr.ui;

import com.forohfor.yamcr.app.RecogApp;
import com.forohfor.yamcr.generator.SetGenerator;
import com.forohfor.yamcr.service.GuiProgressTracker;

import javax.swing.*;

/**
 * GUI wrapper for SetGenerator operations.
 * Provides interactive dialogs and integrates with the OperationBar.
 */
public class SetGeneratorUI {
    
    /**
     * Show interactive dialog for bulk set generation and start the process.
     */
    public static void bulkGenSetsInteractive()
    {
        String selectedType = (String) JOptionPane.showInputDialog(null,
                "Choose set types to pregen",
                "Bulk Generate Sets",
                JOptionPane.PLAIN_MESSAGE, null,
                SetGenerator.SET_TYPES, "all"
        );

        if (selectedType == null)
        {
            return;
        }

        final String type = selectedType;
        new Thread()
        {
            public void run()
            {
                OperationBar bar = RecogApp.INSTANCE.getOpBar();
                GuiProgressTracker tracker = new GuiProgressTracker(bar);
                SetGenerator.bulkGenSets(type, tracker);
            }
        }.start();
    }
}
