/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.googlecode.canoe.core.command.support;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.canoe.core.command.CallbackEntry;
import com.googlecode.canoe.core.command.CallbackEvent;
import com.googlecode.canoe.core.command.CommandEntry;
import com.googlecode.canoe.core.command.CommandHelper;
import com.googlecode.canoe.core.command.CommandMapping;

/**
 *
 * @author panzd
 */
public abstract class AbstractCommandMapping implements CommandMapping {
    private Map<Integer, CommandEntry> commandEntryMap;
    private CommandEntry[] commandEntries;
    private EnumMap<CallbackEvent, List<CallbackEntry>> callbackEntriesMap;

    protected Map<Integer, CommandEntry> getCommandEntryMap()
    {
        return commandEntryMap;
    }

    /**
     * @return the callbackEntriesMap
     */
    protected EnumMap<CallbackEvent, List<CallbackEntry>> getCallbackEntriesMap() {
        return callbackEntriesMap;
    }

    public AbstractCommandMapping()
    {
        commandEntryMap = new HashMap<Integer, CommandEntry>();
        callbackEntriesMap = new EnumMap<CallbackEvent, List<CallbackEntry>>(CallbackEvent.class);
    }

    public CommandEntry getCommandEntry(int opcode) {
        if(commandEntries != null && commandEntries.length > opcode)
        {
            return commandEntries[opcode];
        }

        return commandEntryMap.get(opcode);
    }

    public List<CallbackEntry> getCallbackEntries(CallbackEvent type) {
        return getCallbackEntriesMap().get(type);
    }

    protected void commitChanges()
    {
        CommandHelper.sortCallbackEntries(callbackEntriesMap);
        commandEntries = CommandHelper.createCommandEntryArray(getCommandEntryMap());
    }
}
