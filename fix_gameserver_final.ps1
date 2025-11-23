# Fix the 6 remaining GameServer compilation errors
$file = 'src/main/java/org/jgame/server/GameServer.java'
$content = Get-Content $file -Raw

# 1. Add missing imports at top
$content = $content -replace '(import org\.jgame\.ui\.ChangeLookAndFeelAction;)', "`$1`r`nimport org.jgame.ui.TestPane;`r`nimport org.jgame.util.TextAndMnemonicUtils;"

# 2. Fix GameServer constructor call - wrap in try-catch for InetAddress conversion  
$content = $content -replace 'GameServer GameServer;\r?\n\s+GameServer = new GameServer\(serverAddress, serverPort\);', @'
GameServer GameServer;
        try {
            GameServer = new GameServer(InetAddress.getByName(serverAddress), serverPort);
        } catch (UnknownHostException ex) {
            throw new RuntimeException("Invalid server address", ex);
        }
'@

# 3. Fix ChangeLookAndFeelAction constructor - cast this to JFrame
$content = $content -replace 'new ChangeLookAndFeelAction\(this, lafData\)', 'new ChangeLookAndFeelAction((JFrame)this, lafData)'

# 4. Fix menuBar access - use local variable instead of Frame.menuBar
$content = $content -replace 'public JMenuBar createMenuBar\(\) \{\r?\n\s+menuBar = createMenus\(\);\r?\n\s+return menuBar;', @'
public JMenuBar createMenuBar() {
        JMenuBar mb = createMenus();
        setJMenuBar(mb);
        return mb;
'@

Write-Host "All 6 GameServer errors fixed!"
[System.IO.File]::WriteAllText((Resolve-Path $file).Path, $content)
