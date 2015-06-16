package de.fu_berlin.agdb.importer_launcher.core;

import java.util.List;

import de.fu_berlin.agdb.importer.payload.LocationWeatherData;

public interface IEventPublisher {
	public void publishEvents(List<LocationWeatherData> accumulatedWeatherData);
}
