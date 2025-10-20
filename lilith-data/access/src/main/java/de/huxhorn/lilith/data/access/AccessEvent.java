/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2014 Joern Huxhorn
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
 * Copyright 2007-2014 Joern Huxhorn
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

package de.huxhorn.lilith.data.access;

import de.huxhorn.lilith.data.eventsource.LoggerContext;
import java.io.Serializable;
import java.util.Map;

/**
 * Captures the details of a single HTTP access event produced by the Lilith agent.
 */
public class AccessEvent
	implements Serializable
{
	private static final long serialVersionUID = -942687545417047646L;

	/** Timestamp of the access event in milliseconds since the epoch. */
	private Long timeStamp;
	/** Time spent processing the request in milliseconds. */
	private Long elapsedTime;
	/** Logger context originating the event. */
	private LoggerContext loggerContext;
	/** Request URI (without scheme or host). */
	private String requestURI;
	/** Full request URL. */
	private String requestURL;
	/** Client host name if resolvable. */
	private String remoteHost;
	/** Authenticated remote user. */
	private String remoteUser;
	/** Protocol string, e.g. {@code HTTP/1.1}. */
	private String protocol;
	/** HTTP method used by the client. */
	private String method;
	/** Server host name handling the request. */
	private String serverName;
	/** Client IP address. */
	private String remoteAddress;
	/** Captured request headers. */
	private Map<String, String> requestHeaders;
	/** Captured response headers. */
	private Map<String, String> responseHeaders;
	/** Captured request parameters. */
	private Map<String, String[]> requestParameters;
	/** Local port accepting the connection. */
	private int localPort;
	/** HTTP response status code. */
	private int statusCode;

	/**
	 * Returns the event timestamp.
	 *
	 * @return the timestamp in milliseconds since the epoch or {@code null} if unknown
	 */
	public Long getTimeStamp()
	{
		return timeStamp;
	}

	/**
	 * Sets the event timestamp.
	 *
	 * @param timeStamp the timestamp in milliseconds since the epoch
	 */
	public void setTimeStamp(Long timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	/**
	 * Returns the request processing time.
	 *
	 * @return the elapsed time in milliseconds or {@code null} if not measured
	 */
	public Long getElapsedTime()
	{
		return elapsedTime;
	}

	/**
	 * Sets the request processing time.
	 *
	 * @param elapsedTime the elapsed time in milliseconds
	 */
	public void setElapsedTime(Long elapsedTime)
	{
		this.elapsedTime = elapsedTime;
	}

	/**
	 * Returns the logger context that generated the event.
	 *
	 * @return the logger context or {@code null} if not available
	 */
	public LoggerContext getLoggerContext()
	{
		return loggerContext;
	}

	/**
	 * Associates the event with the given logger context.
	 *
	 * @param loggerContext the logger context to store
	 */
	public void setLoggerContext(LoggerContext loggerContext)
	{
		this.loggerContext = loggerContext;
	}

	/**
	 * Returns the request URI (no scheme/host).
	 *
	 * @return the request URI or {@code null}
	 */
	public String getRequestURI()
	{
		return requestURI;
	}

	/**
	 * Sets the request URI (no scheme/host).
	 *
	 * @param requestURI the request URI
	 */
	public void setRequestURI(String requestURI)
	{
		this.requestURI = requestURI;
	}

	/**
	 * Returns the full request URL.
	 *
	 * @return the request URL or {@code null}
	 */
	public String getRequestURL()
	{
		return requestURL;
	}

	/**
	 * Sets the full request URL.
	 *
	 * @param requestURL the request URL
	 */
	public void setRequestURL(String requestURL)
	{
		this.requestURL = requestURL;
	}

	/**
	 * Returns the remote host name.
	 *
	 * @return the remote host or {@code null}
	 */
	public String getRemoteHost()
	{
		return remoteHost;
	}

	/**
	 * Sets the remote host name.
	 *
	 * @param remoteHost the remote host
	 */
	public void setRemoteHost(String remoteHost)
	{
		this.remoteHost = remoteHost;
	}

	/**
	 * Returns the authenticated remote user.
	 *
	 * @return the remote user or {@code null}
	 */
	public String getRemoteUser()
	{
		return remoteUser;
	}

	/**
	 * Sets the authenticated remote user.
	 *
	 * @param remoteUser the remote user name
	 */
	public void setRemoteUser(String remoteUser)
	{
		this.remoteUser = remoteUser;
	}

	/**
	 * Returns the protocol of the request.
	 *
	 * @return the protocol (e.g. {@code HTTP/1.1}) or {@code null}
	 */
	public String getProtocol()
	{
		return protocol;
	}

	/**
	 * Sets the protocol of the request.
	 *
	 * @param protocol the protocol string
	 */
	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	/**
	 * Returns the HTTP method of the request.
	 *
	 * @return the method or {@code null}
	 */
	public String getMethod()
	{
		return method;
	}

	/**
	 * Sets the HTTP method of the request.
	 *
	 * @param method the method string
	 */
	public void setMethod(String method)
	{
		this.method = method;
	}

	/**
	 * Returns the server name that handled the request.
	 *
	 * @return the server name or {@code null}
	 */
	public String getServerName()
	{
		return serverName;
	}

	/**
	 * Sets the server name that handled the request.
	 *
	 * @param serverName the server name
	 */
	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	/**
	 * Returns the remote IP address.
	 *
	 * @return the remote address or {@code null}
	 */
	public String getRemoteAddress()
	{
		return remoteAddress;
	}

	/**
	 * Sets the remote IP address.
	 *
	 * @param remoteAddress the remote address
	 */
	public void setRemoteAddress(String remoteAddress)
	{
		this.remoteAddress = remoteAddress;
	}

	/**
	 * Returns the captured request headers.
	 *
	 * @return a map of request headers or {@code null}
	 */
	public Map<String, String> getRequestHeaders()
	{
		return requestHeaders;
	}

	/**
	 * Sets the captured request headers.
	 *
	 * @param requestHeaders the request headers to store
	 */
	public void setRequestHeaders(Map<String, String> requestHeaders)
	{
		this.requestHeaders = requestHeaders;
	}

	/**
	 * Returns the captured response headers.
	 *
	 * @return a map of response headers or {@code null}
	 */
	public Map<String, String> getResponseHeaders()
	{
		return responseHeaders;
	}

	/**
	 * Sets the captured response headers.
	 *
	 * @param responseHeaders the response headers to store
	 */
	public void setResponseHeaders(Map<String, String> responseHeaders)
	{
		this.responseHeaders = responseHeaders;
	}

	/**
	 * Returns the captured request parameters.
	 *
	 * @return a map of request parameters or {@code null}
	 */
	public Map<String, String[]> getRequestParameters()
	{
		return requestParameters;
	}

	/**
	 * Sets the captured request parameters.
	 *
	 * @param requestParameters the request parameters to store
	 */
	public void setRequestParameters(Map<String, String[]> requestParameters)
	{
		this.requestParameters = requestParameters;
	}

	/**
	 * Returns the local port that accepted the request.
	 *
	 * @return the local port number
	 */
	public int getLocalPort()
	{
		return localPort;
	}

	/**
	 * Sets the local port that accepted the request.
	 *
	 * @param localPort the local port number
	 */
	public void setLocalPort(int localPort)
	{
		this.localPort = localPort;
	}

	/**
	 * Returns the HTTP response status code.
	 *
	 * @return the response status code
	 */
	public int getStatusCode()
	{
		return statusCode;
	}

	/**
	 * Sets the HTTP response status code.
	 *
	 * @param statusCode the response status code
	 */
	public void setStatusCode(int statusCode)
	{
		this.statusCode = statusCode;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AccessEvent that = (AccessEvent) o;

		if (localPort != that.localPort) return false;
		if (statusCode != that.statusCode) return false;
		if (elapsedTime != null ? !elapsedTime.equals(that.elapsedTime) : that.elapsedTime != null) return false;
		if (loggerContext != null ? !loggerContext.equals(that.loggerContext) : that.loggerContext != null)
			return false;
		if (method != null ? !method.equals(that.method) : that.method != null) return false;
		if (protocol != null ? !protocol.equals(that.protocol) : that.protocol != null) return false;
		if (remoteAddress != null ? !remoteAddress.equals(that.remoteAddress) : that.remoteAddress != null)
			return false;
		if (remoteHost != null ? !remoteHost.equals(that.remoteHost) : that.remoteHost != null) return false;
		if (remoteUser != null ? !remoteUser.equals(that.remoteUser) : that.remoteUser != null) return false;
		if (requestHeaders != null ? !requestHeaders.equals(that.requestHeaders) : that.requestHeaders != null)
			return false;
		// unusable, map.equals does not work with array values.
		//if (requestParameters != null ? !requestParameters.equals(that.requestParameters) : that.requestParameters != null) return false;
		if (requestURI != null ? !requestURI.equals(that.requestURI) : that.requestURI != null) return false;
		if (requestURL != null ? !requestURL.equals(that.requestURL) : that.requestURL != null) return false;
		if (responseHeaders != null ? !responseHeaders.equals(that.responseHeaders) : that.responseHeaders != null)
			return false;
		if (serverName != null ? !serverName.equals(that.serverName) : that.serverName != null) return false;

		return !(timeStamp != null ? !timeStamp.equals(that.timeStamp) : that.timeStamp != null);
	}

	@Override
	public int hashCode()
	{
		int result = timeStamp != null ? timeStamp.hashCode() : 0;
		result = 31 * result + (elapsedTime != null ? elapsedTime.hashCode() : 0);
		result = 31 * result + (loggerContext != null ? loggerContext.hashCode() : 0);
		result = 31 * result + (requestURI != null ? requestURI.hashCode() : 0);
		result = 31 * result + (requestURL != null ? requestURL.hashCode() : 0);
		result = 31 * result + (remoteHost != null ? remoteHost.hashCode() : 0);
		result = 31 * result + (remoteUser != null ? remoteUser.hashCode() : 0);
		result = 31 * result + (protocol != null ? protocol.hashCode() : 0);
		result = 31 * result + (method != null ? method.hashCode() : 0);
		result = 31 * result + (serverName != null ? serverName.hashCode() : 0);
		result = 31 * result + (remoteAddress != null ? remoteAddress.hashCode() : 0);
		result = 31 * result + localPort;
		result = 31 * result + statusCode;
		return result;
	}

	@Override
	public String toString()
	{
		return "AccessEvent{" +
				"timeStamp=" + timeStamp +
				", elapsedTime=" + elapsedTime +
				", loggerContext=" + loggerContext +
				", requestURI='" + requestURI + '\'' +
				", requestURL='" + requestURL + '\'' +
				", remoteHost='" + remoteHost + '\'' +
				", remoteUser='" + remoteUser + '\'' +
				", protocol='" + protocol + '\'' +
				", method='" + method + '\'' +
				", serverName='" + serverName + '\'' +
				", remoteAddress='" + remoteAddress + '\'' +
				", requestHeaders=" + requestHeaders +
				", responseHeaders=" + responseHeaders +
				", requestParameters=" + requestParameters +
				", localPort=" + localPort +
				", statusCode=" + statusCode +
				'}';
	}
}
