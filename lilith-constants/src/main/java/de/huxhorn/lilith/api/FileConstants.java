/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2011 Joern Huxhorn
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
package de.huxhorn.lilith.api;

/**
 * Constants shared between Lilith file formats and metadata keys.
 */
public interface FileConstants
{
	/** Magic header written at the start of Lilith container files. */
	int MAGIC_VALUE = 0x0B5E55ED;

	/** File extension for primary Lilith archive files. */
	String FILE_EXTENSION = ".lilith";
	/** File extension used for index sidecar files. */
	String INDEX_FILE_EXTENSION = ".idx";
	/** File extension appended while files are actively written. */
	String ACTIVE_FILE_EXTENSION = ".active";

	/** Metadata key storing the primary identifier. */
	String IDENTIFIER_KEY = "primaryIdentifier";
	/** Metadata key storing the secondary identifier. */
	String SECONDARY_IDENTIFIER_KEY = "secondaryIdentifier";

	/** Metadata key describing the content type. */
	String CONTENT_TYPE_KEY = "contentType";
	/** Metadata value indicating logging content. */
	String CONTENT_TYPE_VALUE_LOGGING = "logging";
	/** Metadata value indicating access log content. */
	String CONTENT_TYPE_VALUE_ACCESS = "access";

	/** Metadata key describing the content format. */
	String CONTENT_FORMAT_KEY = "contentFormat";
	/** Metadata value indicating protobuf encoding. */
	String CONTENT_FORMAT_VALUE_PROTOBUF = "protobuf";

	/** Metadata key describing the compression mode. */
	String COMPRESSION_KEY = "compression";
	/** Metadata value indicating gzip compression. */
	String COMPRESSION_VALUE_GZIP = "GZIP";
}
