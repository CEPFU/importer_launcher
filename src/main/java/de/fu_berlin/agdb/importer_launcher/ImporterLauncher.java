package de.fu_berlin.agdb.importer_launcher;

import java.util.ArrayList;
import java.util.List;

import de.fu_berlin.agdb.importer.AWeatherImporter;
import de.fu_berlin.agdb.importer_launcher.core.DataGatherer;
import de.fu_berlin.agdb.yahoo_importer.YahooImporter;

public class ImporterLauncher {
    public static void main( String[] args ) {
    	List<AWeatherImporter> importers = new ArrayList<AWeatherImporter>();
    	
    	//add all importers we want to use
    	importers.add(new YahooImporter());
    	
    	DataGatherer dataGatherer = new DataGatherer("localhost:5432", "ems", "ems", "ems", importers);
    	Thread dataGathererThread = new Thread(dataGatherer);
    	dataGathererThread.start();
    }
}
