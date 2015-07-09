package de.fu_berlin.agdb.importer_launcher.core;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.fu_berlin.agdb.importer.AWeatherImporter;
import de.fu_berlin.agdb.importer.payload.LocationWeatherData;
import de.fu_berlin.agdb.importer.tools.LocationLoader;

public class WeatherImporterRunner implements Runnable {

	private static final Logger logger = LogManager.getLogger(WeatherImporterRunner.class);
	
	private static final long DEFAULT_TIMEOUT = 5*1000;
	
	private AWeatherImporter importer;
	private LocationLoader locationLoader;
	private IEventPublisher publisher;

	public WeatherImporterRunner(AWeatherImporter importer, LocationLoader locationLoader, IEventPublisher eventPublisher) {
		this.importer = importer;
		this.locationLoader = locationLoader;
		this.publisher = eventPublisher;
	}

	@Override
	public void run() {
		while (true) {
			try {
				List<LocationWeatherData> weatherDataForLocationsRespectingTimeout = importer.getWeatherDataForLocationsRespectingTimeout(locationLoader.getLocations());
				if(weatherDataForLocationsRespectingTimeout != null){
					publisher.publishEvents(weatherDataForLocationsRespectingTimeout);
				}
				Thread.sleep(DEFAULT_TIMEOUT);
			} catch (SQLException e) {
				logger.error("Error while running Importer:", e);
			} catch (InterruptedException e) {
				logger.error("Runner interrupted while sleeping:", e);
			}
		}		
	}

}
