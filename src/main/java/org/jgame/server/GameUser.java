/*
 * Copyright 2022 Silvere Martin-Michiellot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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