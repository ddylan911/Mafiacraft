/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.vault;

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
     * Transfers money to another Transactable.
     * This does not check for negative money.
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
