package de.fu_berlin.agdb.importer_launcher.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import org.postgis.PGgeometry;
public class ConnectionManager {
	private String host;
	private String database;
	private String user;
	private String password;

	private HashMap<Object, Connection> connections;
	
	public ConnectionManager(String host, String database, String user, String password) {
		this.host = host;
		this.database = database;
		this.user = user;
		this.password = password;
		
		connections = new HashMap<Object, Connection>();
	}
	
	private Properties getDatabaseProperties(){
		Properties properties = new Properties();
		properties.setProperty("user", user);
		properties.setProperty("password", password);
		return properties;
	}
	

	public Connection getConnection(Object o) throws SQLException {
		if(!connections.containsKey(o)){
			Connection connection = DriverManager.getConnection("jdbc:postgresql://" + host + "/" + database, getDatabaseProperties());
			((org.postgresql.PGConnection)connection).addDataType("geometry",PGgeometry.class);
			connections.put(o, connection);
		}
		return connections.get(o);
	}
	
	public void closeConnection(Object o){
		if(connections.containsKey(o)){
			Connection connection = connections.get(o);
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				connections.remove(o);
			}
		}
	}
}
