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
import java.io.Serializable;
import java.util.List;

/**
* Coordinates event sources and dispatches new events to registered handlers.
*/
public interface SourceManager<T extends Serializable>
{
/** Registers a new event source. */
	void addSource(EventSource<T> source);

/** Removes a previously registered source. */
	void removeSource(SourceIdentifier source);

/** Returns all registered sources. */
	List<EventSource<T>> getSources();

/** Returns the number of registered sources. */
	int getNumberOfSources();

/** Adds a listener that receives source lifecycle callbacks. */
	void addEventSourceListener(EventSourceListener<T> listener);

/** Removes a listener from the source manager. */
	void removeEventSourceListener(EventSourceListener<T> listener);

/** Registers a producer that yields event sources on demand. */
	void addEventSourceProducer(EventSourceProducer<T> producer);

/** Registers an event producer that pushes events directly. */
	void addEventProducer(EventProducer<T> producer);

/** Removes the event producer associated with the given identifier. */
	void removeEventProducer(SourceIdentifier id);

/** Configures the event handlers that process incoming events. */
	void setEventHandlers(List<EventHandler<T>> handlers);

/** Returns the currently configured event handlers. */
	List<EventHandler<T>> getEventHandlers();

	//void removeEventProducer(EventProducer producer);
	/** Starts all registered producers and sources. */
	void start();
}
