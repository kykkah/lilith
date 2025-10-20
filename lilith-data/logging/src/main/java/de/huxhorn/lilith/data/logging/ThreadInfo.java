/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2016 Joern Huxhorn
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
 * Copyright 2007-2016 Joern Huxhorn
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

package de.huxhorn.lilith.data.logging;

import java.io.Serializable;

/**
 * Serializable snapshot of thread metadata captured alongside a logging event.
 */
public class ThreadInfo
	implements Serializable, Cloneable
{
	private static final long serialVersionUID = -4824666074411143263L;

	private Long id;
	private String name;
	private Long groupId;
	private String groupName;
	private Integer priority;

	/**
	 * Creates an empty thread information instance.
	 */
	public ThreadInfo()
	{
		this(null, null, null, null);
	}

	/**
	 * Creates a thread information instance with the supplied values.
	 *
	 * @param id        thread identifier
	 * @param name      thread name
	 * @param groupId   identifier of the thread group
	 * @param groupName name of the thread group
	 */
	public ThreadInfo(Long id, String name, Long groupId, String groupName)
	{
		this.id = id;
		this.name = name;
		this.groupId = groupId;
		this.groupName = groupName;
	}

	/**
	 * Returns the thread identifier.
	 *
	 * @return thread identifier or {@code null} if unknown
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets the thread identifier.
	 *
	 * @param id thread identifier to store
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Returns the thread name.
	 *
	 * @return thread name or {@code null} if not captured
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the thread name.
	 *
	 * @param name thread name to store
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Returns the identifier of the thread group.
	 *
	 * @return thread group identifier or {@code null}
	 */
	public Long getGroupId()
	{
		return groupId;
	}

	/**
	 * Sets the identifier of the thread group.
	 *
	 * @param groupId thread group identifier to store
	 */
	public void setGroupId(Long groupId)
	{
		this.groupId = groupId;
	}

	/**
	 * Returns the name of the thread group.
	 *
	 * @return thread group name or {@code null}
	 */
	public String getGroupName()
	{
		return groupName;
	}

	/**
	 * Sets the name of the thread group.
	 *
	 * @param groupName thread group name to store
	 */
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	/**
	 * Returns the thread priority.
	 *
	 * @return thread priority or {@code null} if not captured
	 */
	public Integer getPriority()
	{
		return priority;
	}

	/**
	 * Sets the thread priority.
	 *
	 * @param priority thread priority to store
	 */
	public void setPriority(Integer priority)
	{
		this.priority = priority;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ThreadInfo that = (ThreadInfo) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
		if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null) return false;
		return priority != null ? priority.equals(that.priority) : that.priority == null;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode()
	{
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
		result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
		result = 31 * result + (priority != null ? priority.hashCode() : 0);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public ThreadInfo clone()
		throws CloneNotSupportedException
	{
		return (ThreadInfo) super.clone();
	}

	/** {@inheritDoc} */
	@Override
	public String toString()
	{
		return "ThreadInfo{" +
				"id=" + id +
				", name='" + name + '\'' +
				", priority=" + priority +
				", groupId=" + groupId +
				", groupName='" + groupName + '\'' +
				'}';
	}
}
