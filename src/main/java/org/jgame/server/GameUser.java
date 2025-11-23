/*
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


package org.jgame.server;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Some abstract information to communicate in a reliable manner between the client and the server and some statistics too.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

public class GameUser {

    private String login;
    private String password;
    private List<GamePacket> packets;
    private String hardwareArchitecture;
    private String operatingSystem;
    private String operatingSystemVersion;

    public GameUser(@NotNull String login, @NotNull String password) {
        if ((login != null) && (password != null)) {
            this.login = login;
            this.password = password;
            packets = new ArrayList<>();
            hardwareArchitecture = System.getProperty("os.arch");
            operatingSystem = System.getProperty("os.name");
            operatingSystemVersion = System.getProperty("os.version");
        } else throw new IllegalArgumentException("You cannot set a null login or null password.");
    }

    /**
     * Default constructor for testing/demo purposes.
     */
    public GameUser() {
        this("guest", "guest");
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(@NotNull String login) {
        if (login != null) {
            this.login = login;
        } else throw new IllegalArgumentException("You cannot set a null login.");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        if (password != null) {
            this.password = password;
        } else throw new IllegalArgumentException("You cannot set a null password.");
    }

    public List<GamePacket> getPackets() {
        return packets;
    }

    public void setPackets(@NotNull List<GamePacket> packets) {
        if (packets != null) {
            this.packets = packets;
        } else throw new IllegalArgumentException("You cannot set a null packets array.");
    }

    public String getHardwareArchitecture() {
        return hardwareArchitecture;
    }

    public void setHardwareArchitecture(@NotNull String hardwareArchitecture) {
        if (hardwareArchitecture != null) {
            this.hardwareArchitecture = hardwareArchitecture;
        } else throw new IllegalArgumentException("You cannot set a null hardware architecture.");
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(@NotNull String operatingSystem) {
        if (operatingSystem != null) {
            this.operatingSystem = operatingSystem;
        } else throw new IllegalArgumentException("You cannot set a null operating system.");
    }

    public String getOperatingSystemVersion() {
        return operatingSystemVersion;
    }

    public void setOperatingSystemVersion(@NotNull String operatingSystemVersion) {
        if (operatingSystemVersion != null) {
            this.operatingSystemVersion = operatingSystemVersion;
        } else throw new IllegalArgumentException("You cannot set a null operating system version.");
    }

}