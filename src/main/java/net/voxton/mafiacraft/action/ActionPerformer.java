/*
 * This file is part of Mafiacraft.
 * 
 * Mafiacraft is released under the Voxton License version 1.
 *
 * Mafiacraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition to this, you must also specify that this product includes 
 * software developed by Voxton.net and may not remove any code
 * referencing Voxton.net directly or indirectly.
 * 
 * Mafiacraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
 */
package net.voxton.mafiacraft.action;

import net.voxton.mafiacraft.locale.Locale;

/**
 * Interface for entities that can perform actions.
 */
public interface ActionPerformer {
    /**
     * Sends a message to the {@link ActionPerformer}.
     * 
     * @param message The message to send.
     */
    public void sendMessage(String message);
    
    /**
     * Gets the locale of the {@link ActionPerformer}.
     * 
     * @return The {@link Locale}.
     */
    public Locale getLocale();
}
