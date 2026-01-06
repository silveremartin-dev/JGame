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
package org.jgame.server.chat;

import java.time.Instant;

/**
 * Represents a chat message.
 *
 * @author Google Gemini (Antigravity)
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public record ChatMessage(
        String id,
        String senderId,
        String senderName,
        String lobbyId,
        String content,
        MessageType type,
        Instant timestamp) {
    public enum MessageType {
        CHAT, // Regular chat message
        SYSTEM, // System notification
        JOIN, // Player joined
        LEAVE, // Player left
        GAME_START, // Game started
        GAME_END // Game ended
    }

    public static ChatMessage chat(String senderId, String senderName, String lobbyId, String content) {
        return new ChatMessage(
                java.util.UUID.randomUUID().toString(),
                senderId, senderName, lobbyId, content,
                MessageType.CHAT, Instant.now());
    }

    public static ChatMessage system(String lobbyId, String content) {
        return new ChatMessage(
                java.util.UUID.randomUUID().toString(),
                "system", "System", lobbyId, content,
                MessageType.SYSTEM, Instant.now());
    }

    public static ChatMessage join(String lobbyId, String playerName) {
        return new ChatMessage(
                java.util.UUID.randomUUID().toString(),
                "system", "System", lobbyId, playerName + " joined the lobby",
                MessageType.JOIN, Instant.now());
    }

    public static ChatMessage leave(String lobbyId, String playerName) {
        return new ChatMessage(
                java.util.UUID.randomUUID().toString(),
                "system", "System", lobbyId, playerName + " left the lobby",
                MessageType.LEAVE, Instant.now());
    }
}
