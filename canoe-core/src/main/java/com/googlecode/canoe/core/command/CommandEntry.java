/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.googlecode.canoe.core.command;

/**
 *
 * @author panzd
 */
public class CommandEntry {
    private int opCode;
    private String name;
    private Command command;
    private boolean roleRequired;

    public CommandEntry(short opCode, String name, Command command, boolean roleRequired)
    {
        this.opCode = opCode;
        this.name = name;
        this.command = command;
        this.roleRequired = roleRequired;
    }
    /**
     * @return the opCode
     */
    public int getOpCode() {
        return opCode;
    }
    /**
     * @return the command
     */
    public Command getCommand() {
        return command;
    }

    /**
     * @return the roleRequired
     */
    public boolean isRoleRequired() {
        return roleRequired;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Command[" + opCode + "][" + (roleRequired ? "roleRequired" : "unlimited") + "]->" + name;
    }
}
