package de.fu_berlin.agdb.importer_launcher;

import java.util.ArrayList;
import java.util.List;

import de.fu_berlin.agdb.importer.AWeatherImporter;
import de.fu_berlin.agdb.importer_launcher.core.NioEventPublisher;
import de.fu_berlin.agdb.importer_launcher.core.IEventPublisher;
import de.fu_berlin.agdb.importer_launcher.core.LocationLoader;
import de.fu_berlin.agdb.importer_launcher.core.WeatherImporterRunner;
import de.fu_berlin.agdb.yahoo_importer.YahooImporter;

public class ImporterLauncher {
    public static void main( String[] args ) {
    	
    	
    	LocationLoader locationLoader = new LocationLoader("10.10.10.104:5432", "ems", "ems", "ems");
    	IEventPublisher initializeNewEventPublisher = initializeNewEventPublisher();
    	
    	for (AWeatherImporter weatherImporter : getImporters()) {
			WeatherImporterRunner weatherImporterRunner = new WeatherImporterRunner(weatherImporter, locationLoader, initializeNewEventPublisher);
    		
			Thread runnerThread = new Thread(weatherImporterRunner);
			runnerThread.start();
		}
    }
    
    private static List<AWeatherImporter> getImporters(){
    	List<AWeatherImporter> importers = new ArrayList<AWeatherImporter>();
    	
    	//add all importers we want to use
    	importers.add(new YahooImporter());
    	return importers;
    }
    
    private static IEventPublisher initializeNewEventPublisher(){
    	NioEventPublisher eventPublisher = new NioEventPublisher();
    	Thread publisherThread = new Thread(eventPublisher);
    	publisherThread.start();
    	return eventPublisher;
    }
}
