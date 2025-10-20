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

package de.huxhorn.lilith.data.logging.xml;

/**
 * XML element and attribute names used by the Lilith logging event schema.
 *
 * <p>The constants mirror the structure defined in {@code LoggingEvent.xsd} so that producers and consumers
 * can share a single source of truth.</p>
 */
@SuppressWarnings("doclint:missing")
public interface LoggingEventSchemaConstants
{
	/** Namespace URI 'http://lilith.sf.net/schema/logging/16'. */
	String NAMESPACE_URI = "http://lilith.sf.net/schema/logging/16";
	/** Schema location URL 'http://lilith.sf.net/schema/logging/16/LoggingEvent.xsd'. */
	String NAMESPACE_LOCATION = "http://lilith.sf.net/schema/logging/16/LoggingEvent.xsd";

	/** Default namespace prefix 'log'. */
	String DEFAULT_NAMESPACE_PREFIX = "log";

	/** XML element name 'LoggingEvents'. */
	String LOGGING_EVENTS_NODE = "LoggingEvents";
	/** XML attribute name 'startIndex'. */
	String START_INDEX_ATTRIBUTE = "startIndex";
	//String APPLICATION_IDENTIFIER_ATTRIBUTE = "applicationId";

	/** XML element name 'LoggingEvent'. */
	String LOGGING_EVENT_NODE = "LoggingEvent";
	/** XML element name 'Message'. */
	String MESSAGE_NODE = "Message";
	/** XML element name 'Throwable'. */
	String THROWABLE_NODE = "Throwable";
	/** XML element name 'CallStack'. */
	String CALLSTACK_NODE = "CallStack";
	/** XML attribute name 'logger'. */
	String LOGGER_ATTRIBUTE = "logger";
	/** XML attribute name 'level'. */
	String LEVEL_ATTRIBUTE = "level";
	/** XML attribute name 'sequence'. */
	String SEQUENCE_ATTRIBUTE = "sequence";
	/** XML attribute name 'threadName'. */
	String THREAD_NAME_ATTRIBUTE = "threadName";
	/** XML attribute name 'threadId'. */
	String THREAD_ID_ATTRIBUTE = "threadId";
	/** XML attribute name 'threadGroup'. */
	String THREAD_GROUP_NAME_ATTRIBUTE = "threadGroup";
	/** XML attribute name 'threadGroupId'. */
	String THREAD_GROUP_ID_ATTRIBUTE = "threadGroupId";
	/** XML attribute name 'threadPriority'. */
	String THREAD_PRIORITY_ATTRIBUTE = "threadPriority";
	/** XML attribute name 'timeStamp'. */
	String TIMESTAMP_ATTRIBUTE = "timeStamp";
	/** XML attribute name 'timeStampMillis'. */
	String TIMESTAMP_MILLIS_ATTRIBUTE = "timeStampMillis";

	/** XML element name 'Arguments'. */
	String ARGUMENTS_NODE = "Arguments";
	/** XML element name 'Argument'. */
	String ARGUMENT_NODE = "Argument";
	/** XML element name 'null'. */
	String NULL_ARGUMENT_NODE = "null";

	/** XML element name 'Message'. */
	String THROWABLE_MESSAGE_NODE = "Message";
	/** XML element name 'Suppressed'. */
	String SUPPRESSED_NODE = "Suppressed";
	/** XML element name 'Cause'. */
	String CAUSE_NODE = "Cause";
	/** XML attribute name 'name'. */
	String THROWABLE_CLASS_NAME_ATTRIBUTE = "name";

	/** XML element name 'MDC'. */
	String MDC_NODE = "MDC";
	/** XML element name 'Entry'. */
	String STRING_MAP_ENTRY_NODE = "Entry";
	/** XML attribute name 'key'. */
	String STRING_MAP_ENTRY_KEY_ATTRIBUTE = "key";

	/** XML element name 'NDC'. */
	String NDC_NODE = "NDC";
	/** XML element name 'Entry'. */
	String NDC_ENTRY_NODE = "Entry";

	/** XML element name 'Marker'. */
	String MARKER_NODE = "Marker";
	/** XML attribute name 'name'. */
	String MARKER_NAME_ATTRIBUTE = "name";
	/** XML element name 'MarkerReference'. */
	String MARKER_REFERENCE_NODE = "MarkerReference";
	/** XML attribute name 'ref'. */
	String MARKER_REFERENCE_ATTRIBUTE = "ref";

	/** XML element name 'StackTrace'. */
	String STACK_TRACE_NODE = "StackTrace";
	/** XML element name 'StackTraceElement'. */
	String STACK_TRACE_ELEMENT_NODE = "StackTraceElement";
	/** XML attribute name 'classLoaderName'. */
	String ST_CLASS_LOADER_NAME_ATTRIBUTE = "classLoaderName";
	/** XML attribute name 'moduleName'. */
	String ST_MODULE_NAME_ATTRIBUTE = "moduleName";
	/** XML attribute name 'moduleVersion'. */
	String ST_MODULE_VERSION_ATTRIBUTE = "moduleVersion";
	/** XML attribute name 'className'. */
	String ST_CLASS_NAME_ATTRIBUTE = "className";
	/** XML attribute name 'methodName'. */
	String ST_METHOD_NAME_ATTRIBUTE = "methodName";
	/** XML attribute name 'fileName'. */
	String ST_FILE_NAME_ATTRIBUTE = "fileName";
	/** XML element name 'LineNumber'. */
	String ST_LINE_NUMBER_NODE = "LineNumber";
	/** XML element name 'Native'. */
	String ST_NATIVE_NODE = "Native";
	/** XML element name 'CodeLocation'. */
	String ST_CODE_LOCATION_NODE = "CodeLocation";
	/** XML element name 'Version'. */
	String ST_VERSION_NODE = "Version";
	/** XML element name 'Exact'. */
	String ST_EXACT_NODE = "Exact";

	/** XML attribute name 'omittedElements'. */
	String OMITTED_ELEMENTS_ATTRIBUTE = "omittedElements";

	/** XML element name 'LoggerContext'. */
	String LOGGER_CONTEXT_NODE = "LoggerContext";
	/** XML attribute name 'name'. */
	String LOGGER_CONTEXT_NAME_ATTRIBUTE = "name";
	/** XML attribute name 'birthTimeMillis'. */
	String LOGGER_CONTEXT_BIRTH_TIME_MILLIS_ATTRIBUTE = "birthTimeMillis";
	/** XML attribute name 'birthTime'. */
	String LOGGER_CONTEXT_BIRTH_TIME_ATTRIBUTE = "birthTime";
	/** XML element name 'Properties'. */
	String LOGGER_CONTEXT_PROPERTIES_NODE = "Properties";
}
