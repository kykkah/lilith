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

package de.huxhorn.lilith.log4j2.decoder;

import de.huxhorn.lilith.data.logging.LoggingEvent;
import de.huxhorn.sulky.codec.Decoder;

class Log4j2JsonDecoderTest extends Log4j2DecoderTestBase {

    @Override
    protected String inputString() {
        return """
                {
                  "timeMillis" : 1493121664118,
                  "thread" : "main",
                  "threadId" : 1,
                  "threadPriority" : 5,
                  "level" : "INFO",
                  "loggerName" : "HelloWorld",
                  "marker" : {
                    "name" : "child",
                    "parents" : [ {
                      "name" : "parent",
                      "parents" : [ {
                        "name" : "grandparent"
                      } ]
                    } ]
                  },
                  "message" : "Hello, world!",
                  "thrown" : {
                    "commonElementCount" : 0,
                    "message" : "error message",
                    "name" : "java.lang.RuntimeException",
                    "extendedStackTrace" : [ {
                      "class" : "logtest.Main",
                      "method" : "main",
                      "file" : "Main.java",
                      "line" : 29,
                      "exact" : true,
                      "location" : "classes/",
                      "version" : "?"
                    } ]
                  },
                  "contextStack" : [ "one", "two" ],
                  "loggerFqcn" : "org.apache.logging.log4j.spi.AbstractLogger",
                  "endOfBatch" : false,
                  "contextMap" : {
                    "bar" : "BAR",
                    "foo" : "FOO"
                  },
                  "source" : {
                    "class" : "logtest.Main",
                    "method" : "main",
                    "file" : "Main.java",
                    "line" : 29
                  }
                }
                """;
    }

    @Override
    protected Decoder<LoggingEvent> decoder() {
        return new Log4j2JsonDecoder();
    }
}
