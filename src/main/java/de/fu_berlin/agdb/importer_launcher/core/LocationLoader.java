package de.fu_berlin.agdb.importer_launcher.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.postgis.PGgeometry;

import de.fu_berlin.agdb.importer.payload.StationMetaData;

public class LocationLoader {
	private ConnectionManager connectionManager;

	public LocationLoader(String host, String database, String user, String password) {
		connectionManager = new ConnectionManager(host, database, user, password);
	}
	
	public synchronized List<StationMetaData> getLocations() throws SQLException{
		ArrayList<StationMetaData> stations = new ArrayList<StationMetaData>();
		
		Connection connection = connectionManager.getConnection(this);
		
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM dwd_station_meta_data");
		ResultSet resultSet = preparedStatement.executeQuery();
		while(resultSet.next()){
			StationMetaData stationMetaData = new StationMetaData();
			
			stationMetaData.setStationId(resultSet.getLong("station_id"));
			stationMetaData.setStationPosition((PGgeometry) resultSet.getObject("station_position"));
			stationMetaData.setFromDate(resultSet.getDate("from_date"));
			stationMetaData.setUntilDate(resultSet.getDate("until_date"));
			stationMetaData.setStationHeight(resultSet.getInt("station_height"));
			stationMetaData.setStationName(resultSet.getString("station_name"));
			stationMetaData.setFederalState(resultSet.getString("federal_state"));
			
			stations.add(stationMetaData);
		}
		resultSet.close();
		preparedStatement.close();
		connectionManager.closeConnection(this);
		return stations;
	}
}
