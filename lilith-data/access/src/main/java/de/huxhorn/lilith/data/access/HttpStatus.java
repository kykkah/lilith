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

package de.huxhorn.lilith.data.access;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of HTTP status codes grouped by their semantic category.
 */
@SuppressWarnings("doclint:missing")
public enum HttpStatus
{
	// Informational 1xx
	/** Continue (HTTP 100). */
	CONTINUE(100, Type.INFORMATIONAL, "Continue", Specification.RFC7231),
	/** Switching Protocols (HTTP 101). */
	SWITCHING_PROTOCOLS(101, Type.INFORMATIONAL, "Switching Protocols", Specification.RFC7231),
	/** Processing (WebDAV) (HTTP 102). */
	PROCESSING(102, Type.INFORMATIONAL, "Processing (WebDAV)", Specification.RFC2518),


	// Successful 2xx
	/** OK (HTTP 200). */
	OK(200, Type.SUCCESSFUL, "OK", Specification.RFC7231),
	/** Created (HTTP 201). */
	CREATED(201, Type.SUCCESSFUL, "Created", Specification.RFC7231),
	/** Accepted (HTTP 202). */
	ACCEPTED(202, Type.SUCCESSFUL, "Accepted", Specification.RFC7231),
	/** Non-Authoritative Information (HTTP 203). */
	NON_AUTHORITATIVE_INFORMATION(203, Type.SUCCESSFUL, "Non-Authoritative Information", Specification.RFC7231),
	/** No Content (HTTP 204). */
	NO_CONTENT(204, Type.SUCCESSFUL, "No Content", Specification.RFC7231),
	/** Reset Content (HTTP 205). */
	RESET_CONTENT(205, Type.SUCCESSFUL, "Reset Content", Specification.RFC7231),
	/** Partial Content (HTTP 206). */
	PARTIAL_CONTENT(206, Type.SUCCESSFUL, "Partial Content", Specification.RFC7233),
	/** Multi-Status (WebDAV) (HTTP 207). */
	MULTI_STATUS(207, Type.SUCCESSFUL, "Multi-Status (WebDAV)", Specification.RFC4918),
	/** Already Reported (WebDAV) (HTTP 208). */
	ALREADY_REPORTED(208, Type.SUCCESSFUL, "Already Reported (WebDAV)", Specification.RFC5842),
	/** IM Used (HTTP 226). */
	IM_USED(226, Type.SUCCESSFUL, "IM Used", Specification.RFC3229),


	// Redirection 3xx
	/** Multiple Choices (HTTP 300). */
	MULTIPLE_CHOICES(300, Type.REDIRECTION, "Multiple Choices", Specification.RFC7231),
	/** Moved Permanently (HTTP 301). */
	MOVED_PERMANENTLY(301, Type.REDIRECTION, "Moved Permanently", Specification.RFC7231),
	/** Found (HTTP 302). */
	FOUND(302, Type.REDIRECTION, "Found", Specification.RFC7231),
	/**  See Other (HTTP 303). */
	SEE_OTHER(303, Type.REDIRECTION, " See Other", Specification.RFC7231),
	/** Not Modified (HTTP 304). */
	NOT_MODIFIED(304, Type.REDIRECTION, "Not Modified", Specification.RFC7232),
	/** Use Proxy (HTTP 305). */
	USE_PROXY(305, Type.REDIRECTION, "Use Proxy", Specification.RFC7231, true),
	/** (Unused) (HTTP 306). */
	UNUSED(306, Type.REDIRECTION, "(Unused)", Specification.RFC7231),
	/** Temporary Redirect (HTTP 307). */
	TEMPORARY_REDIRECT(307, Type.REDIRECTION, "Temporary Redirect", Specification.RFC7231),
	/** Permanent Redirect (HTTP 308). */
	PERMANENT_REDIRECT(308, Type.REDIRECTION, "Permanent Redirect", Specification.RFC7238),


	// Client Error 4xx
	/** Bad Request (HTTP 400). */
	BAD_REQUEST(400, Type.CLIENT_ERROR, "Bad Request", Specification.RFC7231),
	/** Unauthorized (HTTP 401). */
	UNAUTHORIZED(401, Type.CLIENT_ERROR, "Unauthorized", Specification.RFC7235),
	/** Payment Required (HTTP 402). */
	PAYMENT_REQUIRED(402, Type.CLIENT_ERROR, "Payment Required", Specification.RFC7231),
	/** Forbidden (HTTP 403). */
	FORBIDDEN(403, Type.CLIENT_ERROR, "Forbidden", Specification.RFC7231),
	/** Not Found (HTTP 404). */
	NOT_FOUND(404, Type.CLIENT_ERROR, "Not Found", Specification.RFC7231),
	/** Method Not Allowed (HTTP 405). */
	METHOD_NOT_ALLOWED(405, Type.CLIENT_ERROR, "Method Not Allowed", Specification.RFC7231),
	/** Not Acceptable (HTTP 406). */
	NOT_ACCEPTABLE(406, Type.CLIENT_ERROR, "Not Acceptable", Specification.RFC7231),
	/** Proxy Authentication Required (HTTP 407). */
	PROXY_AUTHENTICATION_REQUIRED(407, Type.CLIENT_ERROR, "Proxy Authentication Required", Specification.RFC7235),
	/** Request Timeout (HTTP 408). */
	REQUEST_TIMEOUT(408, Type.CLIENT_ERROR, "Request Timeout", Specification.RFC7231),
	/** Conflict (HTTP 409). */
	CONFLICT(409, Type.CLIENT_ERROR, "Conflict", Specification.RFC7231),
	/** Gone (HTTP 410). */
	GONE(410, Type.CLIENT_ERROR, "Gone", Specification.RFC7231),
	/** Length Required (HTTP 411). */
	LENGTH_REQUIRED(411, Type.CLIENT_ERROR, "Length Required", Specification.RFC7231),
	/** Precondition Failed (HTTP 412). */
	PRECONDITION_FAILED(412, Type.CLIENT_ERROR, "Precondition Failed", Specification.RFC7232),
	/** Payload Too Large (HTTP 413). */
	PAYLOAD_TOO_LARGE(413, Type.CLIENT_ERROR, "Payload Too Large", Specification.RFC7231),
	/** URI Too Long (HTTP 414). */
	URI_TOO_LONG(414, Type.CLIENT_ERROR, "URI Too Long", Specification.RFC7231),
	/** Unsupported Media Type (HTTP 415). */
	UNSUPPORTED_MEDIA_TYPE(415, Type.CLIENT_ERROR, "Unsupported Media Type", Specification.RFC7231),
	/** Range Not Satisfiable (HTTP 416). */
	RANGE_NOT_SATISFIABLE(416, Type.CLIENT_ERROR, "Range Not Satisfiable", Specification.RFC7233),
	/** Expectation Failed (HTTP 417). */
	EXPECTATION_FAILED(417, Type.CLIENT_ERROR, "Expectation Failed", Specification.RFC7231),
	/** I'm a teapot (HTTP 418). */
	IM_A_TEAPOT(418, Type.CLIENT_ERROR, "I'm a teapot", Specification.RFC2324),
	/** Enhance Your Calm (Twitter) (HTTP 420). */
	ENHANCE_YOUR_CALM(420, Type.CLIENT_ERROR, "Enhance Your Calm (Twitter)", null /* Twitter */),
	/** Unprocessable Entity (WebDAV) (HTTP 422). */
	UNPROCESSABLE_ENTITY(422, Type.CLIENT_ERROR, "Unprocessable Entity (WebDAV)", Specification.RFC4918),
	/** Locked (WebDAV) (HTTP 423). */
	LOCKED(423, Type.CLIENT_ERROR, "Locked (WebDAV)", Specification.RFC4918),
	/** Failed Dependency (WebDAV) (HTTP 424). */
	FAILED_DEPENDENCY(424, Type.CLIENT_ERROR, "Failed Dependency (WebDAV)", Specification.RFC4918),
	/** Unordered Collection (WebDAV, draft) (HTTP 425). */
	UNORDERED_COLLECTION(425, Type.CLIENT_ERROR, "Unordered Collection (WebDAV, draft)", Specification.RFC3648),
	/** Upgrade Required (HTTP 426). */
	UPGRADE_REQUIRED(426, Type.CLIENT_ERROR, "Upgrade Required", Specification.RFC7231),
	/** Precondition Required (HTTP 428). */
	PRECONDITION_REQUIRED(428, Type.CLIENT_ERROR, "Precondition Required", Specification.RFC6585),
	/** Too Many Requests (HTTP 429). */
	TOO_MANY_REQUESTS(429, Type.CLIENT_ERROR, "Too Many Requests", Specification.RFC6585),
	/** Request Header Fields Too Large (HTTP 431). */
	REQUEST_HEADER_FIELDS_TOO_LARGE(431, Type.CLIENT_ERROR, "Request Header Fields Too Large", Specification.RFC6585),
	/** No Response (Nginx) (HTTP 444). */
	NO_RESPONSE(444, Type.CLIENT_ERROR, "No Response (Nginx)", Specification.NGINX),
	/** Retry With (HTTP 449). */
	RETRY_WITH(449, Type.CLIENT_ERROR, "Retry With", Specification.MICROSOFT),
	/** Blocked by Windows Parental Controls (HTTP 450). */
	BLOCKED_BY_WINDOWS_PARENTAL_CONTROLS(450, Type.CLIENT_ERROR, "Blocked by Windows Parental Controls", Specification.MICROSOFT),
	/** Unavailable For Legal Reasons (HTTP 451). */
	UNAVAILABLE_FOR_LEGAL_REASONS(451, Type.CLIENT_ERROR, "Unavailable For Legal Reasons", Specification.RFC7725),
	/** Client Closed Request (Nginx) (HTTP 499). */
	CLIENT_CLOSED_REQUEST(499, Type.CLIENT_ERROR, "Client Closed Request (Nginx)", Specification.NGINX),


	// Server Error 5xx
	/** Internal Server Error (HTTP 500). */
	INTERNAL_SERVER_ERROR(500, Type.SERVER_ERROR, "Internal Server Error", Specification.RFC7231),
	/** Not Implemented (HTTP 501). */
	NOT_IMPLEMENTED(501, Type.SERVER_ERROR, "Not Implemented", Specification.RFC7231),
	/** Bad Gateway (HTTP 502). */
	BAD_GATEWAY(502, Type.SERVER_ERROR, "Bad Gateway", Specification.RFC7231),
	/** Service Unavailable (HTTP 503). */
	SERVICE_UNAVAILABLE(503, Type.SERVER_ERROR, "Service Unavailable", Specification.RFC7231),
	/** Gateway Timeout (HTTP 504). */
	GATEWAY_TIMEOUT(504, Type.SERVER_ERROR, "Gateway Timeout", Specification.RFC7231),
	/** HTTP Version Not Supported (HTTP 505). */
	HTTP_VERSION_NOT_SUPPORTED(505, Type.SERVER_ERROR, "HTTP Version Not Supported", Specification.RFC7231),
	/** Variant Also Negotiates (HTTP 506). */
	VARIANT_ALSO_NEGOTIATES(506, Type.SERVER_ERROR, "Variant Also Negotiates", Specification.RFC2295),
	/** Insufficient Storage (WebDAV) (HTTP 507). */
	INSUFFICIENT_STORAGE(507, Type.SERVER_ERROR, "Insufficient Storage (WebDAV)", Specification.RFC4918),
	/** Loop Detected (WebDAV) (HTTP 508). */
	LOOP_DETECTED(508, Type.SERVER_ERROR, "Loop Detected (WebDAV)", Specification.RFC5842),
	/** Bandwidth Limit Exceeded (HTTP 509). */
	BANDWIDTH_LIMIT_EXCEEDED(509, Type.SERVER_ERROR, "Bandwidth Limit Exceeded", Specification.APACHE),
	/** Not Extended (HTTP 510). */
	NOT_EXTENDED(510, Type.SERVER_ERROR, "Not Extended", Specification.RFC2774),
	/** Network Authentication Required (HTTP 511). */
	NETWORK_AUTHENTICATION_REQUIRED(511, Type.SERVER_ERROR, "Network Authentication Required", Specification.RFC6585),
	/** Network read timeout error (HTTP 598). */
	NETWORK_READ_TIMEOUT_ERROR(598, Type.SERVER_ERROR, "Network read timeout error", Specification.MICROSOFT),
	/** Network connect timeout error (HTTP 599). */
	NETWORK_CONNECT_TIMEOUT_ERROR(599, Type.SERVER_ERROR, "Network connect timeout error", Specification.MICROSOFT);


	private int code;
	private Type type;
	private Specification specification;
	private String description;
	private boolean deprecated;

	private static final Map<Integer, HttpStatus> CODE_MAP = new HashMap<>();

	static
	{
		for(HttpStatus code : HttpStatus.values())
		{
			HttpStatus previous = CODE_MAP.put(code.getCode(), code);
			if(previous != null)
			{
				throw new IllegalStateException("Duplicate entry for HttpStatus "+code.getCode()+"!");
			}
		}
	}

	/**
	 * Resolves the {@link HttpStatus} for the given numeric status code.
	 *
	 * @param code the HTTP status code
	 * @return the matching {@link HttpStatus} or {@code null} if the code is unknown
	 */
	public static HttpStatus getStatus(int code)
	{
		return CODE_MAP.get(code);
	}

	/**
	 * Returns the {@link Type} bucket the given status code belongs to.
	 *
	 * @param code the HTTP status code
	 * @return the status type or {@code null} if the code does not map to a known range
	 */
	public static HttpStatus.Type getType(int code)
	{
		if(code >= 100 && code < 200)
		{
			return Type.INFORMATIONAL;
		}
		if(code >= 200 && code < 300)
		{
			return Type.SUCCESSFUL;
		}
		if(code >= 300 && code < 400)
		{
			return Type.REDIRECTION;
		}
		if(code >= 400 && code < 500)
		{
			return Type.CLIENT_ERROR;
		}
		if(code >= 500 && code < 600)
		{
			return Type.SERVER_ERROR;
		}
		return null;
	}

	HttpStatus(int code, Type type, String description, Specification specification)
	{
	    this(code, type, description, specification, false);
	}

	HttpStatus(int code, Type type, String description, Specification specification, boolean deprecated)
	{
		this.code = code;
		this.type = type;
		this.description = description;
		this.specification = specification;
		this.deprecated = deprecated;
	}

	/**
	 * Returns the numeric HTTP status code.
	 *
	 * @return the status code
	 */
	public int getCode()
	{
		return code;
	}

	/**
	 * Returns the semantic category of this status code.
	 *
	 * @return the status type
	 */
	public Type getType()
	{
		return type;
	}

	/**
	 * Returns the specification that defines the status code.
	 *
	 * @return the defining specification or {@code null} for vendor extensions
	 */
	public Specification getSpecification()
	{
		return specification;
	}

	/**
	 * Returns the human readable description of the status.
	 *
	 * @return the description string
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Indicates whether the status code is deprecated.
	 *
	 * @return {@code true} if the status is marked as deprecated
	 */
	public boolean isDeprecated()
	{
		return deprecated;
	}

	public enum Type
	{
		/** Informational responses (1xx). */
		INFORMATIONAL("Informational", "1xx"),
		/** Successful responses (2xx). */
		SUCCESSFUL   ("Successful",    "2xx"),
		/** Redirect responses (3xx). */
		REDIRECTION  ("Redirection",   "3xx"),
		/** Client error responses (4xx). */
		CLIENT_ERROR ("Client Error",  "4xx"),
		/** Server error responses (5xx). */
		SERVER_ERROR ("Server Error",  "5xx");

		private final String description;
		private final String range;

		Type(String description, String range)
		{
			this.description = description;
			this.range = range;
		}

		/**
		 * Returns the canonical range notation (e.g. {@code "2xx"}).
		 *
		 * @return the range string
		 */
		public String getRange()
		{
			return range;
		}

		@Override
		public String toString()
		{
			return description;
		}
	}

	public enum Specification
	{
		/** RFC 2295 - Transparent Content Negotiation in HTTP. */
		RFC2295("RFC 2295", "Transparent Content Negotiation in HTTP"),
		/** RFC 2324 - Hyper Text Coffee Pot Control Protocol. */
		RFC2324("RFC 2324", "Hyper Text Coffee Pot Control Protocol"),
		/** RFC 2518 - HTTP Extensions for Distributed Authoring -- WEBDAV. */
		RFC2518("RFC 2518", "HTTP Extensions for Distributed Authoring -- WEBDAV"),
		/** RFC 2774 - An HTTP Extension Framework. */
		RFC2774("RFC 2774", "An HTTP Extension Framework"),
		/** RFC 2817 - Upgrading to TLS Within HTTP/1.1. */
		RFC2817("RFC 2817", "Upgrading to TLS Within HTTP/1.1"),
		/** RFC 3229 - Delta encoding in HTTP. */
		RFC3229("RFC 3229", "Delta encoding in HTTP"),
		/** RFC 3648 - Web Distributed Authoring and Versioning (WebDAV) Ordered Collections Protocol. */
		RFC3648("RFC 3648", "Web Distributed Authoring and Versioning (WebDAV) Ordered Collections Protocol"),
		/** RFC 4918 - HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV). */
		RFC4918("RFC 4918", "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"),
		/** RFC 5842 - Binding Extensions to Web Distributed Authoring and Versioning (WebDAV). */
		RFC5842("RFC 5842", "Binding Extensions to Web Distributed Authoring and Versioning (WebDAV)"),
		/** RFC 6585 - Additional HTTP Status Codes. */
		RFC6585("RFC 6585", "Additional HTTP Status Codes"),
		/** RFC 7231 - HTTP/1.1 Semantics and Content. */
		RFC7231("RFC 7231", "HTTP/1.1 Semantics and Content"),
		/** RFC 7232 - HTTP/1.1 Conditional Requests. */
		RFC7232("RFC 7232", "HTTP/1.1 Conditional Requests"),
		/** RFC 7233 - HTTP/1.1 Range Requests. */
		RFC7233("RFC 7233", "HTTP/1.1 Range Requests"),
		/** RFC 7235 - HTTP/1.1 Authentication. */
		RFC7235("RFC 7235", "HTTP/1.1 Authentication"),
		/** RFC 7238 - The Hypertext Transfer Protocol Status Code 308 (Permanent Redirect). */
		RFC7238("RFC 7238", "The Hypertext Transfer Protocol Status Code 308 (Permanent Redirect)"),
		/** RFC 7725 - An HTTP Status Code to Report Legal Obstacles. */
		RFC7725("RFC 7725", "An HTTP Status Code to Report Legal Obstacles"),
		/** Nginx - Nginx HTTP server extensions. */
		NGINX("Nginx", "Nginx HTTP server extensions"),
		/** Apache - Apache extensions. */
		APACHE("Apache", "Apache extensions"),
		/** Microsoft - Microsoft extensions. */
		MICROSOFT("Microsoft", "Microsoft extensions");

		private String identifier;
		private String description;

		Specification(String identifier, String description)
		{
			this.identifier = identifier;
			this.description = description;
		}

		@Override
		public String toString()
		{
			return identifier+" - "+description;
		}
	}
}
