/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2011 Joern Huxhorn
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
 * Copyright 2007-2011 Joern Huxhorn
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

package de.huxhorn.lilith.log4j.xml;

/**
 * XML element and attribute names recognised by the Lilith Log4j XML schema.
 *
 * <p>The constants mirror the structure defined in {@code LoggingEvent.xsd} and are shared between serializers
 * and deserializers.</p>
 */
@SuppressWarnings("doclint:missing")
public interface LoggingEventSchemaConstants
{
	/*
<log4j:event logger="de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass" timestamp="1233135682640" level="DEBUG" thread="main">
<log4j:message><![CDATA[Foo!]]></log4j:message>
<log4j:NDC><![CDATA[NDC1 NDC2]]></log4j:NDC>
<log4j:throwable><![CDATA[java.lang.RuntimeException: Hello
	at de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass.execute(Log4jSandbox.java:18)
	at de.huxhorn.lilith.sandbox.Log4jSandbox.main(Log4jSandbox.java:38)
Caused by: java.lang.RuntimeException: Hi.
	at de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass.foobar(Log4jSandbox.java:24)
	at de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass.execute(Log4jSandbox.java:14)
	... 1 more
]]></log4j:throwable>
<log4j:locationInfo class="de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass" method="execute" file="Log4jSandbox.java" line="18"/>
<log4j:properties>
<log4j:data name="key1" value="value1"/>
<log4j:data name="key2" value="value2"/>
</log4j:properties>
</log4j:event>

	 */
	/** Default namespace prefix 'log4j'. */
	String DEFAULT_NAMESPACE_PREFIX = "log4j";
	/** Namespace URI 'http://jakarta.apache.org/log4j/'. */
	String NAMESPACE_URI = "http://jakarta.apache.org/log4j/";


	/** XML element name 'event'. */
	String LOGGING_EVENT_NODE = "event";

	/** XML attribute name 'logger'. */
	String LOGGER_ATTRIBUTE = "logger";
	/** XML attribute name 'timestamp'. */
	String TIMESTAMP_ATTRIBUTE = "timestamp";
	/** XML attribute name 'level'. */
	String LEVEL_ATTRIBUTE = "level";
	/** XML attribute name 'thread'. */
	String THREAD_NAME_ATTRIBUTE = "thread";
	/** XML attribute name 'threadId'. */
	String THREAD_ID_ATTRIBUTE = "threadId";
	/** XML attribute name 'threadGroup'. */
	String THREAD_GROUP_NAME_ATTRIBUTE = "threadGroup";
	/** XML attribute name 'threadGroupId'. */
	String THREAD_GROUP_ID_ATTRIBUTE = "threadGroupId";

	/** XML element name 'message'. */
	String MESSAGE_NODE = "message";
	/** XML element name 'NDC'. */
	String NDC_NODE = "NDC";

	/** XML element name 'throwable'. */
	String THROWABLE_NODE = "throwable";

	/** XML element name 'locationInfo'. */
	String LOCATION_INFO_NODE = "locationInfo";

	/** XML attribute name 'class'. */
	String CLASS_ATTRIBUTE = "class";
	/** XML attribute name 'method'. */
	String METHOD_ATTRIBUTE = "method";
	/** XML attribute name 'file'. */
	String FILE_ATTRIBUTE = "file";
	/** XML attribute name 'line'. */
	String LINE_ATTRIBUTE = "line";

	/** XML element name 'properties'. */
	String PROPERTIES_NODE = "properties";
	/** XML element name 'data'. */
	String DATA_NODE = "data";
	/** XML attribute name 'name'. */
	String NAME_ATTRIBUTE = "name";
	/** XML attribute name 'value'. */
	String VALUE_ATTRIBUTE = "value";
}
