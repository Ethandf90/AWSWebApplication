package ece1779.ec2.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import ece1779.ec2.service.ConnectionPool;

public class Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		///////////////delete S3
		try{
			String bucketName = "ece1779bucket";
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
//			out.write("<html><head><title>Delete</title></head>");
//			out.write("<body>");
		        
			BasicAWSCredentials awsCredentials = (BasicAWSCredentials)this.getServletContext().getAttribute("AWSCredentials");
			AmazonS3 s3 = new AmazonS3Client(awsCredentials);
			int i = 0;
	        
			try{
				ObjectListing objects = s3.listObjects(bucketName);
	
	       		List<S3ObjectSummary> summaries = objects.getObjectSummaries();
	       		System.out.println("The list size is:" + summaries.size());
	    		for (S3ObjectSummary item : summaries) {
	    			String key = item.getKey();
	    			s3.deleteObject(bucketName, key);
	    			i++;
//	    			out.println("<p>Deleted " + key +"</p>");
//	        		out.flush();
	    		}
	    		System.out.println("No. of objects deleted:" + i);
	    		while (objects.isTruncated()) {
	        		objects = s3.listNextBatchOfObjects(objects);
	        		summaries = objects.getObjectSummaries();
	        		for (S3ObjectSummary item : summaries) {
	        			String key = item.getKey();
	        			s3.deleteObject(bucketName, key);
//	        			out.println("<p>Deleted " + key +"</p>");
//	            		out.flush();
	        		}
	        	}
	    		
//	    		s3.deleteBucket(bucketName);
//	        	out.println("<p>Deleted Bucket " + bucketName + "</p>");
	    		
		    }
		    catch (AmazonServiceException ase) {
		    	out.println("Caught an AmazonServiceException, which means your request made it "
		                    + "to Amazon S3, but was rejected with an error response for some reason.");
		    	out.println("Error Message:    " + ase.getMessage());
		    	out.println("HTTP Status Code: " + ase.getStatusCode());
		    	out.println("AWS Error Code:   " + ase.getErrorCode());
		    	out.println("Error Type:       " + ase.getErrorType());
		    	out.println("Request ID:       " + ase.getRequestId());
		    } catch (AmazonClientException ace) {
		    	out.println("Caught an AmazonClientException, which means the client encountered "
		                    + "a serious internal problem while trying to communicate with S3, "
		                    + "such as not being able to access the network.");
		    	out.println("Error Message: " + ace.getMessage());
		    }
//			out.write("</body></html>");
		}
		catch (Exception ex) {
			throw new ServletException (ex);
		}
		/////////////////////////////delete S3 
		
		///////////////////////////delete DB
		Connection con = null;
		
		try{
			con = ConnectionPool.getInstance().getConnection();
			Statement sqlState = con.createStatement();
			con.setAutoCommit(false);
            String sql = "Delete from users";
            sqlState.executeUpdate(sql);
            
            sql = "Delete from images";
            sqlState.executeUpdate(sql);
            con.commit();
            con.setAutoCommit(true);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try{
				con.close();
			}
			catch (SQLException e) {
			     e.printStackTrace();
			} catch (Exception e) {
			     e.printStackTrace();
			}
		}
		///////////////////////
		response.sendRedirect("Manager.jsp");
			
	}

}
