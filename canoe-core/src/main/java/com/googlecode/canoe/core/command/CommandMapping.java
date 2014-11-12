package com.googlecode.canoe.core.command;

import java.util.Collection;

public interface CommandMapping {
	CommandEntry getCommandEntry(int opcode);
        Collection<CallbackEntry> getCallbackEntries(CallbackEvent type);
}
