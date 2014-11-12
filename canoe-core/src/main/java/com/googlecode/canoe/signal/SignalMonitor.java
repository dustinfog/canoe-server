package com.googlecode.canoe.signal;

import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.SignalHandler;

@SuppressWarnings("restriction")
public class SignalMonitor extends Observable implements SignalHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(SignalMonitor.class);

	public void handleSignal(Signal signal)
	{
		try {
			sun.misc.Signal.handle(new sun.misc.Signal(signal.name()), this);
		} catch (Throwable x) {
			logger.error(x.getMessage(), x);
		}
	}

	public void handle(sun.misc.Signal signal) {
		setChanged();
		notifyObservers(Signal.valueOf(signal.getName()));
	}
}
