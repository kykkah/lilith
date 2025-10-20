/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2017 Joern Huxhorn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Copyright 2007-2017 Joern Huxhorn
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

package de.huxhorn.lilith.data.logging;

import de.huxhorn.lilith.data.eventsource.LoggerContext;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

/**
 * <p>Replacement for ch.qos.logback.classic.spi.LoggingEvent</p>
 *
 * <p>This class has been implemented for optimized serialization (size) and creation (speed).</p>
 *
 * <p>Only the unformatted message pattern and the argument array are serialized. The formatted message is (re)created
 * lazily on demand.</p>
 *
 * <p>Additionally, a LoggingEvent can contain an optional applicationId so it's possible to recognize the application
 * the events are originating from. This is extremely useful if more than one application is running on the same
 * host.</p>
 */
@SuppressWarnings({"PMD.MethodReturnsInternalArray", "PMD.ArrayIsStoredDirectly"})
public class LoggingEvent
	implements Serializable
{
	private static final long serialVersionUID = -2135999771611827603L;

	/**
	 * Severity levels supported by Lilith.
	 */
	public enum Level
	{
		/** Finest-grained diagnostic information. */
		TRACE,
		/** Debugging information helpful during development. */
		DEBUG,
		/** General informational messages. */
		INFO,
		/** Potentially harmful situations. */
		WARN,
		/** Error events that may still allow the application to continue running. */
		ERROR
	}

	/** Severity level of the event. */
	private Level level;
	/** Message template and arguments describing the event. */
	private Message message;
	/** Name of the logger that produced the event. */
	private String logger;
	/** Captured throwable information if the event logged an exception. */
	private ThrowableInfo throwable;
	/** Captured call stack when the event was created. */
	private ExtendedStackTraceElement[] callStack;
	/** Mapped diagnostic context values associated with the event. */
	private Map<String, String> mdc;
	/** Nested diagnostic context entries attached to the event. */
	private Message[] ndc;
	/** Marker used to tag the event. */
	private Marker marker;
	/** Metadata about the thread emitting the event. */
	private ThreadInfo threadInfo;
	/** Context information about the logger environment. */
	private LoggerContext loggerContext;
	/** Sequence number used to maintain relative ordering. */
	private Long sequenceNumber;
	/** Event creation time in milliseconds since the epoch. */
	private Long timeStamp;

	/**
	 * Returns the logger name that created this event.
	 *
	 * @return the logger name or {@code null} if unknown
	 */
	public String getLogger()
	{
		return logger;
	}

	/**
	 * Sets the logger name that created this event.
	 *
	 * @param logger the originating logger name
	 */
	public void setLogger(String logger)
	{
		this.logger = logger;
	}

	/**
	 * Returns information about the thread that emitted the event.
	 *
	 * @return thread information or {@code null} if not captured
	 */
	public ThreadInfo getThreadInfo()
	{
		return threadInfo;
	}

	/**
	 * Updates the thread details attached to this event.
	 *
	 * @param threadInfo metadata describing the emitting thread
	 */
	public void setThreadInfo(ThreadInfo threadInfo)
	{
		this.threadInfo = threadInfo;
	}

	/**
	 * Returns the monotonically increasing sequence number assigned to the event.
	 *
	 * @return the event sequence number or {@code null} if unset
	 */
	public Long getSequenceNumber()
	{
		return sequenceNumber;
	}

	/**
	 * Sets the sequence number used to preserve event ordering.
	 *
	 * @param sequenceNumber the sequence number to use
	 */
	public void setSequenceNumber(Long sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * Returns the logger context the event belongs to.
	 *
	 * @return the logger context or {@code null} if none applies
	 */
	public LoggerContext getLoggerContext()
	{
		return loggerContext;
	}

	/**
	 * Associates the event with the given logger context.
	 *
	 * @param loggerContext context describing the emitting logger environment
	 */
	public void setLoggerContext(LoggerContext loggerContext)
	{
		this.loggerContext = loggerContext;
	}

	/**
	 * Returns the message payload.
	 *
	 * @return the logging message or {@code null} if the event carried none
	 */
	public Message getMessage()
	{
		return message;
	}

	/**
	 * Sets the message payload of this event.
	 *
	 * @param message the logging message to store
	 */
	public void setMessage(Message message)
	{
		this.message = message;
	}

	/**
	 * Returns the time the event was created.
	 *
	 * @return the timestamp in milliseconds since the epoch or {@code null} if unknown
	 */
	public Long getTimeStamp()
	{
		return timeStamp;
	}

	/**
	 * Sets the time the event was created.
	 *
	 * @param timeStamp the timestamp in milliseconds since the epoch
	 */
	public void setTimeStamp(Long timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	/**
	 * Returns the severity level of the event.
	 *
	 * @return the event level or {@code null} if unset
	 */
	public Level getLevel()
	{
		return level;
	}

	/**
	 * Sets the severity level of the event.
	 *
	 * @param level the level to assign
	 */
	public void setLevel(Level level)
	{
		this.level = level;
	}

	/**
	 * Returns the captured throwable information.
	 *
	 * @return the throwable information or {@code null} if no stack trace was attached
	 */
	public ThrowableInfo getThrowable()
	{
		return throwable;
	}

	/**
	 * Attaches throwable details to the event.
	 *
	 * @param throwable the throwable information to store
	 */
	public void setThrowable(ThrowableInfo throwable)
	{
		this.throwable = throwable;
	}

	/**
	 * Returns the mapped diagnostic context values.
	 *
	 * @return the MDC map or {@code null} if no MDC was captured
	 */
	public Map<String, String> getMdc()
	{
		return mdc;
	}

	/**
	 * Sets the mapped diagnostic context for the event.
	 *
	 * @param mdc the MDC values to store
	 */
	public void setMdc(Map<String, String> mdc)
	{
		this.mdc = mdc;
	}

	/**
	 * Returns the nested diagnostic context values.
	 *
	 * @return the NDC array or {@code null} if absent
	 */
	public Message[] getNdc()
	{
		return ndc;
	}

	/**
	 * Sets the nested diagnostic context values.
	 *
	 * @param ndc the NDC messages to store
	 */
	public void setNdc(Message[] ndc)
	{
		this.ndc = ndc;
	}

	/**
	 * Returns the marker associated with the event.
	 *
	 * @return the marker or {@code null} if none was specified
	 */
	public Marker getMarker()
	{
		return marker;
	}

	/**
	 * Sets the marker associated with the event.
	 *
	 * @param marker the marker to attach
	 */
	public void setMarker(Marker marker)
	{
		this.marker = marker;
	}

	/**
	 * Returns the captured call stack.
	 *
	 * @return the call stack or {@code null} if the event has none
	 */
	public ExtendedStackTraceElement[] getCallStack()
	{
		return callStack;
	}

	/**
	 * Sets the captured call stack.
	 *
	 * @param callStack the call stack to store
	 */
	public void setCallStack(ExtendedStackTraceElement[] callStack)
	{
		this.callStack = callStack;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LoggingEvent event = (LoggingEvent) o;

		return level == event.level
				&& (sequenceNumber != null ? sequenceNumber.equals(event.sequenceNumber) : event.sequenceNumber == null)
				&& (timeStamp != null ? timeStamp.equals(event.timeStamp) : event.timeStamp == null)
				&& (logger != null ? logger.equals(event.logger) : event.logger == null)
				&& (loggerContext != null ? loggerContext.equals(event.loggerContext) : event.loggerContext == null)
				&& (message != null ? message.equals(event.message) : event.message == null)
				&& (threadInfo != null ? threadInfo.equals(event.threadInfo) : event.threadInfo == null)
				&& Arrays.equals(callStack, event.callStack)
				&& (marker != null ? marker.equals(event.marker) : event.marker == null)
				&& (mdc != null ? mdc.equals(event.mdc) : event.mdc == null)
				&& Arrays.equals(ndc, event.ndc)
				&& (throwable != null ? throwable.equals(event.throwable) : event.throwable == null);
	}

	@Override
	public int hashCode()
	{
		int result;
		result = (logger != null ? logger.hashCode() : 0);
		result = 31 * result + (sequenceNumber != null ? sequenceNumber.hashCode() : 0);
		result = 31 * result + (level != null ? level.hashCode() : 0);
		result = 31 * result + (message != null ? message.hashCode() : 0);
		result = 31 * result + (timeStamp != null ? timeStamp.hashCode() : 0);
		result = 31 * result + (threadInfo != null ? threadInfo.hashCode() : 0);
		return result;
	}

	@Override
	public String toString()
	{
		return "LoggingEvent{" +
				"logger='" + logger + '\'' +
				", level=" + level +
				", message=" + message +
				", throwable=" + throwable +
				", callStack=" + Arrays.toString(callStack) +
				", mdc=" + mdc +
				", ndc=" + Arrays.toString(ndc) +
				", marker=" + marker +
				", threadInfo=" + threadInfo +
				", loggerContext=" + loggerContext +
				", sequenceNumber=" + sequenceNumber +
				", timeStamp=" + timeStamp +
				'}';
	}
}
