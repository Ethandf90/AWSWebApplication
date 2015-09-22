package ece1779.ec2.init;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp.cpdsadapter.DriverAdapterCPDS;
import org.apache.tomcat.dbcp.dbcp.datasources.SharedPoolDataSource;

import com.amazonaws.auth.BasicAWSCredentials;

public class ec2Initialization extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public void init(ServletConfig config) {
		try{
			//Initialize connection pool
    		String accessKey = config.getInitParameter("AWSaccessKey");
    		String secretKey = config.getInitParameter("AWSsecretKey");
    		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    		ServletContext context = config.getServletContext();
    			
    		context.setAttribute("AWSCredentials", awsCredentials);
    		
    		String dbDriver = config.getInitParameter("dbDriver");
    		String dbURL = config.getInitParameter("dbURL");
    		String dbUser = config.getInitParameter("dbUser");
    		String dbPassword = config.getInitParameter("dbPassword");
    		
		    DriverAdapterCPDS ds = new DriverAdapterCPDS();
		    ds.setDriver(dbDriver);
		    ds.setUrl(dbURL);
		    
		    ds.setUser(dbUser);
		    ds.setPassword(dbPassword);
		    
		    SharedPoolDataSource dbcp = new SharedPoolDataSource();
		    dbcp.setConnectionPoolDataSource(ds);

		    config.getServletContext().setAttribute("dbpoolAWS",dbcp);
		    
//		    System.out.println("initialization");
		}
		catch (Exception ex) {
		    getServletContext().log("SQLGatewayPool Error: " + ex.getMessage());
		}
	}


}
