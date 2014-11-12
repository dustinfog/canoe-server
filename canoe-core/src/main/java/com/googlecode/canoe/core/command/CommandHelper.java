/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.canoe.core.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.reflect.MethodDelegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.canoe.core.command.annotation.CanoeCallback;
import com.googlecode.canoe.core.command.annotation.CanoeCallbacks;
import com.googlecode.canoe.core.command.annotation.CanoeCommand;
import com.googlecode.canoe.core.exception.ServerException;
import com.googlecode.canoe.core.message.Request;
import com.googlecode.canoe.core.message.ResponseSender;
import com.googlecode.canoe.core.session.Session;

/**
 *
 * @author panzd
 */
public class CommandHelper {

	public static Logger log = LoggerFactory.getLogger(CommandHelper.class);

	public static void fetchCommands(Object object,
			Map<Integer, CommandEntry> commandEntryMap,
			EnumMap<CallbackEvent, List<CallbackEntry>> callbackEntriesMap) {
		Method[] methods = object.getClass().getMethods();
		for (Method method : methods) {
			CommandEntry commandEntry = createCommandEntry(object, method);
			if (commandEntry != null) {
				commandEntryMap.put(commandEntry.getOpCode(), commandEntry);
			}

			CallbackEntry[] callbackEntries = createCallbackEntries(object,
					method);
			if (callbackEntries != null) {
				for (CallbackEntry callbackEntry : callbackEntries) {
					saveCallbackEntry(callbackEntriesMap, callbackEntry);
				}
			} else {
				saveCallbackEntry(callbackEntriesMap,
						createCallbackEntry(object, method));
			}
		}
	}

	public static CommandEntry[] createCommandEntryArray(
			Map<Integer, CommandEntry> commandEntryMap) {
		Set<Integer> opCodes = commandEntryMap.keySet();

		int maxOpcode = 0;
		for (int opCode : opCodes) {
			if (opCode > maxOpcode) {
				maxOpcode = opCode;
			}
		}

		CommandEntry[] commandEntries = new CommandEntry[maxOpcode + 1];

		for (int opCode : opCodes) {
			commandEntries[opCode] = commandEntryMap.get(opCode);
		}

		return commandEntries;
	}

	public static void sortCallbackEntries(
			EnumMap<CallbackEvent, List<CallbackEntry>> callbackEntriesMap) {
		Set<CallbackEvent> eventSet = callbackEntriesMap.keySet();

		for (CallbackEvent event : eventSet) {
			List<CallbackEntry> callbackEntries = callbackEntriesMap.get(event);
			Collections.sort(callbackEntries);
		}
	}

	private static String createCommandName(Object object, Method method) {
		return object.getClass().getName() + "." + method.getName();
	}

	private static CommandEntry createCommandEntry(Object object, Method method) {
		CanoeCommand commandAnnotation = (CanoeCommand) method
				.getAnnotation(CanoeCommand.class);

		if (commandAnnotation != null) {
			short opCode = commandAnnotation.value();
			Command command = createMethodCommand(object, method);
			CommandEntry commandEntry = new CommandEntry(opCode,
					createCommandName(object, method), command,
					commandAnnotation.roleRequired());

			log.info("created " + commandEntry);

			return commandEntry;
		}

		return null;
	}

	private static void saveCallbackEntry(
			EnumMap<CallbackEvent, List<CallbackEntry>> callbackEntriesMap,
			CallbackEntry callbackEntry) {
		if (callbackEntry == null) {
			return;
		}

		CallbackEvent event = callbackEntry.getEvent();

		List<CallbackEntry> callbackEntries = callbackEntriesMap.get(event);
		if (callbackEntries == null) {
			callbackEntries = new ArrayList<CallbackEntry>();
			callbackEntriesMap.put(event, callbackEntries);
		}

		callbackEntries.add(callbackEntry);
	}

	private static CallbackEntry[] createCallbackEntries(Object object,
			Method method) {
		CanoeCallbacks callbacksAnnotation = (CanoeCallbacks) method
				.getAnnotation(CanoeCallbacks.class);
		if (callbacksAnnotation == null) {
			return null;
		}

		CanoeCallback[] callbackAnnotations = callbacksAnnotation.value();
		if (callbackAnnotations == null || callbackAnnotations.length == 0) {
			return null;
		}

		Callback callback = createMethodCallback(object, method);

		CallbackEntry[] callbackEntrys = new CallbackEntry[callbackAnnotations.length];
		for (int i = 0; i < callbackAnnotations.length; i++) {
			CanoeCallback callbackAnnotation = callbackAnnotations[i];
			callbackEntrys[i] = createCallbackEntry(object, method, callback,
					callbackAnnotation);
		}

		return callbackEntrys;
	}

	private static CallbackEntry createCallbackEntry(Object object,
			Method method) {
		CanoeCallback callbackAnnotation = (CanoeCallback) method
				.getAnnotation(CanoeCallback.class);
		if (callbackAnnotation != null) {
			Callback callback = createMethodCallback(object, method);
			return createCallbackEntry(object, method, callback,
					callbackAnnotation);
		}

		return null;
	}

	private static CallbackEntry createCallbackEntry(Object object,
			Method method, Callback callback, CanoeCallback callbackAnnotation) {
		CallbackEvent event = callbackAnnotation.value();
		int priority = callbackAnnotation.priority();

		CallbackEntry callbackEntry = new CallbackEntry(createCommandName(
				object, method), event, priority, callback);

		log.info("created " + callbackEntry);
		return callbackEntry;
	}

	private static Callback createMethodCallback(Object object, Method method) {
		String methodName = method.getName();

		Class<?>[] parameterTypes = method.getParameterTypes();
		Class<?> returnType = method.getReturnType();

		if (parameterTypes.length != 2 || parameterTypes[0] != Session.class || parameterTypes[1] != ResponseSender.class
				|| returnType != void.class) {

			throw new ServerException("create callback from "
					+ object.getClass().getName() + "." + method.getName()
					+ " error");
		}

		return (Callback) MethodDelegate.create(object, methodName,
				Callback.class);
	}

	private static Command createMethodCommand(Object object, Method method) {
		String methodName = method.getName();

		Class<?>[] parameterTypes = method.getParameterTypes();
		Class<?> returnType = method.getReturnType();
		if (parameterTypes.length != 2
				|| parameterTypes[0] != Request.class
				|| parameterTypes[1] != ResponseSender.class
				|| (returnType != void.class)) {
			throw new ServerException("create command from "
					+ object.getClass().getName() + "." + method.getName()
					+ " error");
		}

		return (Command) MethodDelegate.create(object, methodName,
				Command.class);
	}
}