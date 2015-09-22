package ece1779.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import ece1779.ec2.mode.imageMode;
import ece1779.ec2.service.ImageService;


public class FileUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Create a factory for disk-based file items
    	FileItemFactory factory = new DiskFileItemFactory();
    	
    	// Create a new file upload handler
    	ServletFileUpload upload = new ServletFileUpload(factory);
    	
    	// Parse the request
    	List items = null;
    	try {
			items = upload.parseRequest(new ServletRequestContext(request));
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
    	
    	// User ID
    	FileItem userID = (FileItem)items.get(0);
    	
    	//the name of the param
//    	String name = userID.getFieldName();
    	//the value of the param
    	String userId = userID.getString();
    	
    	//Uploaded File
        FileItem theFile = (FileItem)items.get(1);
        
     // filename of the image
//      String fileName = theFile.getName();
      
   // get root directory of web application
        String path = this.getServletContext().getRealPath("/");
      
        String key1 = "Key1_" + UUID.randomUUID();
        String key2 = "Key2_" + UUID.randomUUID();
        String key3 = "Key3_" + UUID.randomUUID();
        String key4 = "Key4_" + UUID.randomUUID();
      
        String path1 = path + key1;
        String path2 = path + key2;
        String path3 = path + key3;
        String path4 = path + key4;
        
      //store file in server
        File file1 = new File(path1);
        try {
			theFile.write(file1);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        ConvertCmd cmd = new ConvertCmd();
//        String cmdPath = "/Users/newuser/Downloads/ImageMagick-6/bin";
//        cmd.setSearchPath(cmdPath);
        ///2
        IMOperation op = new IMOperation();
        op.addImage();
        op.flip();
        op.addImage();
         
        try {
			cmd.run(op, path1,path2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IM4JavaException e) {
			e.printStackTrace();
		}
        File file2 = new File(path2);
      /// 3
        op = new IMOperation();
        op.addImage();
        op.blueShift(10.7);
        op.addImage();
        
        try {
			cmd.run(op, path1, path3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IM4JavaException e) {
			e.printStackTrace();
		}
        File file3 = new File(path3);
        ///4
        op = new IMOperation();
        op.addImage();
        op.rotate(90.0);
        op.addImage();
        
        try {
			cmd.run(op, path1, path4);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IM4JavaException e) {
			e.printStackTrace();
		}
        File file4 = new File(path4);
      
        s3SaveFile(file1, key1);
        file1.delete();
        s3SaveFile(file2, key2);
        file2.delete();
        s3SaveFile(file3, key3);
        file3.delete();
        s3SaveFile(file4, key4);
        file4.delete();
        
        ImageService imageservice = new ImageService();
        imageMode image = new imageMode(Integer.parseInt(userId),key1,key2,key3,key4);
        imageservice.addImageToSQL(image);
        
        theFile.delete();
//        System.out.println("here");
        response.sendRedirect("fileupload.jsp");

//      RequestDispatcher dispatcher = request.getRequestDispatcher("fileupload.jsp");
//		dispatcher.forward(request, response);
        
//      PrintWriter out = response.getWriter();
//      response.setContentType("text/html");
//      
//      out.println("<html><head><title>Sample Image Upload</title></head>");
//      out.println("<body>");
//      
//      out.println("bbbbbbbbb");
//      
//      out.println("</body></html>");
	}
	
	private void s3SaveFile(File file, String key) throws IOException{
		BasicAWSCredentials awsCredentials = (BasicAWSCredentials)this.getServletContext().getAttribute("AWSCredentials");

    	AmazonS3 s3 = new AmazonS3Client(awsCredentials);
        
        String bucketName = "ece1779bucket";
        
        try {
            s3.putObject(new PutObjectRequest(bucketName, key, file));
            
            s3.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);

        } 
        catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } 
        catch (AmazonClientException ace) {
        	System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
        	System.out.println("Error Message: " + ace.getMessage());
        }
	}

}
