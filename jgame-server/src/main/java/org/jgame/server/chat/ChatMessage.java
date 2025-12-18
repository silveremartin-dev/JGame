/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
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
