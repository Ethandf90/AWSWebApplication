package ece1779.ec2.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.tomcat.dbcp.dbcp.cpdsadapter.DriverAdapterCPDS;
import org.apache.tomcat.dbcp.dbcp.datasources.SharedPoolDataSource;

public class ConnectionPool {

	String driverClassName = "com.mysql.jdbc.Driver";
	String connectionUrl = "jdbc:mysql://ece1779winter2015db.cf2zhhwzx2tf.us-east-1.rds.amazonaws.com/ece1779group10";
	String dbUser = "group10";
	String dbPwd = "2192143945";
	
	SharedPoolDataSource dbcp;
	
	private static ConnectionPool connectionpool = null;
	
	private ConnectionPool() {
        try {

        	DriverAdapterCPDS ds = new DriverAdapterCPDS();
 		    ds.setDriver(driverClassName);
 		    ds.setUrl(connectionUrl);
 		    ds.setUser(dbUser);
 		    ds.setPassword(dbPwd);

 		    dbcp = new SharedPoolDataSource();
 		    dbcp.setConnectionPoolDataSource(ds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	
	public Connection getConnection() throws SQLException {
        return dbcp.getConnection();
    }
	
	public static ConnectionPool getInstance() {
        if (connectionpool == null) {
        	connectionpool = new ConnectionPool();
        }
        return connectionpool;
    }
	
}
