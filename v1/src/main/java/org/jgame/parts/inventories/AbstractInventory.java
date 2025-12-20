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

package org.jgame.parts.inventories;

import org.jgame.parts.InventoryInterface;
import org.jgame.parts.PieceInterface;

import java.util.Set;

public abstract class AbstractInventory implements InventoryInterface {

    private Set<PieceInterface> contents;

    @Override
    public Set<PieceInterface> getContents() {
        return contents;
    }

    public void setContents(Set<PieceInterface> contents) {
        this.contents = contents;
    }

}
