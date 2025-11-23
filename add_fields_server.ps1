// Script to add missing field declarations to GameServer.java

$file = 'src/main/java/org/jgame/server/GameServer.java'
$content = Get-Content $file -Raw

# Add missing field declarations after existing fields
$fieldsToAdd = @'

    // UI Components  
    private JFrame frame;
    private Container contentPane;
    private JPopupMenu popupMenu;
    private ButtonGroup popupMenuGroup;
    private AboutDialog aboutBox;
    private JTextField statusField;
    
    // Application tracking
    private static int numSSs = 0;
    private static List<GameServer> GameServerApplications = new ArrayList<>();
    
    // Configuration
    private static ResourceBundle gameResourceBundle;
    private String serverAddress;
    
    // Look and Feel data
    private static LookAndFeelData[] lookAndFeelData = {
        new LookAndFeelData("Metal", "javax.swing.plaf.metal.MetalLookAndFeel", "Metal Look and Feel"),
        new LookAndFeelData("Nimbus", "javax.swing.plaf.nimbus.NimbusLookAndFeel", "Nimbus Look and Feel"),
        new LookAndFeelData("Windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel", "Windows Look and Feel"),
        new LookAndFeelData("System", UIManager.getSystemLookAndFeelClassName(), "System Look and Feel")
    };
'@

# Find position after "private BufferedReader bufferedReader;" and insert
$pattern = '(private BufferedReader bufferedReader;)'
$replacement = "$1$fieldsToAdd"
$content = $content -replace $pattern, $replacement

# Add missing imports
$importPattern = '(package org\.jgame\.server;)'
$importAddition = @'
$1

import org.jgame.ui.AboutDialog;
import org.jgame.ui.LookAndFeelData;
import org.jgame.ui.ChangeLookAndFeelAction;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
'@
$content = $content -replace $importPattern, $importAddition

[System.IO.File]::WriteAllText((Resolve-Path $file).Path, $content, [System.Text.UTF8Encoding]::new($false))

Write-Host "Added missing fields and imports to GameServer.java"
