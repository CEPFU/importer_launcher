package de.fu_berlin.agdb.importer_launcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.fu_berlin.agdb.dwd_importer.DWDImporter;
import de.fu_berlin.agdb.importer.AWeatherImporter;
import de.fu_berlin.agdb.importer.tools.LocationLoader;
import de.fu_berlin.agdb.importer_launcher.core.IEventPublisher;
import de.fu_berlin.agdb.importer_launcher.core.NioEventPublisher;
import de.fu_berlin.agdb.importer_launcher.core.WeatherImporterRunner;
import de.fu_berlin.agdb.yahoo_importer.YahooImporter;

public class ImporterLauncher {
	
	private static final String DATABASE_HOST = "10.10.10.105";
	private static final String DATABASE_PORT = "5432";
	private static final String DATABASE = "ems";
	private static final String USER = "ems";
	private static final String PASSWORD = "ems";
	
	private static final int APPLICATION_PORT = 9977;
	
    public static void main( String[] args ) throws IOException {
    	
    	LocationLoader locationLoader = new LocationLoader(DATABASE_HOST + ":" + DATABASE_PORT, DATABASE, USER, PASSWORD);
    	IEventPublisher initializeNewEventPublisher =  new NioEventPublisher(APPLICATION_PORT);
    	
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
    	importers.add(new DWDImporter());
    	return importers;
    }
}
