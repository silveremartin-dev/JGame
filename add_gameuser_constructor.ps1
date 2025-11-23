$file = 'src/main/java/org/jgame/server/GameUser.java'
$content = Get-Content $file -Raw

# Find the closing brace of the constructor and add default constructor after it
$pattern = '(\}\r?\n)\r?\n(\s+public String getLogin)'
$replacement = "`$1`r`n    /**`r`n     * Default constructor for testing/demo purposes.`r`n     */`r`n    public GameUser() {`r`n        this(`"guest`", `"guest`");`r`n    }`r`n`r`n`$2"

$content = $content -replace $pattern, $replacement

[System.IO.File]::WriteAllText((Resolve-Path $file).Path, $content)
Write-Host "Added default constructor to GameUser"
