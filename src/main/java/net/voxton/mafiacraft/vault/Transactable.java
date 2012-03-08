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
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
 */
package net.voxton.mafiacraft.vault;

/**
 * Represents an object that can be transacted with.
 */
public abstract class Transactable {

    private double money;

    /**
     * Gets the money of this Transactable.
     *
     * @return
     */
    public double getMoney() {
        return money;
    }

    public double setMoney(double amt) {
        money = amt;
        return money;
    }

    public double addMoney(double amt) {
        return setMoney(getMoney() + amt);
    }

    public double subtractMoney(double amt) {
        return setMoney(getMoney() - amt);
    }

    /**
     * Transfers money to another Transactable. This does not check for negative
     * money.
     *
     * @param other
     * @param amt
     * @return
     */
    public Transactable transferMoney(Transactable other, double amt) {
        subtractMoney(amt);
        other.addMoney(amt);
        return this;
    }

    /**
     * Returns true if the thing has enough.
     *
     * @param amt
     * @return
     */
    public boolean hasEnough(double amt) {
        return getMoney() >= amt;
    }

    /**
     * Transfers money with checking for enough money first.
     *
     * @param other
     * @param amt
     * @return
     */
    public boolean transferWithCheck(Transactable other, double amt) {
        if (!hasEnough(amt)) {
            return false;
        }

        transferMoney(other, amt);
        return true;
    }

}
