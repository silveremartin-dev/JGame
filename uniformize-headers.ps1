#!/usr/bin/env pwsh
# Script to uniformize MIT license headers across all Java files
# Authors: Silvere Martin-Michiellot, Google Gemini (Antigravity)

$rootDir = $PSScriptRoot
$standardHeader = @'
/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot, Google Gemini (Antigravity)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

'@

Write-Host "Uniformizing MIT License headers..." -ForegroundColor Cyan
Write-Host "Root directory: $rootDir" -ForegroundColor Gray

$modules = @("jgame-core", "jgame-games", "jgame-server", "jgame-client-java")
$totalFiles = 0
$updatedFiles = 0

foreach ($module in $modules) {
    $modulePath = Join-Path $rootDir $module
    if (-not (Test-Path $modulePath)) {
        Write-Host "  Skipping $module (not found)" -ForegroundColor Yellow
        continue
    }
    
    Write-Host "`nProcessing module: $module" -ForegroundColor Green
    
    $javaFiles = Get-ChildItem -Path $modulePath -Filter "*.java" -Recurse
    
    foreach ($file in $javaFiles) {
        $totalFiles++
        $content = Get-Content $file.FullName -Raw
        
        # Check if file has a header
        if ($content -match '(?s)^/\*.*?\*/\s*') {
            # Remove old header
            $contentWithoutHeader = $content -replace '(?s)^/\*.*?\*/\s*', ''
            
            # Add new standard header
            $newContent = $standardHeader + $contentWithoutHeader
            
            # Write back to file
            Set-Content -Path $file.FullName -Value $newContent -NoNewline
            $updatedFiles++
            Write-Host "  Updated: $($file.Name)" -ForegroundColor Gray
        }
        elseif ($content -notmatch '^\s*package\s+org\.jgame') {
            # File without header and without package statement - add header
            $newContent = $standardHeader + $content
            Set-Content -Path $file.FullName -Value $newContent -NoNewline
            $updatedFiles++
            Write-Host "  Added header: $($file.Name)" -ForegroundColor Gray
        }
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "License header uniformization complete!" -ForegroundColor Green
Write-Host "Total files processed: $totalFiles" -ForegroundColor White
Write-Host "Files updated: $updatedFiles" -ForegroundColor White
Write-Host "========================================" -ForegroundColor Cyan
