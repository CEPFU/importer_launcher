package de.fu_berlin.agdb.importer_launcher.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.fu_berlin.agdb.importer.payload.LocationWeatherData;
import de.fu_berlin.agdb.nio_tools.AConnectionHandler;
import de.fu_berlin.agdb.nio_tools.NioServer;

public class NioEventPublisher extends AConnectionHandler implements IEventPublisher {
	
	private static final Logger logger = LogManager.getLogger(NioEventPublisher.class);
	
	private NioServer nioServer;

	public NioEventPublisher(int port) throws IOException {
		nioServer = new NioServer(port, this);
		Thread thread = new Thread(nioServer);
		thread.start();
	}

	@Override
	public void handleReceivedData(byte[] data) {
		logger.debug("Client tryed to send data. This will be ignored");
	}

	@Override
	public void publishEvents(List<LocationWeatherData> accumulatedWeatherData) {
		try {
			for (LocationWeatherData locationWeatherData : accumulatedWeatherData) {
				nioServer.bordcastData(locationWeatherData.asJSONObject().toString().getBytes("UTF-8"));
				logger.debug("Boardcasted following event: \n" + locationWeatherData.asJSONObject().toString());
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("Error publishing events:", e);
		}
	}
}
