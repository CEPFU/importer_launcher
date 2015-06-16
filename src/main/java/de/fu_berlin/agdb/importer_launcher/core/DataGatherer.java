package de.fu_berlin.agdb.importer_launcher.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.fu_berlin.agdb.importer.AWeatherImporter;
import de.fu_berlin.agdb.importer.payload.LocationWeatherData;
import de.fu_berlin.agdb.importer.payload.StationMetaData;

public class DataGatherer implements Runnable {
	private static final Logger logger = LogManager.getLogger(DataGatherer.class);
	
	private List<AWeatherImporter> importers;
	private LocationLoader locationLoader;

	public DataGatherer(String host, String database, String user, String password, List<AWeatherImporter> importers) {
		this.importers = importers;
		locationLoader = new LocationLoader(host, database, user, password);
	}

	@Override
	public void run() {
		try {
			runDataGatherer();
		} catch (Exception e) {
			logger.error("An Exception was caught while running the DataGatherer:", e);
		}
	}
	
	private void runDataGatherer() throws SQLException {
		while(true){
			List<StationMetaData> locations = locationLoader.getLocations();
			
			List<LocationWeatherData> accumulatedWeatherData = new ArrayList<LocationWeatherData>();
			for (AWeatherImporter aWeatherImporter : importers) {
				accumulatedWeatherData.addAll(aWeatherImporter.getWeatherDataForLocationsRespectingTimeout(locations));
			}
			publishEvents(accumulatedWeatherData);
		}
	}

	private void publishEvents(List<LocationWeatherData> accumulatedWeatherData){
		//TODO
	}
}
