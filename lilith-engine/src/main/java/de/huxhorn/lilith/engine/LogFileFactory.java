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
package de.huxhorn.lilith.engine;

import de.huxhorn.lilith.data.eventsource.SourceIdentifier;
import java.io.File;

/**
* Provides filesystem locations for Lilith log files and metadata.
*/
public interface LogFileFactory
{
/** Returns the base directory managed by this factory. */
	File getBaseDir();

/** Returns the index file for the given source. */
	File getIndexFile(SourceIdentifier sourceIdentifier);

/** Returns the main data file for the given source. */
	File getDataFile(SourceIdentifier sourceIdentifier);

/** Returns the temporary active file for the given source. */
	File getActiveFile(SourceIdentifier sourceIdentifier);

/** Returns the extension used for data files. */
	String getDataFileExtension();

/** Returns the size on disk for the given source. */
	long getSizeOnDisk(SourceIdentifier sourceIdentifier);

/** Returns the number of events stored for the given source. */
	long getNumberOfEvents(SourceIdentifier sourceIdentifier);
}
