/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2016 Joern Huxhorn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.huxhorn.lilith.data;

import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.eventsource.EventWrapper;
import de.huxhorn.lilith.data.eventsource.LoggerContext;
import de.huxhorn.lilith.data.eventsource.SourceIdentifier;
import de.huxhorn.lilith.data.logging.ExtendedStackTraceElement;
import de.huxhorn.lilith.data.logging.LoggingEvent;
import de.huxhorn.lilith.data.logging.Marker;
import de.huxhorn.lilith.data.logging.Message;
import de.huxhorn.lilith.data.logging.ThreadInfo;
import de.huxhorn.lilith.data.logging.ThrowableInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventWrapperCorpus
{
    private static final Logger logger = LoggerFactory.getLogger(EventWrapperCorpus.class);

    private static final Set<Integer> MATCH_ALL_SET = Collections.unmodifiableSet(matchAllSet(createCorpus()));
    private static final Set<Integer> MATCH_ANY_LOGGING_SET = Collections.unmodifiableSet(matchAnyLoggingEventSet(createCorpus()));
    private static final Set<Integer> MATCH_ANY_ACCESS_SET = Collections.unmodifiableSet(matchAnyAccessEventSet(createCorpus()));
    private static final Set<Integer> MATCH_ANY_LOGGING_OR_ACCESS_SET = Collections.unmodifiableSet(matchAnyLoggingOrAccessEventSet(createCorpus()));
    private static final Set<Integer> MATCH_ANY_WRAPPER_SET = Collections.unmodifiableSet(matchAnyEventWrapperSet(createCorpus()));

    private static final Marker FOO_MARKER = new Marker("Foo-Marker");
    private static final Marker BAR_MARKER = new Marker("Bar-Marker");
    private static final Marker RECURSIVE_MARKER = new Marker("Recursive-Marker");

    static
    {
        FOO_MARKER.add(BAR_MARKER);
        RECURSIVE_MARKER.add(RECURSIVE_MARKER);
        if(logger.isInfoEnabled())
        {
            StringBuilder builder = new StringBuilder();
            List<Object> corpus = createCorpus();
            for(int i = 0; i < corpus.size(); i++)
            {
                if(builder.length() > 0)
                {
                    builder.append('\n');
                }
                builder.append('#').append(i).append('\n');
                builder.append('\t').append(corpus.get(i));
            }
            logger.info("EventWrapperCorpus:\n{}", builder);
        }
    }

    public static List<Object> createCorpus()
    {
        List<Object> result = new ArrayList<>();

        // #0
        result.add(null);
        result.add(new Foo());
        result.add(new EventWrapper<>());
        result.add(new EventWrapper<>(new SourceIdentifier("identifier", "secondaryIdentifier"), 17L, new Foo()));
        result.add(wrap(new Foo()));
        result.add(accessWrapper());
        result.add(loggingWrapper());
        result.add(loggingWrapper());

        // level
        result.add(loggingWrapper(event -> event.setLevel(LoggingEvent.Level.TRACE)));
        result.add(loggingWrapper(event -> event.setLevel(LoggingEvent.Level.DEBUG)));

        // #10
        result.add(loggingWrapper(event -> event.setLevel(LoggingEvent.Level.INFO)));
        result.add(loggingWrapper(event -> event.setLevel(LoggingEvent.Level.WARN)));
        result.add(loggingWrapper(event -> event.setLevel(LoggingEvent.Level.ERROR)));

        // logger
        result.add(loggingWrapper(event -> event.setLogger("com.foo.Foo")));
        result.add(loggingWrapper(event -> event.setLogger("com.foo.Bar")));

        // message
        result.add(loggingWrapper(event -> event.setMessage(new Message())));
        result.add(loggingWrapper(event -> event.setMessage(new Message(null))));
        result.add(loggingWrapper(event -> event.setMessage(new Message("a message."))));
        result.add(loggingWrapper(event -> event.setMessage(new Message("another message."))));
        result.add(loggingWrapper(event -> event.setMessage(new Message("a message with parameter {}.", new String[]{"paramValue"}))));

        // #20
        result.add(loggingWrapper(event -> event.setMessage(new Message("a message with unresolved parameter {}."))));
        result.add(loggingWrapper(event -> event.setMessage(new Message("a message with parameter {} and unresolved parameter {}.", new String[]{"paramValue"}))));
        result.add(loggingWrapper(event -> event.setMessage(new Message("{}", new String[]{"paramValue"}))));
        result.add(loggingWrapper(event -> event.setMessage(new Message("{}"))));

        // mdc
        result.add(loggingWrapper(event -> {
            Map<String, String> mdc = new LinkedHashMap<>();
            mdc.put("mdcKey", "mdcValue");
            event.setMdc(mdc);
        }));

        // throwable
        result.add(loggingWrapper(event -> event.setThrowable(configure(new ThrowableInfo(), throwable -> throwable.setName("java.lang.RuntimeException")))));
        result.add(loggingWrapper(event -> event.setThrowable(configure(new ThrowableInfo(), throwable -> {
            throwable.setName("java.lang.RuntimeException");
            throwable.setCause(configure(new ThrowableInfo(), cause -> cause.setName("java.lang.NullPointerException")));
        }))));
        result.add(loggingWrapper(event -> event.setThrowable(configure(new ThrowableInfo(), throwable -> {
            throwable.setName("java.lang.RuntimeException");
            throwable.setCause(configure(new ThrowableInfo(), cause -> {
                cause.setName("java.lang.NullPointerException");
                cause.setCause(configure(new ThrowableInfo(), inner -> inner.setName("java.lang.FooException")));
            }));
        }))));
        result.add(loggingWrapper(event -> event.setThrowable(configure(new ThrowableInfo(), throwable -> {
            throwable.setName("java.lang.RuntimeException");
            throwable.setSuppressed(new ThrowableInfo[]{configure(new ThrowableInfo(), suppressed -> suppressed.setName("java.lang.NullPointerException"))});
        }))));
        result.add(loggingWrapper(event -> event.setThrowable(configure(new ThrowableInfo(), throwable -> {
            throwable.setName("java.lang.RuntimeException");
            throwable.setSuppressed(new ThrowableInfo[]{
                configure(new ThrowableInfo(), suppressed -> suppressed.setName("java.lang.NullPointerException")),
                configure(new ThrowableInfo(), suppressed -> suppressed.setName("java.lang.FooException"))
            });
        }))));

        // #30
        result.add(loggingWrapper(event -> event.setThrowable(configure(new ThrowableInfo(), throwable -> {
            throwable.setName("java.lang.RuntimeException");
            throwable.setCause(configure(new ThrowableInfo(), cause -> cause.setName("java.lang.BarException")));
            throwable.setSuppressed(new ThrowableInfo[]{
                configure(new ThrowableInfo(), suppressed -> suppressed.setName("java.lang.NullPointerException")),
                configure(new ThrowableInfo(), suppressed -> suppressed.setName("java.lang.FooException"))
            });
        }))));

        // marker
        result.add(loggingWrapper(event -> event.setMarker(FOO_MARKER)));
        result.add(loggingWrapper(event -> event.setMarker(BAR_MARKER)));

        // ndc
        result.add(loggingWrapper(event -> event.setNdc(new Message[0])));
        result.add(loggingWrapper(event -> event.setNdc(new Message[]{new Message()})));
        result.add(loggingWrapper(event -> event.setNdc(new Message[]{new Message(null)})));
        result.add(loggingWrapper(event -> event.setNdc(new Message[]{new Message("a message.")})));
        result.add(loggingWrapper(event -> event.setNdc(new Message[]{new Message("another message.")})));
        result.add(loggingWrapper(event -> event.setNdc(new Message[]{new Message("a message with parameter {}.", new String[]{"paramValue"})})));
        result.add(loggingWrapper(event -> event.setNdc(new Message[]{new Message("a message with unresolved parameter {}.")})));

        // #40
        result.add(loggingWrapper(event -> event.setNdc(new Message[]{new Message("a message with parameter {} and unresolved parameter {}.", new String[]{"paramValue"})})));
        result.add(loggingWrapper(event -> event.setNdc(new Message[]{new Message("{}", new String[]{"paramValue"})})));
        result.add(loggingWrapper(event -> event.setNdc(new Message[]{new Message("{}")})));

        // call stack
        result.add(loggingWrapper(event -> event.setCallStack(new ExtendedStackTraceElement[0])));
        result.add(loggingWrapper(event -> event.setCallStack(new ExtendedStackTraceElement[]{
            parse("de.huxhorn.lilith.debug.DebugDialog$LogAllAction.actionPerformed(DebugDialog.java:358) ~[de.huxhorn.lilith-8.1.0-SNAPSHOT.jar:na]"),
            parse("javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022) ~[na:1.8.0_92]"),
            parse("javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2348) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259) ~[na:1.8.0_92]")
        })));
        result.add(loggingWrapper(event -> event.setCallStack(new ExtendedStackTraceElement[]{
            parse("de.huxhorn.lilith.debug.DebugDialog$LogAllAction.actionPerformed(DebugDialog.java:358) ~[de.huxhorn.lilith-8.1.0-SNAPSHOT.jar:na]"),
            parse("javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022) ~[na:1.8.0_92]"),
            parse("javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2348) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259) ~[na:1.8.0_92]"),
            parse("javax.swing.plaf.basic.BasicButtonListener.mouseReleased(BasicButtonListener.java:252) ~[na:1.8.0_92]")
        })));
        result.add(loggingWrapper(event -> event.setCallStack(new ExtendedStackTraceElement[]{
            parse("de.huxhorn.lilith.debug.DebugDialog$LogAllAction.actionPerformed(DebugDialog.java:358)"),
            parse("javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022)"),
            parse("javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2348)"),
            parse("javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402)"),
            parse("javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259)")
        })));
        result.add(loggingWrapper(event -> event.setCallStack(new ExtendedStackTraceElement[]{
            parse("de.huxhorn.lilith.debug.DebugDialog$LogAllAction.actionPerformed(DebugDialog.java:358)"),
            parse("javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022)"),
            parse("javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2348)"),
            parse("javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402)"),
            parse("javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259)"),
            parse("javax.swing.plaf.basic.BasicButtonListener.mouseReleased(BasicButtonListener.java:252)")
        })));
        result.add(loggingWrapper(event -> event.setCallStack(new ExtendedStackTraceElement[]{
            parse("javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022) ~[na:1.8.0_92]"),
            parse("javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2348) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259) ~[na:1.8.0_92]")
        })));
        result.add(loggingWrapper(event -> event.setCallStack(new ExtendedStackTraceElement[]{
            parse("javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022) ~[na:1.8.0_92]"),
            parse("javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2348) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259) ~[na:1.8.0_92]"),
            parse("javax.swing.plaf.basic.BasicButtonListener.mouseReleased(BasicButtonListener.java:252) ~[na:1.8.0_92]")
        })));

        // #50
        result.add(loggingWrapper(event -> event.setCallStack(new ExtendedStackTraceElement[]{
            parse("javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022)"),
            parse("javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2348)"),
            parse("javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402)"),
            parse("javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259)")
        })));
        result.add(loggingWrapper(event -> event.setCallStack(new ExtendedStackTraceElement[]{
            parse("javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022)"),
            parse("javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2348)"),
            parse("javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402)"),
            parse("javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259)"),
            parse("javax.swing.plaf.basic.BasicButtonListener.mouseReleased(BasicButtonListener.java:252)")
        })));

        // status code
        result.add(accessWrapper(event -> event.setStatusCode(100)));
        result.add(accessWrapper(event -> event.setStatusCode(200)));
        result.add(accessWrapper(event -> event.setStatusCode(202)));
        result.add(accessWrapper(event -> event.setStatusCode(301)));
        result.add(accessWrapper(event -> event.setStatusCode(404)));
        result.add(accessWrapper(event -> event.setStatusCode(451)));
        result.add(accessWrapper(event -> event.setStatusCode(500)));

        // remote user
        result.add(accessWrapper(event -> event.setRemoteUser("")));

        // #60
        result.add(accessWrapper(event -> event.setRemoteUser("-")));
        result.add(accessWrapper(event -> event.setRemoteUser(" ")));
        result.add(accessWrapper(event -> event.setRemoteUser(" - ")));
        result.add(accessWrapper(event -> event.setRemoteUser("sfalken")));
        result.add(accessWrapper(event -> event.setRemoteUser(" sfalken ")));

        // broken call stack
        result.add(loggingWrapper(event -> event.setCallStack(new ExtendedStackTraceElement[]{
            null,
            parse("javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022) ~[na:1.8.0_92]"),
            parse("javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2348) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259) ~[na:1.8.0_92]")
        })));
        result.add(loggingWrapper(event -> event.setCallStack(new ExtendedStackTraceElement[]{
            parse("de.huxhorn.lilith.debug.DebugDialog$LogAllAction.actionPerformed(DebugDialog.java:358) ~[de.huxhorn.lilith-8.1.0-SNAPSHOT.jar:na]"),
            null,
            parse("javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2348) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259) ~[na:1.8.0_92]")
        })));

        // more mdc
        result.add(loggingWrapper(event -> event.setMdc(new LinkedHashMap<>())));
        result.add(loggingWrapper(event -> {
            Map<String, String> mdc = new LinkedHashMap<>();
            mdc.put("mdcKey", "otherMdcValue");
            event.setMdc(mdc);
        }));
        result.add(loggingWrapper(event -> {
            Map<String, String> mdc = new LinkedHashMap<>();
            mdc.put("mdcKey", null);
            event.setMdc(mdc);
        }));

        // #70
        // http method
        result.add(accessWrapper(event -> event.setMethod("GET")));
        result.add(accessWrapper(event -> event.setMethod("PUT")));

        // request URI
        result.add(accessWrapper(event -> event.setRequestURI("/")));
        result.add(accessWrapper(event -> event.setRequestURI("/index.html")));

        // request URL
        result.add(accessWrapper(event -> event.setRequestURL("GET /?foo=bar&foo=schnurz HTTP/1.1")));
        result.add(accessWrapper(event -> event.setRequestURL("GET /index.html?foo=bar&foo=schnurz HTTP/1.1")));

        // logger context name
        result.add(loggingWrapper(event -> event.setLoggerContext(configure(new LoggerContext(), context -> context.setName("loggerContextName")))));
        result.add(accessWrapper(event -> event.setLoggerContext(configure(new LoggerContext(), context -> context.setName("loggerContextName")))));

        // logger context properties
        result.add(loggingWrapper(event -> event.setLoggerContext(configure(new LoggerContext(), context -> context.setProperties(new LinkedHashMap<>())))));
        result.add(accessWrapper(event -> event.setLoggerContext(configure(new LoggerContext(), context -> context.setProperties(new LinkedHashMap<>())))));

        // #80
        result.add(loggingWrapper(event -> event.setLoggerContext(configure(new LoggerContext(), context -> {
            Map<String, String> properties = new LinkedHashMap<>();
            properties.put("loggerContextKey", "loggerContextValue");
            context.setProperties(properties);
        }))));
        result.add(accessWrapper(event -> event.setLoggerContext(configure(new LoggerContext(), context -> {
            Map<String, String> properties = new LinkedHashMap<>();
            properties.put("loggerContextKey", "loggerContextValue");
            context.setProperties(properties);
        }))));

        // thread info
        result.add(loggingWrapper(event -> event.setThreadInfo(new ThreadInfo())));
        result.add(loggingWrapper(event -> event.setThreadInfo(configure(new ThreadInfo(), threadInfo -> threadInfo.setName("threadName")))));
        result.add(loggingWrapper(event -> event.setThreadInfo(configure(new ThreadInfo(), threadInfo -> threadInfo.setId(11337L)))));
        result.add(loggingWrapper(event -> event.setThreadInfo(configure(new ThreadInfo(), threadInfo -> threadInfo.setGroupName("groupName")))));
        result.add(loggingWrapper(event -> event.setThreadInfo(configure(new ThreadInfo(), threadInfo -> threadInfo.setGroupId(31337L)))));

        // broken ndc with gap
        result.add(loggingWrapper(event -> event.setNdc(new Message[]{new Message("b0rked1"), null, new Message("b0rked3")})));

        // recursive marker
        result.add(loggingWrapper(event -> event.setMarker(RECURSIVE_MARKER)));

        // throwable message
        result.add(loggingWrapper(event -> event.setThrowable(configure(new ThrowableInfo(), throwable -> throwable.setMessage("exception1")))));
        // cause
        result.add(loggingWrapper(event -> event.setThrowable(configure(new ThrowableInfo(), throwable -> throwable.setCause(configure(new ThrowableInfo(), cause -> cause.setMessage("exception2")))))));

        // #90
        // suppressed
        result.add(loggingWrapper(event -> event.setThrowable(configure(new ThrowableInfo(), throwable -> throwable.setSuppressed(new ThrowableInfo[]{configure(new ThrowableInfo(), suppressed -> suppressed.setMessage("exception3"))})))));
        // broken suppressed array
        result.add(loggingWrapper(event -> event.setThrowable(configure(new ThrowableInfo(), throwable -> throwable.setSuppressed(new ThrowableInfo[]{
            configure(new ThrowableInfo(), suppressed -> suppressed.setMessage("exception4")),
            null,
            configure(new ThrowableInfo(), suppressed -> suppressed.setMessage("exception5"))
        })))));

        // recursive throwables
        result.add(loggingWrapper(event -> {
            ThrowableInfo recursiveCause = configure(new ThrowableInfo(), throwable -> throwable.setName("recursiveCause"));
            recursiveCause.setCause(recursiveCause);
            event.setThrowable(recursiveCause);
        }));

        result.add(loggingWrapper(event -> {
            ThrowableInfo recursiveSuppressed = configure(new ThrowableInfo(), throwable -> throwable.setName("recursiveSuppressed"));
            recursiveSuppressed.setSuppressed(new ThrowableInfo[]{recursiveSuppressed});
            event.setThrowable(recursiveSuppressed);
        }));

        // broken throwable stack trace
        result.add(loggingWrapper(event -> event.setThrowable(configure(new ThrowableInfo(), throwable -> throwable.setStackTrace(new ExtendedStackTraceElement[]{
            null,
            parse("javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022) ~[na:1.8.0_92]"),
            parse("javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2348) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259) ~[na:1.8.0_92]")
        })))));

        result.add(loggingWrapper(event -> event.setThrowable(configure(new ThrowableInfo(), throwable -> throwable.setCause(configure(new ThrowableInfo(), cause -> cause.setStackTrace(new ExtendedStackTraceElement[]{
            parse("javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022) ~[na:1.8.0_92]"),
            null,
            parse("javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259) ~[na:1.8.0_92]")
        })))))));

        // empty mdc
        result.add(loggingWrapper(event -> event.setMdc(new LinkedHashMap<>())));
        // mdc null cases
        result.add(loggingWrapper(event -> {
            Map<String, String> mdc = new LinkedHashMap<>();
            mdc.put("nullMdcValueKey", null);
            event.setMdc(mdc);
        }));
        Map<String, String> nullMdcOnlyMap = new LinkedHashMap<>();
        nullMdcOnlyMap.put(null, "nullMdcKeyValue");
        result.add(loggingWrapper(event -> event.setMdc(nullMdcOnlyMap)));

        // #100
        // request headers
        result.add(accessWrapper(event -> event.setRequestHeaders(new LinkedHashMap<>())));
        result.add(accessWrapper(event -> {
            Map<String, String> headers = new LinkedHashMap<>();
            headers.put("requestHeaderKey", "requestHeaderValue");
            event.setRequestHeaders(headers);
        }));
        result.add(accessWrapper(event -> {
            Map<String, String> headers = new LinkedHashMap<>();
            headers.put("nullRequestHeaderValueKey", null);
            event.setRequestHeaders(headers);
        }));
        Map<String, String> nullRequestHeaderKeyMap = new LinkedHashMap<>();
        nullRequestHeaderKeyMap.put(null, "nullRequestHeaderKeyValue");
        result.add(accessWrapper(event -> event.setRequestHeaders(nullRequestHeaderKeyMap)));

        // response headers
        result.add(accessWrapper(event -> event.setResponseHeaders(new LinkedHashMap<>())));
        result.add(accessWrapper(event -> {
            Map<String, String> headers = new LinkedHashMap<>();
            headers.put("responseHeaderKey", "responseHeaderValue");
            event.setResponseHeaders(headers);
        }));
        result.add(accessWrapper(event -> {
            Map<String, String> headers = new LinkedHashMap<>();
            headers.put("nullResponseHeaderValueKey", null);
            event.setResponseHeaders(headers);
        }));
        Map<String, String> nullResponseHeaderKeyMap = new LinkedHashMap<>();
        nullResponseHeaderKeyMap.put(null, "nullResponseHeaderKeyValue");
        result.add(accessWrapper(event -> event.setResponseHeaders(nullResponseHeaderKeyMap)));

        // request parameters
        result.add(accessWrapper(event -> event.setRequestParameters(new LinkedHashMap<>())));
        result.add(accessWrapper(event -> {
            Map<String, String[]> parameters = new LinkedHashMap<>();
            parameters.put("nullRequestParameterValueKey", null);
            event.setRequestParameters(parameters);
        }));

        // #110
        result.add(accessWrapper(event -> {
            Map<String, String[]> parameters = new LinkedHashMap<>();
            parameters.put("requestParameterKey", new String[0]);
            event.setRequestParameters(parameters);
        }));
        result.add(accessWrapper(event -> {
            Map<String, String[]> parameters = new LinkedHashMap<>();
            parameters.put("requestParameterKey", new String[]{"requestParameterValue1", "requestParameterValue2"});
            event.setRequestParameters(parameters);
        }));
        result.add(accessWrapper(event -> {
            Map<String, String[]> parameters = new LinkedHashMap<>();
            parameters.put("requestParameterKey", new String[]{"requestParameterValue1", null, "requestParameterValue3"});
            event.setRequestParameters(parameters);
        }));
        Map<String, String[]> nullRequestParameterKeyMap = new LinkedHashMap<>();
        nullRequestParameterKeyMap.put(null, new String[]{"nullRequestHeaderKeyValue"});
        result.add(accessWrapper(event -> event.setRequestParameters(nullRequestParameterKeyMap)));

        // empty String logger name
        result.add(loggingWrapper(event -> event.setLogger("")));

        // empty String throwable name
        result.add(loggingWrapper(event -> event.setThrowable(configure(new ThrowableInfo(), throwable -> throwable.setName("")))));

        // missing first call stack entry
        result.add(loggingWrapper(event -> event.setCallStack(new ExtendedStackTraceElement[]{
            null,
            parse("de.huxhorn.lilith.debug.DebugDialog$LogAllAction.actionPerformed(DebugDialog.java:358) ~[de.huxhorn.lilith-8.1.0-SNAPSHOT.jar:na]"),
            parse("javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2348) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402) ~[na:1.8.0_92]"),
            parse("javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259) ~[na:1.8.0_92]")
        })));

        // thread priority
        result.add(loggingWrapper(event -> event.setThreadInfo(configure(new ThreadInfo(), threadInfo -> threadInfo.setPriority(7)))));

        result.add(accessWrapper(event -> event.setStatusCode(99))); // status code too small
        result.add(accessWrapper(event -> event.setStatusCode(600))); // status code too large
        // #120
        result.add(accessWrapper(event -> event.setStatusCode(488))); // unknown status code

        // mdc with null key and other key/value
        Map<String, String> nullMdcKeyMap = new LinkedHashMap<>();
        nullMdcKeyMap.put(null, "nullMdcKeyValue");
        nullMdcKeyMap.put("nonNullKey", "nonNullValue");
        result.add(loggingWrapper(event -> event.setMdc(nullMdcKeyMap)));

        // multi-level request URI
        result.add(accessWrapper(event -> event.setRequestURI("/foo/bar/foobar")));

        return result;
    }

    public static Set<Integer> matchAllSet()
    {
        return MATCH_ALL_SET;
    }

    public static Set<Integer> matchAllSet(List<Object> corpus)
    {
        Objects.requireNonNull(corpus, "corpus must not be null!");
        if(corpus.isEmpty())
        {
            return new LinkedHashSet<>();
        }
        Set<Integer> result = new LinkedHashSet<>(corpus.size());
        for(int i = 0; i < corpus.size(); i++)
        {
            result.add(i);
        }
        return result;
    }

    public static Set<Integer> matchAnyLoggingEventSet()
    {
        return MATCH_ANY_LOGGING_SET;
    }

    public static Set<Integer> matchAnyLoggingEventSet(List<Object> corpus)
    {
        Set<Integer> result = new LinkedHashSet<>();
        for(int i = 0; i < corpus.size(); i++)
        {
            Object current = corpus.get(i);
            if(current instanceof EventWrapper)
            {
                Object event = ((EventWrapper<?>) current).getEvent();
                if(event instanceof LoggingEvent)
                {
                    result.add(i);
                }
            }
        }
        return result;
    }

    public static Set<Integer> matchAnyAccessEventSet()
    {
        return MATCH_ANY_ACCESS_SET;
    }

    public static Set<Integer> matchAnyAccessEventSet(List<Object> corpus)
    {
        Set<Integer> result = new LinkedHashSet<>();
        for(int i = 0; i < corpus.size(); i++)
        {
            Object current = corpus.get(i);
            if(current instanceof EventWrapper)
            {
                Object event = ((EventWrapper<?>) current).getEvent();
                if(event instanceof AccessEvent)
                {
                    result.add(i);
                }
            }
        }
        return result;
    }

    public static Set<Integer> matchAnyLoggingOrAccessEventSet()
    {
        return MATCH_ANY_LOGGING_OR_ACCESS_SET;
    }

    public static Set<Integer> matchAnyLoggingOrAccessEventSet(List<Object> corpus)
    {
        Set<Integer> result = new LinkedHashSet<>();
        for(int i = 0; i < corpus.size(); i++)
        {
            Object current = corpus.get(i);
            if(current instanceof EventWrapper)
            {
                Object event = ((EventWrapper<?>) current).getEvent();
                if(event instanceof AccessEvent || event instanceof LoggingEvent)
                {
                    result.add(i);
                }
            }
        }
        return result;
    }

    public static Set<Integer> matchAnyEventWrapperSet()
    {
        return MATCH_ANY_WRAPPER_SET;
    }

    public static Set<Integer> matchAnyEventWrapperSet(List<Object> corpus)
    {
        Set<Integer> result = new LinkedHashSet<>();
        for(int i = 0; i < corpus.size(); i++)
        {
            Object current = corpus.get(i);
            if(current instanceof EventWrapper)
            {
                result.add(i);
            }
        }
        return result;
    }

    private static <T extends Serializable> EventWrapper<T> wrap(T event)
    {
        EventWrapper<T> wrapper = new EventWrapper<>();
        wrapper.setEvent(event);
        return wrapper;
    }

    private static EventWrapper<LoggingEvent> loggingWrapper()
    {
        return loggingWrapper(null);
    }

    private static EventWrapper<LoggingEvent> loggingWrapper(Consumer<LoggingEvent> customizer)
    {
        LoggingEvent event = new LoggingEvent();
        if(customizer != null)
        {
            customizer.accept(event);
        }
        return wrap(event);
    }

    private static EventWrapper<AccessEvent> accessWrapper()
    {
        return accessWrapper(null);
    }

    private static EventWrapper<AccessEvent> accessWrapper(Consumer<AccessEvent> customizer)
    {
        AccessEvent event = new AccessEvent();
        if(customizer != null)
        {
            customizer.accept(event);
        }
        return wrap(event);
    }

    private static <T> T configure(T instance, Consumer<T> customizer)
    {
        customizer.accept(instance);
        return instance;
    }

    private static ExtendedStackTraceElement parse(String stackTrace)
    {
        return ExtendedStackTraceElement.parseStackTraceElement(stackTrace);
    }

    private static class Foo
        implements Serializable
    {
        private static final long serialVersionUID = -5207922872610875882L;
    }
}
