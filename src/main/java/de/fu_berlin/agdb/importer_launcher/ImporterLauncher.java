package de.fu_berlin.agdb.importer_launcher;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.fu_berlin.agdb.importer.AWeatherImporter;
import de.fu_berlin.agdb.importer.tools.LocationLoader;
import de.fu_berlin.agdb.importer_launcher.core.IEventPublisher;
import de.fu_berlin.agdb.importer_launcher.core.NioEventPublisher;
import de.fu_berlin.agdb.importer_launcher.core.WeatherImporterRunner;
import de.fu_berlin.agdb.yahoo_importer.YahooImporter;

public class ImporterLauncher {
	
	private static Properties properties;

    public static void main( String[] args ) throws IOException {
    	loadProperties();
    	
    	LocationLoader locationLoader = new LocationLoader(properties.getProperty("database_host") 
    			+ ":" 
    			+ properties.getProperty("database_port"), 
    			properties.getProperty("database"), 
    			properties.getProperty("database_user"), 
    			properties.getProperty("database_password"));
    	IEventPublisher initializeNewEventPublisher =  new NioEventPublisher(Integer.valueOf(properties.getProperty("application_port")));
    	
    	for (AWeatherImporter weatherImporter : getImporters()) {
			WeatherImporterRunner weatherImporterRunner = new WeatherImporterRunner(weatherImporter, locationLoader, initializeNewEventPublisher);
    		
			Thread runnerThread = new Thread(weatherImporterRunner);
			runnerThread.start();
		}
    }

    
    public static void loadProperties() throws IOException {
    	properties = new Properties();
    	FileReader reader = new FileReader(new File("importer-launcher.properties"));
		properties.load(reader);
		reader.close();
	}
    
    private static List<AWeatherImporter> getImporters(){
    	List<AWeatherImporter> importers = new ArrayList<AWeatherImporter>();
    	
    	//add all importers we want to use
    	importers.add(new YahooImporter());
//    	importers.add(new DWDImporter());
    	return importers;
    }
}
