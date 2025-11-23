# Fix remaining compilation errors

# 1. Add loadModules stub method to GameClient
$clientFile = 'src/main/java/org/jgame/client/GameClient.java'
$content = Get-Content $clientFile -Raw

# Add loadModules method before the last closing brace
$loadModulesMethod = @'

    /**
     * Loads game modules.
     * TODO: Implement module loading functionality
     */
    public static void loadModules() {
        // TODO: Implement module loading
    }

    /**
     * Gets mnemonic from resource key.
     * @param key the resource key
     * @return the mnemonic character code
     */
    public static int getMnemonic(String key) {
        return TextAndMnemonicUtils.getMnemonic(key);
    }
'@

$content = $content -replace '(\}\s*)$', "$loadModulesMethod`n}`n"
[System.IO.File]::WriteAllText((Resolve-Path $clientFile).Path, $content)

# 2. Add same methods to GameServer
$serverFile = 'src/main/java/org/jgame/server/GameServer.java'
$serverContent = Get-Content $serverFile -Raw

$serverContent = $serverContent -replace '(\}\s*)$', "$loadModulesMethod`n}`n"
[System.IO.File]::WriteAllText((Resolve-Path $serverFile).Path, $serverContent)

# 3. Add bufferedReader field to GameServer
$pattern = '(private DataOutputStream dataOutputStream;)'
$replacement = @'
$1
    private BufferedReader bufferedReader;
'@
$serverContent2 = Get-Content $serverFile -Raw
$serverContent2 = $serverContent2 -replace $pattern, $replacement
[System.IO.File]::WriteAllText((Resolve-Path $serverFile).Path, $serverContent2)

Write-Host "Added stub methods and missing fields"
