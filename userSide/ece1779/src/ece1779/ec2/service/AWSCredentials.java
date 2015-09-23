package ece1779.ec2.service;

import com.amazonaws.auth.BasicAWSCredentials;

public class AWSCredentials {

	String accessKey = "AKIAJTSU5KGNMMTVUJHA";
	String secretKey = "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";
	BasicAWSCredentials awsCredentials;
	private static AWSCredentials awsAccessManager;
	
	private AWSCredentials(){
		awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
	}
	public static AWSCredentials getAWSManager(){
		if(awsAccessManager==null){
			awsAccessManager = new AWSCredentials();
		}
		return awsAccessManager;
	}
	public BasicAWSCredentials getAWSCredentials(){
		return awsCredentials;
	}
}
