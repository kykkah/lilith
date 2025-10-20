/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2020 Joern Huxhorn
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
 * Copyright 2007-2020 Joern Huxhorn
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

import java.io.Serializable;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Objects;

/**
 * Serializable representation of a {@link Throwable}, including stack trace, cause chain, and suppressed exceptions.
 */
@SuppressWarnings({"PMD.MethodReturnsInternalArray", "PMD.ArrayIsStoredDirectly"})
public class ThrowableInfo
	implements Serializable, Cloneable
{
	private static final long serialVersionUID = -6320441996003349426L;

	/** Prefix used when rendering nested exception causes. */
	public static final String CAUSED_BY_PREFIX = "Caused by: ";
	/** Prefix used when rendering suppressed exceptions. */
	public static final String SUPPRESSED_PREFIX = "Suppressed: ";
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	/** Shared empty array instance to avoid repeated allocations. */
	public static final ThrowableInfo[] ARRAY_PROTOTYPE = new ThrowableInfo[0];

	private String name;
	private String message;
	private ExtendedStackTraceElement[] stackTrace;
	private int omittedElements;
	private ThrowableInfo[] suppressed;
	private ThrowableInfo cause;

	/**
	 * Returns the fully qualified class name of the throwable.
	 *
	 * @return throwable class name or {@code null} if unknown
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the fully qualified class name of the throwable.
	 *
	 * @param name throwable class name to store
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Returns the throwable message.
	 *
	 * @return throwable message or {@code null} if none was provided
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * Sets the throwable message.
	 *
	 * @param message message text to store
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * Returns the captured stack trace.
	 *
	 * @return stack trace elements or {@code null} if none were recorded
	 */
	public ExtendedStackTraceElement[] getStackTrace()
	{
		return stackTrace;
	}

	/**
	 * Sets the captured stack trace.
	 *
	 * @param stackTrace stack trace elements to store
	 */
	public void setStackTrace(ExtendedStackTraceElement[] stackTrace)
	{
		this.stackTrace = stackTrace;
	}

	/**
	 * Returns the suppressed throwable information.
	 *
	 * @return suppressed throwables or {@code null} if none exist
	 */
	public ThrowableInfo[] getSuppressed()
	{
		return suppressed;
	}

	/**
	 * Sets the suppressed throwable information.
	 *
	 * @param suppressed suppressed throwables to store
	 */
	public void setSuppressed(ThrowableInfo[] suppressed)
	{
		this.suppressed = suppressed;
	}

	/**
	 * Returns the cause of this throwable.
	 *
	 * @return cause information or {@code null} if none was captured
	 */
	public ThrowableInfo getCause()
	{
		return cause;
	}

	/**
	 * Sets the cause of this throwable.
	 *
	 * @param cause cause information to store
	 */
	public void setCause(ThrowableInfo cause)
	{
		this.cause = cause;
	}

	/**
	 * Returns the number of omitted stack trace elements.
	 *
	 * @return count of omitted stack trace elements
	 */
	public int getOmittedElements()
	{
		return omittedElements;
	}

	/**
	 * Sets the number of omitted stack trace elements.
	 *
	 * @param omittedElements count of omitted stack trace elements
	 */
	public void setOmittedElements(int omittedElements)
	{
		this.omittedElements = omittedElements;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		return recursiveEquals((ThrowableInfo) o, new IdentityHashMap<>());
	}

	@SuppressWarnings("PMD.CompareObjectsWithEquals")
	private boolean recursiveEquals(ThrowableInfo that, IdentityHashMap<ThrowableInfo, Object> dejaVu)
	{
		if(this == that) return true;
		if(that == null) return false;

		if(dejaVu.containsKey(that))
		{
			return true; // A special kind of true. Equally b0rked.
		}
		dejaVu.put(that, null);

		if(omittedElements != that.omittedElements) return false;
		if(name != null ? !name.equals(that.name) : that.name != null) return false;
		if(message != null ? !message.equals(that.message) : that.message != null) return false;
		if(!Arrays.equals(stackTrace, that.stackTrace)) return false;

		if(cause == null)
		{
			if(that.cause != null)
			{
				return false;
			}
		}
		else
		{
			if(!cause.recursiveEquals(that.cause, dejaVu))
			{
				return false;
			}
		}

		if(suppressed == null)
		{
			if(that.suppressed != null)
			{
				return false;
			}
		}
		else
		{
			if(that.suppressed == null)
			{
				return false;
			}
			if(suppressed.length != that.suppressed.length)
			{
				return false;
			}
			for(int i=0;i<suppressed.length; i++)
			{
				ThrowableInfo thisSuppressedEntry = suppressed[i];
				ThrowableInfo thatSuppressedEntry = that.suppressed[i];

				if(thisSuppressedEntry == null)
				{
					if(thatSuppressedEntry != null)
					{
						return false;
					}
				}
				else
				{
					if(!thisSuppressedEntry.recursiveEquals(thatSuppressedEntry, dejaVu))
					{
						return false;
					}
				}
			}
		}

		return true;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode()
	{
		return recursiveHashCode(this, new IdentityHashMap<>());
	}

	private static int recursiveHashCode(ThrowableInfo instance, IdentityHashMap<ThrowableInfo, Object> dejaVu)
	{
		if(instance == null)
		{
			return 0;
		}
		if(dejaVu.containsKey(instance))
		{
			return 0;
		}
		dejaVu.put(instance, null);
		int result = instance.getOmittedElements();

		String name = instance.getName();
		result = 29 * result + (name != null ? name.hashCode() : 0);

		String message = instance.getMessage();
		result = 29 * result + (message != null ? message.hashCode() : 0);

		ThrowableInfo cause = instance.getCause();
		result = 29 * result + recursiveHashCode(cause, dejaVu);

		ThrowableInfo[] suppressed = instance.getSuppressed();
		if(suppressed != null)
		{
			for (ThrowableInfo throwableInfo : suppressed)
			{
				result = 29 * result + recursiveHashCode(throwableInfo, dejaVu);
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("CloneDoesntCallSuperClone")
	@Override
	public ThrowableInfo clone() throws CloneNotSupportedException
	{
		return recursiveClone(new IdentityHashMap<>());
	}

	private ThrowableInfo recursiveClone(IdentityHashMap<ThrowableInfo, ThrowableInfo> dejaVu)
			throws CloneNotSupportedException
	{
		ThrowableInfo result = dejaVu.get(this);
		if(result != null)
		{
			// we already cloned it.
			return result;
		}

		result = (ThrowableInfo) super.clone();
		dejaVu.put(this, result);

		if(stackTrace != null)
		{
			result.stackTrace = new ExtendedStackTraceElement[stackTrace.length];
			for(int i=0; i<stackTrace.length; i++)
			{
				ExtendedStackTraceElement current = stackTrace[i];
				if(current != null)
				{
					result.stackTrace[i] = current.clone();
				}
			}
		}

		if(cause != null)
		{
			result.cause = cause.recursiveClone(dejaVu);
		}

		if(suppressed != null)
		{
			result.suppressed = new ThrowableInfo[suppressed.length];
			for(int i=0; i<suppressed.length; i++)
			{
				ThrowableInfo current = suppressed[i];
				if(current != null)
				{
					result.suppressed[i] = current.recursiveClone(dejaVu);
				}
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public String toString()
	{
		return toString(true);
	}

	/**
	 * Returns a string representation similar to printStackTrace.
	 *
	 * @param extended whether or not extended StackTraceElement information should be appended.
	 * @return the String representation of this ThrowableInfo.
	 */
	public String toString(boolean extended)
	{
		return appendTo(new StringBuilder(), extended).toString();
	}

	/**
	 * Appends this instance to the given StringBuilder.
	 *
	 * @param stringBuilder the StringBuilder to append this instance to.
	 * @param extended Whether or not extended info should be included, if available.
	 * @return the given StringBuilder instance.
	 * @throws NullPointerException if stringBuilder is null.
	 */
	public StringBuilder appendTo(StringBuilder stringBuilder, boolean extended)
	{
		Objects.requireNonNull(stringBuilder, "stringBuilder must not be null!");

		recursiveAppend(stringBuilder, new IdentityHashMap<>(), null, 0, this, extended);

		return stringBuilder;
	}

	private static void recursiveAppend(StringBuilder sb, IdentityHashMap<ThrowableInfo, Object> dejaVu, String prefix, int indent, ThrowableInfo throwableInfo, boolean extended)
	{
		if(throwableInfo == null)
		{
			return;
		}

		appendIndent(sb, indent);
		if(prefix != null)
		{
			sb.append(prefix);
		}

		String name = throwableInfo.getName();
		if(name != null)
		{
			sb.append(name);
			String message = throwableInfo.getMessage();
			if(message != null && !name.equals(message))
			{
				sb.append(": ").append(throwableInfo.getMessage());
			}
		}
		else
		{
			sb.append(throwableInfo.getMessage());
		}

		if(dejaVu.containsKey(throwableInfo))
		{
			sb.append("[CIRCULAR REFERENCE]\n");
			return;
		}
		dejaVu.put(throwableInfo, null);
		sb.append(LINE_SEPARATOR);

		appendSTEArray(sb, indent + 1, throwableInfo, extended);

		ThrowableInfo[] suppressed = throwableInfo.getSuppressed();
		if(suppressed != null)
		{
			for(ThrowableInfo current : suppressed)
			{
				recursiveAppend(sb, dejaVu, SUPPRESSED_PREFIX, indent + 1, current, extended);
			}
		}
		recursiveAppend(sb, dejaVu, CAUSED_BY_PREFIX, indent, throwableInfo.getCause(), extended);
	}

	private static void appendIndent(StringBuilder sb, int indent)
	{
		for(int i=0;i<indent;i++)
		{
			sb.append('\t');
		}
	}

	private static void appendSTEArray(StringBuilder sb, int indentLevel, ThrowableInfo throwableInfo, boolean extended)
	{
		ExtendedStackTraceElement[] steArray = throwableInfo.getStackTrace();

		if(steArray != null)
		{
			for(ExtendedStackTraceElement ste : steArray)
			{
				if (ste == null)
				{
					continue;
				}
				appendIndent(sb, indentLevel);
				sb.append("at ");
				ste.appendTo(sb, extended);
				sb.append(LINE_SEPARATOR);
			}
		}

		int commonFrames = throwableInfo.getOmittedElements();

		if(commonFrames > 0)
		{
			appendIndent(sb, indentLevel);
			sb.append("... ").append(commonFrames).append(" more").append(LINE_SEPARATOR);
		}
	}

}
