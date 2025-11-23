import re

def fix_game_client():
    with open('src/main/java/org/jgame/client/GameClient.java', 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Fix registerAccount method
    content = re.sub(
        r'public GameUser registerAccount\(\) \{\s+PasswordEncoderSingleton\.hashPassword\(\)\s+\}',
        'public GameUser registerAccount() {\n        // TODO: Implement account registration\n        throw new UnsupportedOperationException("Not yet implemented");\n    }',
        content,
        flags=re.DOTALL
    )
    
    # Fix changePassword method
    content = re.sub(
        r'public GameUser changePassword\(\) \{\s+PasswordEncoderSingleton\.hashPassword\(\)\s+\}',
        'public GameUser changePassword() {\n        // TODO: Implement password change\n        throw new UnsupportedOperationException("Not yet implemented");\n    }',
        content,
        flags=re.DOTALL
    )
    
    # Fix solver pseudocode
    content = re.sub(
        r'//choose solver\s+BasicGameSolver\s+AdvancedGameSolver',
        '// TODO: Choose solver (BasicGameSolver vs AdvancedGameSolver)',
        content
    )
    
    with open('src/main/java/org/jgame/client/GameClient.java', 'w', encoding='utf-8') as f:
        f.write(content)
    print("GameClient.java fixed")

def fix_game_server():
    with open('src/main/java/org/jgame/server/GameServer.java', 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    # Fix duplicate variable names and extra parenthesis
    for i in range(len(lines)):
        if 'private static final String ServerCommandRegister = "LOGIN"' in lines[i]:
            lines[i] = lines[i].replace('ServerCommandRegister', 'ServerCommandLogin')
        elif 'private static final String ServerCommandRegister = "LOGOUT"' in lines[i]:
            lines[i] = lines[i].replace('ServerCommandRegister', 'ServerCommandLogout')
        elif 'private static final String ServerCommandRegister = "GETRANKED"' in lines[i]:
            lines[i] = lines[i].replace('ServerCommandRegister', 'ServerCommandGetRanked')
        elif 'private DataOutputStream dataOutputStream);' in lines[i]:
            lines[i] = lines[i].replace(');', ';')
    
    with open('src/main/java/org/jgame/server/GameServer.java', 'w', encoding='utf-8') as f:
        f.writelines(lines)
    print("GameServer.java fixed")

if __name__ == '__main__':
    fix_game_client()
    fix_game_server()
    print("All files fixed successfully")
