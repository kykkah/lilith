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

package de.huxhorn.lilith.prefs;

import java.util.Map;

/**
 * Persistent user preferences captured by the Lilith desktop application.
 *
 * <p>The class acts as a transport container when serialising preferences to disk or exchanging them
 * between components. The numerous getters/setters correspond to the persisted flags and maps.</p>
 */
@SuppressWarnings("doclint:missing")
public class LilithPreferences
{
	private Map<String, byte[]> groovyConditions;
	private Map<String, byte[]> groovyClipboardFormatters;
	private Map<String, byte[]> detailsView;
	private Map<String, byte[]> rootFiles;
	private String blackListName;
	private String whiteListName;
	private String lookAndFeel;
	private boolean askingBeforeQuit = false;
	private boolean autoClosing = false;
	private boolean autoFocusingWindow = false;
	private boolean autoOpening = true;
	private boolean checkingForUpdate = true;
	private boolean checkingForSnapshot = false;
	private boolean cleaningLogsOnExit = false;
	private boolean coloringWholeRow = false;
	private boolean globalLoggingEnabled = true;
	private boolean hidingOnClose = true;
	private boolean maximizingInternalFrames = false;
	private boolean mute = false;
	private boolean scrollingSmoothly = true;
	private boolean scrollingToBottom = true;
	private boolean showingFullCallStack = false;
	private boolean showingFullRecentPath = false;
	private boolean showingPrimaryIdentifier = false;
	private boolean showingSecondaryIdentifier = false;
	private boolean showingStatusBar = true;
	private boolean showingStackTrace = true;
	private boolean showingTipOfTheDay = true;
	private boolean showingToolbar = true;
	private boolean trayActive = true;
	private boolean splashScreenDisabled = false;
	private boolean usingInternalFrames = true;
	private SourceFiltering sourceFiltering;
	private String defaultConditionName;
	private boolean usingWrappedExceptionStyle = false;

	/**
	 * Returns the current value of the <code>groovyConditions</code> preference.
	 *
	 * @return current value of the <code>groovyConditions</code> preference
	 */
	public Map<String, byte[]> getGroovyConditions()
	{
		return groovyConditions;
	}

	/**
	 * Updates the <code>groovyConditions</code> preference.
	 *
	 * @param groovyConditions new value for the <code>groovyConditions</code> preference
	 */
	public void setGroovyConditions(Map<String, byte[]> groovyConditions)
	{
		this.groovyConditions = groovyConditions;
	}

	/**
	 * Returns the current value of the <code>groovyClipboardFormatters</code> preference.
	 *
	 * @return current value of the <code>groovyClipboardFormatters</code> preference
	 */
	public Map<String, byte[]> getGroovyClipboardFormatters()
	{
		return groovyClipboardFormatters;
	}

	/**
	 * Updates the <code>groovyClipboardFormatters</code> preference.
	 *
	 * @param groovyClipboardFormatters new value for the <code>groovyClipboardFormatters</code> preference
	 */
	public void setGroovyClipboardFormatters(Map<String, byte[]> groovyClipboardFormatters)
	{
		this.groovyClipboardFormatters = groovyClipboardFormatters;
	}

	/**
	 * Returns the current value of the <code>detailsView</code> preference.
	 *
	 * @return current value of the <code>detailsView</code> preference
	 */
	public Map<String, byte[]> getDetailsView()
	{
		return detailsView;
	}

	/**
	 * Updates the <code>detailsView</code> preference.
	 *
	 * @param detailsView new value for the <code>detailsView</code> preference
	 */
	public void setDetailsView(Map<String, byte[]> detailsView)
	{
		this.detailsView = detailsView;
	}

	/**
	 * Returns the current value of the <code>rootFiles</code> preference.
	 *
	 * @return current value of the <code>rootFiles</code> preference
	 */
	public Map<String, byte[]> getRootFiles()
	{
		return rootFiles;
	}

	/**
	 * Updates the <code>rootFiles</code> preference.
	 *
	 * @param rootFiles new value for the <code>rootFiles</code> preference
	 */
	public void setRootFiles(Map<String, byte[]> rootFiles)
	{
		this.rootFiles = rootFiles;
	}

	/**
	 * Returns the current value of the <code>blackListName</code> preference.
	 *
	 * @return current value of the <code>blackListName</code> preference
	 */
	public String getBlackListName()
	{
		return blackListName;
	}

	/**
	 * Updates the <code>blackListName</code> preference.
	 *
	 * @param blackListName new value for the <code>blackListName</code> preference
	 */
	public void setBlackListName(String blackListName)
	{
		this.blackListName = blackListName;
	}

	/**
	 * Returns the current value of the <code>whiteListName</code> preference.
	 *
	 * @return current value of the <code>whiteListName</code> preference
	 */
	public String getWhiteListName()
	{
		return whiteListName;
	}

	/**
	 * Updates the <code>whiteListName</code> preference.
	 *
	 * @param whiteListName new value for the <code>whiteListName</code> preference
	 */
	public void setWhiteListName(String whiteListName)
	{
		this.whiteListName = whiteListName;
	}

	/**
	 * Returns the current value of the <code>lookAndFeel</code> preference.
	 *
	 * @return current value of the <code>lookAndFeel</code> preference
	 */
	public String getLookAndFeel()
	{
		return lookAndFeel;
	}

	/**
	 * Updates the <code>lookAndFeel</code> preference.
	 *
	 * @param lookAndFeel new value for the <code>lookAndFeel</code> preference
	 */
	public void setLookAndFeel(String lookAndFeel)
	{
		this.lookAndFeel = lookAndFeel;
	}

	/**
	 * Returns whether the <code>askingBeforeQuit</code> preference is enabled.
	 *
	 * @return true if the <code>askingBeforeQuit</code> preference is enabled
	 */
	public boolean isAskingBeforeQuit()
	{
		return askingBeforeQuit;
	}

	/**
	 * Updates the <code>askingBeforeQuit</code> preference.
	 *
	 * @param askingBeforeQuit new value for the <code>askingBeforeQuit</code> preference
	 */
	public void setAskingBeforeQuit(boolean askingBeforeQuit)
	{
		this.askingBeforeQuit = askingBeforeQuit;
	}

	/**
	 * Returns whether the <code>autoClosing</code> preference is enabled.
	 *
	 * @return true if the <code>autoClosing</code> preference is enabled
	 */
	public boolean isAutoClosing()
	{
		return autoClosing;
	}

	/**
	 * Updates the <code>autoClosing</code> preference.
	 *
	 * @param autoClosing new value for the <code>autoClosing</code> preference
	 */
	public void setAutoClosing(boolean autoClosing)
	{
		this.autoClosing = autoClosing;
	}

	/**
	 * Returns whether the <code>autoFocusingWindow</code> preference is enabled.
	 *
	 * @return true if the <code>autoFocusingWindow</code> preference is enabled
	 */
	public boolean isAutoFocusingWindow()
	{
		return autoFocusingWindow;
	}

	/**
	 * Updates the <code>autoFocusingWindow</code> preference.
	 *
	 * @param autoFocusingWindow new value for the <code>autoFocusingWindow</code> preference
	 */
	public void setAutoFocusingWindow(boolean autoFocusingWindow)
	{
		this.autoFocusingWindow = autoFocusingWindow;
	}

	/**
	 * Returns whether the <code>autoOpening</code> preference is enabled.
	 *
	 * @return true if the <code>autoOpening</code> preference is enabled
	 */
	public boolean isAutoOpening()
	{
		return autoOpening;
	}

	/**
	 * Updates the <code>autoOpening</code> preference.
	 *
	 * @param autoOpening new value for the <code>autoOpening</code> preference
	 */
	public void setAutoOpening(boolean autoOpening)
	{
		this.autoOpening = autoOpening;
	}

	/**
	 * Returns whether the <code>checkingForUpdate</code> preference is enabled.
	 *
	 * @return true if the <code>checkingForUpdate</code> preference is enabled
	 */
	public boolean isCheckingForUpdate()
	{
		return checkingForUpdate;
	}

	/**
	 * Updates the <code>checkingForUpdate</code> preference.
	 *
	 * @param checkingForUpdate new value for the <code>checkingForUpdate</code> preference
	 */
	public void setCheckingForUpdate(boolean checkingForUpdate)
	{
		this.checkingForUpdate = checkingForUpdate;
	}

	/**
	 * Returns whether the <code>checkingForSnapshot</code> preference is enabled.
	 *
	 * @return true if the <code>checkingForSnapshot</code> preference is enabled
	 */
	public boolean isCheckingForSnapshot()
	{
		return checkingForSnapshot;
	}

	/**
	 * Updates the <code>checkingForSnapshot</code> preference.
	 *
	 * @param checkingForSnapshot new value for the <code>checkingForSnapshot</code> preference
	 */
	public void setCheckingForSnapshot(boolean checkingForSnapshot)
	{
		this.checkingForSnapshot = checkingForSnapshot;
	}

	/**
	 * Returns whether the <code>cleaningLogsOnExit</code> preference is enabled.
	 *
	 * @return true if the <code>cleaningLogsOnExit</code> preference is enabled
	 */
	public boolean isCleaningLogsOnExit()
	{
		return cleaningLogsOnExit;
	}

	/**
	 * Updates the <code>cleaningLogsOnExit</code> preference.
	 *
	 * @param cleaningLogsOnExit new value for the <code>cleaningLogsOnExit</code> preference
	 */
	public void setCleaningLogsOnExit(boolean cleaningLogsOnExit)
	{
		this.cleaningLogsOnExit = cleaningLogsOnExit;
	}

	/**
	 * Returns whether the <code>coloringWholeRow</code> preference is enabled.
	 *
	 * @return true if the <code>coloringWholeRow</code> preference is enabled
	 */
	public boolean isColoringWholeRow()
	{
		return coloringWholeRow;
	}

	/**
	 * Updates the <code>coloringWholeRow</code> preference.
	 *
	 * @param coloringWholeRow new value for the <code>coloringWholeRow</code> preference
	 */
	public void setColoringWholeRow(boolean coloringWholeRow)
	{
		this.coloringWholeRow = coloringWholeRow;
	}

	/**
	 * Returns whether the <code>globalLoggingEnabled</code> preference is enabled.
	 *
	 * @return true if the <code>globalLoggingEnabled</code> preference is enabled
	 */
	public boolean isGlobalLoggingEnabled()
	{
		return globalLoggingEnabled;
	}

	/**
	 * Updates the <code>globalLoggingEnabled</code> preference.
	 *
	 * @param globalLoggingEnabled new value for the <code>globalLoggingEnabled</code> preference
	 */
	public void setGlobalLoggingEnabled(boolean globalLoggingEnabled)
	{
		this.globalLoggingEnabled = globalLoggingEnabled;
	}

	/**
	 * Returns whether the <code>maximizingInternalFrames</code> preference is enabled.
	 *
	 * @return true if the <code>maximizingInternalFrames</code> preference is enabled
	 */
	public boolean isMaximizingInternalFrames()
	{
		return maximizingInternalFrames;
	}

	/**
	 * Updates the <code>maximizingInternalFrames</code> preference.
	 *
	 * @param maximizingInternalFrames new value for the <code>maximizingInternalFrames</code> preference
	 */
	public void setMaximizingInternalFrames(boolean maximizingInternalFrames)
	{
		this.maximizingInternalFrames = maximizingInternalFrames;
	}

	/**
	 * Returns whether the <code>mute</code> preference is enabled.
	 *
	 * @return true if the <code>mute</code> preference is enabled
	 */
	public boolean isMute()
	{
		return mute;
	}

	/**
	 * Updates the <code>mute</code> preference.
	 *
	 * @param mute new value for the <code>mute</code> preference
	 */
	public void setMute(boolean mute)
	{
		this.mute = mute;
	}


	/**
	 * Returns whether the <code>scrollingSmoothly</code> preference is enabled.
	 *
	 * @return true if the <code>scrollingSmoothly</code> preference is enabled
	 */
	public boolean isScrollingSmoothly()
	{
		return scrollingSmoothly;
	}

	/**
	 * Updates the <code>scrollingSmoothly</code> preference.
	 *
	 * @param scrollingSmoothly new value for the <code>scrollingSmoothly</code> preference
	 */
	public void setScrollingSmoothly(boolean scrollingSmoothly)
	{
		this.scrollingSmoothly = scrollingSmoothly;
	}

	/**
	 * Returns whether the <code>scrollingToBottom</code> preference is enabled.
	 *
	 * @return true if the <code>scrollingToBottom</code> preference is enabled
	 */
	public boolean isScrollingToBottom()
	{
		return scrollingToBottom;
	}

	/**
	 * Updates the <code>scrollingToBottom</code> preference.
	 *
	 * @param scrollingToBottom new value for the <code>scrollingToBottom</code> preference
	 */
	public void setScrollingToBottom(boolean scrollingToBottom)
	{
		this.scrollingToBottom = scrollingToBottom;
	}

	/**
	 * Returns whether the <code>showingFullCallStack</code> preference is enabled.
	 *
	 * @return true if the <code>showingFullCallStack</code> preference is enabled
	 */
	public boolean isShowingFullCallStack()
	{
		return showingFullCallStack;
	}

	/**
	 * Updates the <code>showingFullCallStack</code> preference.
	 *
	 * @param showingFullCallStack new value for the <code>showingFullCallStack</code> preference
	 */
	public void setShowingFullCallStack(boolean showingFullCallStack)
	{
		this.showingFullCallStack = showingFullCallStack;
	}

	/**
	 * Returns whether the <code>showingPrimaryIdentifier</code> preference is enabled.
	 *
	 * @return true if the <code>showingPrimaryIdentifier</code> preference is enabled
	 */
	public boolean isShowingPrimaryIdentifier()
	{
		return showingPrimaryIdentifier;
	}

	/**
	 * Updates the <code>showingPrimaryIdentifier</code> preference.
	 *
	 * @param showingPrimaryIdentifier new value for the <code>showingPrimaryIdentifier</code> preference
	 */
	public void setShowingPrimaryIdentifier(boolean showingPrimaryIdentifier)
	{
		this.showingPrimaryIdentifier = showingPrimaryIdentifier;
	}

	/**
	 * Returns whether the <code>showingSecondaryIdentifier</code> preference is enabled.
	 *
	 * @return true if the <code>showingSecondaryIdentifier</code> preference is enabled
	 */
	public boolean isShowingSecondaryIdentifier()
	{
		return showingSecondaryIdentifier;
	}

	/**
	 * Updates the <code>showingSecondaryIdentifier</code> preference.
	 *
	 * @param showingSecondaryIdentifier new value for the <code>showingSecondaryIdentifier</code> preference
	 */
	public void setShowingSecondaryIdentifier(boolean showingSecondaryIdentifier)
	{
		this.showingSecondaryIdentifier = showingSecondaryIdentifier;
	}

	/**
	 * Returns whether the <code>showingStatusBar</code> preference is enabled.
	 *
	 * @return true if the <code>showingStatusBar</code> preference is enabled
	 */
	public boolean isShowingStatusBar()
	{
		return showingStatusBar;
	}

	/**
	 * Updates the <code>showingStatusBar</code> preference.
	 *
	 * @param showingStatusBar new value for the <code>showingStatusBar</code> preference
	 */
	public void setShowingStatusBar(boolean showingStatusBar)
	{
		this.showingStatusBar = showingStatusBar;
	}

	/**
	 * Returns whether the <code>showingStackTrace</code> preference is enabled.
	 *
	 * @return true if the <code>showingStackTrace</code> preference is enabled
	 */
	public boolean isShowingStackTrace()
	{
		return showingStackTrace;
	}

	/**
	 * Updates the <code>showingStackTrace</code> preference.
	 *
	 * @param showingStackTrace new value for the <code>showingStackTrace</code> preference
	 */
	public void setShowingStackTrace(boolean showingStackTrace)
	{
		this.showingStackTrace = showingStackTrace;
	}

	/**
	 * Returns whether the <code>showingTipOfTheDay</code> preference is enabled.
	 *
	 * @return true if the <code>showingTipOfTheDay</code> preference is enabled
	 */
	public boolean isShowingTipOfTheDay()
	{
		return showingTipOfTheDay;
	}

	/**
	 * Updates the <code>showingTipOfTheDay</code> preference.
	 *
	 * @param showingTipOfTheDay new value for the <code>showingTipOfTheDay</code> preference
	 */
	public void setShowingTipOfTheDay(boolean showingTipOfTheDay)
	{
		this.showingTipOfTheDay = showingTipOfTheDay;
	}

	/**
	 * Returns whether the <code>showingToolbar</code> preference is enabled.
	 *
	 * @return true if the <code>showingToolbar</code> preference is enabled
	 */
	public boolean isShowingToolbar()
	{
		return showingToolbar;
	}

	/**
	 * Updates the <code>showingToolbar</code> preference.
	 *
	 * @param showingToolbar new value for the <code>showingToolbar</code> preference
	 */
	public void setShowingToolbar(boolean showingToolbar)
	{
		this.showingToolbar = showingToolbar;
	}

	/**
	 * Returns whether the <code>splashScreenDisabled</code> preference is enabled.
	 *
	 * @return true if the <code>splashScreenDisabled</code> preference is enabled
	 */
	public boolean isSplashScreenDisabled()
	{
		return splashScreenDisabled;
	}

	/**
	 * Updates the <code>splashScreenDisabled</code> preference.
	 *
	 * @param splashScreenDisabled new value for the <code>splashScreenDisabled</code> preference
	 */
	public void setSplashScreenDisabled(boolean splashScreenDisabled)
	{
		this.splashScreenDisabled = splashScreenDisabled;
	}

	/**
	 * Returns whether the <code>usingInternalFrames</code> preference is enabled.
	 *
	 * @return true if the <code>usingInternalFrames</code> preference is enabled
	 */
	public boolean isUsingInternalFrames()
	{
		return usingInternalFrames;
	}

	/**
	 * Updates the <code>usingInternalFrames</code> preference.
	 *
	 * @param usingInternalFrames new value for the <code>usingInternalFrames</code> preference
	 */
	public void setUsingInternalFrames(boolean usingInternalFrames)
	{
		this.usingInternalFrames = usingInternalFrames;
	}

	/**
	 * Returns the current value of the <code>sourceFiltering</code> preference.
	 *
	 * @return current value of the <code>sourceFiltering</code> preference
	 */
	public SourceFiltering getSourceFiltering()
	{
		return sourceFiltering;
	}

	/**
	 * Updates the <code>sourceFiltering</code> preference.
	 *
	 * @param sourceFiltering new value for the <code>sourceFiltering</code> preference
	 */
	public void setSourceFiltering(SourceFiltering sourceFiltering)
	{
		this.sourceFiltering = sourceFiltering;
	}

	/**
	 * Returns the current value of the <code>defaultConditionName</code> preference.
	 *
	 * @return current value of the <code>defaultConditionName</code> preference
	 */
	public String getDefaultConditionName()
	{
		return defaultConditionName;
	}

	/**
	 * Updates the <code>defaultConditionName</code> preference.
	 *
	 * @param defaultConditionName new value for the <code>defaultConditionName</code> preference
	 */
	public void setDefaultConditionName(String defaultConditionName)
	{
		this.defaultConditionName = defaultConditionName;
	}

	/**
	 * Returns whether the <code>trayActive</code> preference is enabled.
	 *
	 * @return true if the <code>trayActive</code> preference is enabled
	 */
	public boolean isTrayActive()
	{
		return trayActive;
	}

	/**
	 * Updates the <code>trayActive</code> preference.
	 *
	 * @param trayActive new value for the <code>trayActive</code> preference
	 */
	public void setTrayActive(boolean trayActive)
	{
		this.trayActive = trayActive;
	}

	/**
	 * Returns whether the <code>showingFullRecentPath</code> preference is enabled.
	 *
	 * @return true if the <code>showingFullRecentPath</code> preference is enabled
	 */
	public boolean isShowingFullRecentPath()
	{
		return showingFullRecentPath;
	}

	/**
	 * Updates the <code>showingFullRecentPath</code> preference.
	 *
	 * @param showingFullRecentPath new value for the <code>showingFullRecentPath</code> preference
	 */
	public void setShowingFullRecentPath(boolean showingFullRecentPath)
	{
		this.showingFullRecentPath = showingFullRecentPath;
	}

	/**
	 * Returns whether the <code>hidingOnClose</code> preference is enabled.
	 *
	 * @return true if the <code>hidingOnClose</code> preference is enabled
	 */
	public boolean isHidingOnClose()
	{
		return hidingOnClose;
	}

	/**
	 * Updates the <code>hidingOnClose</code> preference.
	 *
	 * @param hidingOnClose new value for the <code>hidingOnClose</code> preference
	 */
	public void setHidingOnClose(boolean hidingOnClose)
	{
		this.hidingOnClose = hidingOnClose;
	}

	/**
	 * Returns whether the <code>usingWrappedExceptionStyle</code> preference is enabled.
	 *
	 * @return true if the <code>usingWrappedExceptionStyle</code> preference is enabled
	 */
	public boolean isUsingWrappedExceptionStyle()
	{
		return usingWrappedExceptionStyle;
	}

	/**
	 * Updates the <code>usingWrappedExceptionStyle</code> preference.
	 *
	 * @param usingWrappedExceptionStyle new value for the <code>usingWrappedExceptionStyle</code> preference
	 */
	public void setUsingWrappedExceptionStyle(boolean usingWrappedExceptionStyle)
	{
		this.usingWrappedExceptionStyle = usingWrappedExceptionStyle;
	}

	public enum SourceFiltering
	{
		NONE, BLACKLIST, WHITELIST
	}
}
