import re

# Fix GameClient.java
with open('src/main/java/org/jgame/client/GameClient.java', 'r', encoding='utf-8') as f:
    content = f.read()

# Remove the char getMnemonic method (line ~481) - keep the static int version
content = re.sub(
    r'    /\*\*\s+\* Returns a mnemonic from the resource bundle.*?\n    \*/\s+public char getMnemonic\(String key\) \{\s+return \(getString\(key\)\)\.charAt\(0\);\s+\}',
    '',
    content,
    flags=re.DOTALL
)

# Fix the loadModules method reference issue - change from instance reference to static call
content = content.replace(
    'SwingUtilities.invokeLater(GameClientApplication::loadModules);',
    'SwingUtilities.invokeLater(() -> GameClient.loadModules());'
)

# Add missing methods: getInputMap and getActionMap (as stubs since GameClient extends JFrame, not JComponent)
insert_pos = content.find('    /**\n     * Loads game modules.')
if insert_pos > 0:
    stub_methods = '''    /**
     * Gets the input map for this component.
     * Note: GameClient extends JFrame, not JComponent, so this delegates to root pane.
     */
    public InputMap getInputMap(int condition) {
        return getRootPane().getInputMap(condition);
    }
    
    /**
     * Gets the action map for this component.
     * Note: GameClient extends JFrame, not JComponent, so this delegates to root pane.
     */
    public ActionMap getActionMap() {
        return getRootPane().getActionMap();
    }
    
'''
    content = content[:insert_pos] + stub_methods + content[insert_pos:]

# Fix getMenuBar return type issue - rename to getJMenuBar to avoid conflict
content = content.replace(
    'public JMenuBar getMenuBar() {',
    'public JMenuBar getJMenuBar() {'
)

with open('src/main/java/org/jgame/client/GameClient.java', 'w', encoding='utf-8', newline='\r\n') as f:
    f.write(content)

print("Fixed GameClient.java")

# Fix GameServer.java - similar issues
with open('src/main/java/org/jgame/server/GameServer.java', 'r', encoding='utf-8') as f:
    content = f.read()

# Remove duplicate getMnemonic if exists
content = re.sub(
    r'    /\*\*\s+\* Returns a mnemonic from the resource bundle.*?\n    \*/\s+public char getMnemonic\(String key\) \{\s+return \(getString\(key\)\)\.charAt\(0\);\s+\}',
    '',
    content,
    flags=re.DOTALL
)

# Fix loadModules method reference
content = content.replace(
    'SwingUtilities.invokeLater(GameServerApplication::loadModules);',
    'SwingUtilities.invokeLater(() -> GameServer.loadModules());'
)

# Add missing methods
insert_pos = content.find('    /**\n     * Loads game modules.')
if insert_pos > 0:
    stub_methods = '''    /**
     * Gets the input map for this component.
     */
    public InputMap getInputMap(int condition) {
        return getRootPane().getInputMap(condition);
    }
    
    /**
     * Gets the action map for this component.
     */
    public ActionMap getActionMap() {
        return getRootPane().getActionMap();
    }
    
'''
    content = content[:insert_pos] + stub_methods + content[insert_pos:]

# Fix getMenuBar return type issue
content = content.replace(
    'public JMenuBar getMenuBar() {',
    'public JMenuBar getJMenuBar() {'
)

# Fix menuBar access issue - use setJMenuBar instead
content = content.replace(
    'menuBar = createMenuBar();',
    'setJMenuBar(createMenuBar());'
)

with open('src/main/java/org/jgame/server/GameServer.java', 'w', encoding='utf-8', newline='\r\n') as f:
    f.write(content)

print("Fixed GameServer.java")

# Fix GooseRules.java - setId() needs an argument
with open('src/main/java/org/jgame/logic/games/goose/GooseRules.java', 'r', encoding='utf-8') as f:
    content = f.read()

# Fix setId() to pass a player ID
content = content.replace(
    'currentPlayer.setId();',
    'currentPlayer.setId("player_" + i);'
)

with open('src/main/java/org/jgame/logic/games/goose/GooseRules.java', 'w', encoding='utf-8', newline='\r\n') as f:
    f.write(content)

print("Fixed GooseRules.java")

print("\nAll fixes applied!")
