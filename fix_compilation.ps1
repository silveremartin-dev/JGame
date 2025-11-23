# Fix GameClient.java
$gameclient = 'src/main/java/org/jgame/client/GameClient.java'
$content = [System.IO.File]::ReadAllText($gameclient)

# Fix registerAccount
$pattern1 = 'public GameUser registerAccount\(\) \{[\s\r\n]+PasswordEncoderSingleton\.hashPassword\(\)[\s\r\n]+\}'
$replace1 = @'
public GameUser registerAccount() {
        // TODO: Implement account registration
        throw new UnsupportedOperationException("Not yet implemented");
    }
'@
$content = $content -replace $pattern1, $replace1

# Fix changePassword
$pattern2 = 'public GameUser changePassword\(\) \{[\s\r\n]+PasswordEncoderSingleton\.hashPassword\(\)[\s\r\n]+\}'
$replace2 = @'
public GameUser changePassword() {
        // TODO: Implement password change
        throw new UnsupportedOperationException("Not yet implemented");
    }
'@
$content = $content -replace $pattern2, $replace2

# Fix solver comment
$content = $content -replace '//choose solver[\s\r\n]+BasicGameSolver[\s\r\n]+AdvancedGameSolver', '// TODO: Choose solver (BasicGameSolver vs AdvancedGameSolver)'

[System.IO.File]::WriteAllText((Resolve-Path $gameclient).Path, $content)

# Fix GameServer.java  
$gameserver = 'src/main/java/org/jgame/server/GameServer.java'
$lines = Get-Content $gameserver

for ($i = 0; $i -lt $lines.Count; $i++) {
    if ($lines[$i] -match 'private static final String ServerCommandRegister = "LOGIN"') {
        $lines[$i] = $lines[$i] -replace 'ServerCommandRegister', 'ServerCommandLogin'
    }
    elseif ($lines[$i] -match 'private static final String ServerCommandRegister = "LOGOUT"') {
        $lines[$i] = $lines[$i] -replace 'ServerCommandRegister', 'ServerCommandLogout'
    }
    elseif ($lines[$i] -match 'private static final String ServerCommandRegister = "GETRANKED"') {
        $lines[$i] = $lines[$i] -replace 'ServerCommandRegister', 'ServerCommandGetRanked'
    }
    elseif ($lines[$i] -match 'private DataOutputStream dataOutputStream\);') {
        $lines[$i] = $lines[$i] -replace '\);', ';'
    }
}

$lines | Set-Content $gameserver

Write-Host "Files fixed successfully"
