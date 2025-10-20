/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2018 Joern Huxhorn
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
 * Copyright 2007-2018 Joern Huxhorn
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

package de.huxhorn.lilith.data.logging.xml;

import de.huxhorn.lilith.data.eventsource.LoggerContext;
import de.huxhorn.lilith.data.logging.ExtendedStackTraceElement;
import de.huxhorn.lilith.data.logging.LoggingEvent;
import de.huxhorn.lilith.data.logging.Marker;
import de.huxhorn.lilith.data.logging.Message;
import de.huxhorn.lilith.data.logging.ThreadInfo;
import de.huxhorn.lilith.data.logging.ThrowableInfo;
import de.huxhorn.sulky.stax.DateTimeFormatter;
import de.huxhorn.sulky.stax.GenericStreamWriter;
import de.huxhorn.sulky.stax.StaxUtilities;
import de.huxhorn.sulky.stax.WhiteSpaceHandling;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Serialises {@link LoggingEvent} instances to the Lilith XML schema.
 */
public class LoggingEventWriter
	implements GenericStreamWriter<LoggingEvent>, LoggingEventSchemaConstants
{
	private String preferredPrefix;
	private boolean sortingMaps;
	private final DateTimeFormatter dateTimeFormatter;
	private boolean writingSchemaLocation;
	private TimeStampType timeStampType;

	/**
	 * Controls how timestamps are rendered in the XML output.
	 */
	public enum TimeStampType
	{
		/** Write only the ISO8601 timestamp attribute. */
		ONLY_TIMESTAMP,
		/** Write only the millisecond timestamp attribute. */
		ONLY_MILLIS,
		/** Write both the ISO8601 and millisecond timestamp attributes. */
		BOTH
	}

	/**
	 * Creates a writer with default configuration.
	 */
	public LoggingEventWriter()
	{
		dateTimeFormatter = new DateTimeFormatter();
		timeStampType = TimeStampType.BOTH;
	}

	/**
	 * Returns how timestamps are rendered in the XML output.
	 *
	 * @return configured timestamp type
	 */
	public TimeStampType getTimeStampType()
	{
		return timeStampType;
	}

	/**
	 * Sets how timestamps should be rendered in the XML output.
	 *
	 * @param timeStampType timestamp strategy to use
	 */
	public void setTimeStampType(TimeStampType timeStampType)
	{
		this.timeStampType = timeStampType;
	}

	/**
	 * Returns whether maps are sorted before writing them.
	 *
	 * @return {@code true} if maps are sorted lexicographically
	 */
	public boolean isSortingMaps()
	{
		return sortingMaps;
	}

	/**
	 * Enables or disables sorting of map entries prior to serialisation.
	 *
	 * @param sortingMaps {@code true} to sort map entries
	 */
	public void setSortingMaps(boolean sortingMaps)
	{
		this.sortingMaps = sortingMaps;
	}

	/**
	 * Returns whether the schema location attribute is emitted on the root element.
	 *
	 * @return {@code true} if the schema location attribute is written
	 */
	public boolean isWritingSchemaLocation()
	{
		return writingSchemaLocation;
	}

	/**
	 * Enables or disables writing of the schema location attribute.
	 *
	 * @param writingSchemaLocation {@code true} to include the schema location attribute
	 */
	public void setWritingSchemaLocation(boolean writingSchemaLocation)
	{
		this.writingSchemaLocation = writingSchemaLocation;
	}

	/**
	 * Returns the preferred namespace prefix.
	 *
	 * @return preferred namespace prefix or {@code null}
	 */
	public String getPreferredPrefix()
	{
		return preferredPrefix;
	}

	/**
	 * Sets the preferred namespace prefix.
	 *
	 * @param prefix namespace prefix to request when writing elements
	 */
	public void setPreferredPrefix(String prefix)
	{
		this.preferredPrefix = prefix;
	}

	/**
	 * Writes the supplied logging event to the provided stream writer.
	 *
	 * @param writer stream writer to output to
	 * @param event  logging event to serialise
	 * @param isRoot whether the event is the document root element
	 * @throws XMLStreamException if writing fails
	 */
	@Override
	public void write(XMLStreamWriter writer, LoggingEvent event, boolean isRoot)
		throws XMLStreamException
	{
		if(isRoot)
		{
			writer.writeStartDocument(StandardCharsets.UTF_8.toString(), "1.0");
		}

		StaxUtilities.NamespaceInfo ni = StaxUtilities
			.setNamespace(writer, preferredPrefix, NAMESPACE_URI, DEFAULT_NAMESPACE_PREFIX);
		String prefix = ni.getPrefix();
		StaxUtilities.writeStartElement(writer, prefix, NAMESPACE_URI, LOGGING_EVENT_NODE);
		if(ni.isCreated())
		{
			StaxUtilities.writeNamespace(writer, prefix, NAMESPACE_URI);
		}

		if(isRoot && writingSchemaLocation)
		{
			ni = StaxUtilities
				.setNamespace(writer, StaxUtilities.XML_SCHEMA_INSTANCE_PREFIX, StaxUtilities.XML_SCHEMA_INSTANCE_NAMESPACE_URI, StaxUtilities.XML_SCHEMA_INSTANCE_PREFIX);
			if(ni.isCreated())
			{
				StaxUtilities.writeNamespace(writer, ni.getPrefix(), StaxUtilities.XML_SCHEMA_INSTANCE_NAMESPACE_URI);
			}
			StaxUtilities.writeAttribute(writer,
				true,
				ni.getPrefix(),
				StaxUtilities.XML_SCHEMA_INSTANCE_NAMESPACE_URI,
				StaxUtilities.XML_SCHEMA_INSTANCE_SCHEMA_LOCATION_ATTRIBUTE,
				NAMESPACE_URI + " " + NAMESPACE_LOCATION);
		}
		StaxUtilities.writeAttribute(writer, false, prefix, NAMESPACE_URI, LOGGER_ATTRIBUTE, event.getLogger());
		StaxUtilities.writeAttribute(writer, false, prefix, NAMESPACE_URI, LEVEL_ATTRIBUTE, String.valueOf(event.getLevel()));
		Long sequence = event.getSequenceNumber();
		if(sequence != null)
		{
			StaxUtilities.writeAttribute(writer, false, prefix, NAMESPACE_URI, SEQUENCE_ATTRIBUTE, sequence.toString());
		}
		ThreadInfo threadInfo = event.getThreadInfo();
		if(threadInfo != null)
		{
			Long id = threadInfo.getId();
			String name = threadInfo.getName();
			Long groupId = threadInfo.getGroupId();
			String groupName = threadInfo.getGroupName();
			Integer priority = threadInfo.getPriority();
			if(name != null)
			{
				StaxUtilities
					.writeAttributeIfNotNull(writer, false, prefix, NAMESPACE_URI, THREAD_NAME_ATTRIBUTE, name);
			}
			if(id != null)
			{
				StaxUtilities
					.writeAttributeIfNotNull(writer, false, prefix, NAMESPACE_URI, THREAD_ID_ATTRIBUTE, Long.toString(id));
			}
			if(groupName != null)
			{
				StaxUtilities
					.writeAttributeIfNotNull(writer, false, prefix, NAMESPACE_URI, THREAD_GROUP_NAME_ATTRIBUTE, groupName);
			}
			if(groupId != null)
			{
				StaxUtilities
					.writeAttributeIfNotNull(writer, false, prefix, NAMESPACE_URI, THREAD_GROUP_ID_ATTRIBUTE, Long.toString(groupId));
			}
			if(priority != null)
			{
				StaxUtilities
						.writeAttributeIfNotNull(writer, false, prefix, NAMESPACE_URI, THREAD_PRIORITY_ATTRIBUTE, Integer.toString(priority));
			}
		}

		Long timeStamp = event.getTimeStamp();
		if(timeStamp != null)
		{
			if(timeStampType == TimeStampType.ONLY_TIMESTAMP || timeStampType == TimeStampType.BOTH)
			{
				StaxUtilities
					.writeAttribute(writer, false, prefix, NAMESPACE_URI, TIMESTAMP_ATTRIBUTE, dateTimeFormatter.format(new Date(timeStamp)));
			}
			if(timeStampType == TimeStampType.ONLY_MILLIS || timeStampType == TimeStampType.BOTH)
			{
				StaxUtilities
					.writeAttribute(writer, false, prefix, NAMESPACE_URI, TIMESTAMP_MILLIS_ATTRIBUTE, Long.toString(timeStamp));
			}
		}
		Message message = event.getMessage();
		if(message != null)
		{
			String messagePattern = message.getMessagePattern();
			if(messagePattern != null)
			{
				StaxUtilities.writeSimpleTextNode(writer, prefix, NAMESPACE_URI, MESSAGE_NODE, messagePattern);
			}
			writeArguments(writer, prefix, message.getArguments());
		}
		writeThrowable(writer, prefix, event);
		writeStringMap(writer, prefix, event.getMdc(), MDC_NODE);
		writeNdc(writer, prefix, event);
		writeMarker(writer, prefix, event);
		writeCallStack(writer, prefix, event);
		writeLoggerContext(writer, prefix, event);
		writer.writeEndElement();
		if(isRoot)
		{
			writer.writeEndDocument();
		}
	}

	private void writeLoggerContext(XMLStreamWriter writer, String prefix, LoggingEvent event)
		throws XMLStreamException
	{
		LoggerContext context = event.getLoggerContext();
		if(context == null)
		{
			return;
		}
		StaxUtilities.writeStartElement(writer, prefix, NAMESPACE_URI, LOGGER_CONTEXT_NODE);
		String name = context.getName();
		if(name != null)
		{
			StaxUtilities.writeAttribute(writer, false, prefix, NAMESPACE_URI, LOGGER_CONTEXT_NAME_ATTRIBUTE, name);
		}
		Long timeStamp = context.getBirthTime();
		if(timeStamp != null)
		{
			if(timeStampType == TimeStampType.ONLY_TIMESTAMP || timeStampType == TimeStampType.BOTH)
			{
				StaxUtilities
					.writeAttribute(writer, false, prefix, NAMESPACE_URI, LOGGER_CONTEXT_BIRTH_TIME_ATTRIBUTE, dateTimeFormatter.format(new Date(timeStamp)));
			}
			if(timeStampType == TimeStampType.ONLY_MILLIS || timeStampType == TimeStampType.BOTH)
			{
				StaxUtilities
					.writeAttribute(writer, false, prefix, NAMESPACE_URI, LOGGER_CONTEXT_BIRTH_TIME_MILLIS_ATTRIBUTE, Long.toString(timeStamp));
			}
		}
		writeStringMap(writer, prefix, context.getProperties(), LOGGER_CONTEXT_PROPERTIES_NODE);
		writer.writeEndElement();
	}

	private void writeCallStack(XMLStreamWriter writer, String prefix, LoggingEvent event)
		throws XMLStreamException
	{
		writeStackTraceNode(writer, prefix, event.getCallStack(), CALLSTACK_NODE);
	}

	private void writeMarker(XMLStreamWriter writer, String prefix, LoggingEvent event)
		throws XMLStreamException
	{
		Marker marker = event.getMarker();
		if(marker != null)
		{
			List<String> handledMarkers = new ArrayList<>();
			writeMarkerNode(writer, prefix, marker, handledMarkers);
		}
	}

	private void writeMarkerNode(XMLStreamWriter writer, String prefix, Marker marker, List<String> handledMarkers)
		throws XMLStreamException
	{
		String markerName = marker.getName();
		if(handledMarkers.contains(markerName))
		{
			StaxUtilities.writeEmptyElement(writer, prefix, NAMESPACE_URI, MARKER_REFERENCE_NODE);
			StaxUtilities.writeAttribute(writer, false, prefix, NAMESPACE_URI, MARKER_REFERENCE_ATTRIBUTE, markerName);
		}
		else
		{
			handledMarkers.add(markerName);
			boolean hasChildren = marker.hasReferences();
			if(hasChildren)
			{
				StaxUtilities.writeStartElement(writer, prefix, NAMESPACE_URI, MARKER_NODE);
			}
			else
			{
				StaxUtilities.writeEmptyElement(writer, prefix, NAMESPACE_URI, MARKER_NODE);
			}
			StaxUtilities.writeAttribute(writer, false, prefix, NAMESPACE_URI, MARKER_NAME_ATTRIBUTE, markerName);
			if(hasChildren)
			{
				Map<String, Marker> children = marker.getReferences();
				for(Map.Entry<String, Marker> current : children.entrySet())
				{
					writeMarkerNode(writer, prefix, current.getValue(), handledMarkers);
				}
				writer.writeEndElement();
			}
		}

	}

	private void writeStringMap(XMLStreamWriter writer, String prefix, Map<String, String> map, String nodeName)
		throws XMLStreamException
	{
		if(map != null)
		{
			if(sortingMaps)
			{
				map = new TreeMap<>(map);
			}

			StaxUtilities.writeStartElement(writer, prefix, NAMESPACE_URI, nodeName);
			for(Map.Entry<String, String> entry : map.entrySet())
			{

				StaxUtilities.writeStartElement(writer, prefix, NAMESPACE_URI, STRING_MAP_ENTRY_NODE);
				StaxUtilities
					.writeAttribute(writer, false, prefix, NAMESPACE_URI, STRING_MAP_ENTRY_KEY_ATTRIBUTE, entry.getKey(), WhiteSpaceHandling.PRESERVE_NORMALIZE_NEWLINE);
				StaxUtilities.writeText(writer, entry.getValue());
				writer.writeEndElement();
			}
			writer.writeEndElement();
		}
	}

	private void writeNdc(XMLStreamWriter writer, String prefix, LoggingEvent event)
		throws XMLStreamException
	{
		Message[] ndc = event.getNdc();
		if(ndc != null)
		{
			StaxUtilities.writeStartElement(writer, prefix, NAMESPACE_URI, NDC_NODE);
			for(Message entry : ndc)
			{

				StaxUtilities.writeStartElement(writer, prefix, NAMESPACE_URI, NDC_ENTRY_NODE);
				StaxUtilities
					.writeSimpleTextNode(writer, prefix, NAMESPACE_URI, MESSAGE_NODE, entry.getMessagePattern());
				writeArguments(writer, prefix, entry.getArguments());
				writer.writeEndElement();
			}
			writer.writeEndElement();
		}
	}

	private void writeArguments(XMLStreamWriter writer, String prefix, String[] arguments)
		throws XMLStreamException
	{
		if(arguments != null)
		{
			StaxUtilities.writeStartElement(writer, prefix, NAMESPACE_URI, ARGUMENTS_NODE);
			for(String current : arguments)
			{
				if(current == null)
				{
					StaxUtilities.writeEmptyElement(writer, prefix, NAMESPACE_URI, NULL_ARGUMENT_NODE);
				}
				else
				{
					StaxUtilities.writeSimpleTextNode(writer, prefix, NAMESPACE_URI, ARGUMENT_NODE, current);
				}
			}
			writer.writeEndElement();
		}
	}

	private void writeThrowable(XMLStreamWriter writer, String prefix, LoggingEvent event)
		throws XMLStreamException
	{
		ThrowableInfo throwable = event.getThrowable();
		writeThrowableNode(writer, prefix, throwable, THROWABLE_NODE);
	}

	private void writeThrowableNode(XMLStreamWriter writer, String prefix, ThrowableInfo throwable, String nodeName)
		throws XMLStreamException
	{
		if(throwable != null)
		{
			StaxUtilities.writeStartElement(writer, prefix, NAMESPACE_URI, nodeName);
			StaxUtilities
				.writeAttribute(writer, false, prefix, NAMESPACE_URI, THROWABLE_CLASS_NAME_ATTRIBUTE, throwable.getName());
			int omitted = throwable.getOmittedElements();
			if(omitted != 0)
			{
				StaxUtilities
					.writeAttribute(writer, false, prefix, NAMESPACE_URI, OMITTED_ELEMENTS_ATTRIBUTE, Integer.toString(throwable.getOmittedElements()));
			}
			// TODO: can message be null?
			StaxUtilities
				.writeSimpleTextNode(writer, prefix, NAMESPACE_URI, THROWABLE_MESSAGE_NODE, throwable.getMessage());
			writeStackTraceNode(writer, prefix, throwable.getStackTrace(), STACK_TRACE_NODE);

			ThrowableInfo[] suppressed = throwable.getSuppressed();
			if(suppressed != null)
			{
				StaxUtilities.writeStartElement(writer, prefix, NAMESPACE_URI, SUPPRESSED_NODE);
				for(ThrowableInfo current : suppressed)
				{
					writeThrowableNode(writer, prefix, current, THROWABLE_NODE);
				}
				writer.writeEndElement();
			}

			writeThrowableNode(writer, prefix, throwable.getCause(), CAUSE_NODE);
			writer.writeEndElement();
		}
	}

	private void writeStackTraceNode(XMLStreamWriter writer, String prefix, ExtendedStackTraceElement[] ste, String nodeName)
		throws XMLStreamException
	{
		if(ste != null)
		{
			StaxUtilities.writeStartElement(writer, prefix, NAMESPACE_URI, nodeName);
			StackTraceElementWriter steWriter = new StackTraceElementWriter();
			steWriter.setPreferredPrefix(prefix);
			for(ExtendedStackTraceElement elem : ste)
			{
				steWriter.write(writer, elem, false);
			}
			writer.writeEndElement();
		}
	}
}
