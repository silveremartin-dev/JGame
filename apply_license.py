#!/usr/bin/env python3
"""
Apply MIT License headers to all Java source files.
Preserves existing package and import statements.
"""

import os
import re
from pathlib import Path

LICENSE_HEADER = """/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
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
 *
 * Enhanced with AI assistance from Google Gemini (Antigravity)
 */
"""

def apply_license_to_file(file_path):
    """Apply MIT license header to a single Java file."""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Skip if already has MIT license
    if 'MIT License' in content:
        return False
    
    # Remove old license header (Copyright block)
    content = re.sub(r'/\*.*?Copyright.*?\*/', '', content, flags=re.DOTALL)
    
    # Trim leading whitespace
    content = content.lstrip()
    
    # Add new MIT license header
    new_content = LICENSE_HEADER + '\n\n' + content
    
    # Write back
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(new_content)
    
    return True

def main():
    """Apply license headers to all Java files."""
    count = 0
    skipped = 0
    
    # Find all Java files
    java_files = list(Path('src/main/java').rglob('*.java'))
    test_files = list(Path('src/test/java').rglob('*.java')) if Path('src/test/java').exists() else []
    all_files = java_files + test_files
    
    for file_path in all_files:
        if apply_license_to_file(file_path):
            print(f"Updated: {file_path.name}")
            count += 1
        else:
            print(f"Skipped: {file_path.name} - already has MIT license")
            skipped += 1
    
    print(f"\n✓ Applied MIT license to {count} Java files")
    print(f"✓ Skipped {skipped} files (already licensed)")

if __name__ == '__main__':
    main()
