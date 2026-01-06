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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgame.server.security.HtmlSanitizer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages chat messages for lobbies.
 * 
 * <p>
 * Uses HtmlSanitizer for comprehensive XSS protection.
 * </p>
 *
 * @author Google Gemini (Antigravity)
 * @author Silvere Martin-Michiellot
 * @version 2.0
 */
public class ChatManager {

    private static final Logger logger = LogManager.getLogger(ChatManager.class);
    private static final int MAX_MESSAGES_PER_LOBBY = 100;
    private static final int MAX_MESSAGE_LENGTH = 500;
    private static ChatManager instance;

    private final Map<String, List<ChatMessage>> lobbyMessages = new ConcurrentHashMap<>();
    private final List<ChatListener> listeners = new CopyOnWriteArrayList<>();

    public interface ChatListener {
        void onMessage(ChatMessage message);
    }

    private ChatManager() {
    }

    public static synchronized ChatManager getInstance() {
        if (instance == null) {
            instance = new ChatManager();
        }
        return instance;
    }

    /**
     * Sends a chat message.
     */
    public void sendMessage(String senderId, String senderName, String lobbyId, String content) {
        if (content == null || content.isBlank())
            return;

        // Comprehensive sanitization using HtmlSanitizer
        String sanitized = HtmlSanitizer.sanitizeAndTruncate(content, MAX_MESSAGE_LENGTH);
        ChatMessage message = ChatMessage.chat(senderId, senderName, lobbyId, sanitized);
        addMessage(lobbyId, message);
    }

    /**
     * Sends a system message.
     */
    public void sendSystemMessage(String lobbyId, String content) {
        ChatMessage message = ChatMessage.system(lobbyId, content);
        addMessage(lobbyId, message);
    }

    /**
     * Notifies of player join.
     */
    public void notifyJoin(String lobbyId, String playerName) {
        ChatMessage message = ChatMessage.join(lobbyId, playerName);
        addMessage(lobbyId, message);
    }

    /**
     * Notifies of player leave.
     */
    public void notifyLeave(String lobbyId, String playerName) {
        ChatMessage message = ChatMessage.leave(lobbyId, playerName);
        addMessage(lobbyId, message);
    }

    private void addMessage(String lobbyId, ChatMessage message) {
        List<ChatMessage> messages = lobbyMessages.computeIfAbsent(
                lobbyId, k -> new CopyOnWriteArrayList<>());

        messages.add(message);

        // Trim old messages
        while (messages.size() > MAX_MESSAGES_PER_LOBBY) {
            messages.remove(0);
        }

        // Notify listeners
        for (ChatListener listener : listeners) {
            try {
                listener.onMessage(message);
            } catch (Exception e) {
                logger.warn("Chat listener error: {}", e.getMessage());
            }
        }

        logger.debug("Chat message in {}: {}", lobbyId, message.content());
    }

    /**
     * Gets recent messages for a lobby.
     */
    public List<ChatMessage> getMessages(String lobbyId, int limit) {
        List<ChatMessage> messages = lobbyMessages.get(lobbyId);
        if (messages == null)
            return List.of();

        int start = Math.max(0, messages.size() - limit);
        return new ArrayList<>(messages.subList(start, messages.size()));
    }

    /**
     * Clears messages for a lobby.
     */
    public void clearLobby(String lobbyId) {
        lobbyMessages.remove(lobbyId);
    }

    /**
     * Adds a chat listener.
     */
    public void addListener(ChatListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a chat listener.
     */
    public void removeListener(ChatListener listener) {
        listeners.remove(listener);
    }
}
