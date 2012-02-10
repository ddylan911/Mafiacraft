/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

/**
 *
 * @author simplyianm
 */
public class GovType {
    public static GovType MAFIA;
    public static GovType POLICE;
    
    static {
        MAFIA = new GovType();
        POLICE = new GovType();
    }
}
