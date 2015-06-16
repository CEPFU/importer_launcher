package de.fu_berlin.agdb.importer_launcher.core;

import java.util.List;

import de.fu_berlin.agdb.importer.payload.LocationWeatherData;

public class NioEventPublisher implements Runnable, IEventPublisher{

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

	@Override
	public synchronized void publishEvents(List<LocationWeatherData> accumulatedWeatherData) {
		for (LocationWeatherData locationWeatherData : accumulatedWeatherData) {
			System.out.println(locationWeatherData.getDate() + " : " + locationWeatherData.getTemperature());
		}
	}
}
