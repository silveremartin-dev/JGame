# Apply MIT License headers to all Java files
# Preserves existing package and import statements

$licenseHeader = Get-Content 'license_header.txt' -Raw

# Get all Java files
$javaFiles = Get-ChildItem -Path 'src/main/java' -Filter '*.java' -Recurse
$javaTestFiles = Get-ChildItem -Path 'src/test/java' -Filter '*.java' -Recurse -ErrorAction SilentlyContinue
$allFiles = $javaFiles + $javaTestFiles

$count = 0
foreach ($file in $allFiles) {
    $content = Get-Content $file.FullName -Raw
    
    # Skip if already has MIT license
    if ($content -match 'MIT License') {
        Write-Host "Skipping $($file.Name) - already has MIT license"
        continue
    }
    
    # Remove old license header if exists
    $content = $content -replace '(?s)/\*.*?Copyright.*?\*/', ''
    
    # Trim leading whitespace
    $content = $content.TrimStart()
    
    # Add new MIT license header
    $newContent = $licenseHeader + "`r`n`r`n" + $content
    
    # Write back
    [System.IO.File]::WriteAllText($file.FullName, $newContent, [System.Text.UTF8Encoding]::new($false))
    
    $count++
    Write-Host "Updated: $($file.Name)"
}

Write-Host "`n✓ Applied MIT license  to $count Java files"
