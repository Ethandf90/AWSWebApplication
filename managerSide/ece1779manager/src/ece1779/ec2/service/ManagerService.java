package ece1779.ec2.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.cloudwatch.model.ListMetricsRequest;
import com.amazonaws.services.cloudwatch.model.ListMetricsResult;
import com.amazonaws.services.cloudwatch.model.Metric;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;
import com.amazonaws.services.elasticloadbalancing.model.DeregisterInstancesFromLoadBalancerRequest;
import com.amazonaws.services.elasticloadbalancing.model.RegisterInstancesWithLoadBalancerRequest;

import ece1779.ec2.mode.ManagerMode;
import ece1779.ec2.mode.WorkerMode;

public class ManagerService {
	//the image of
	public static final String imageID = "ami-c4dbffac";
	public static final String keyPair = "ece1779_Ethan";
	public static final String LoadBalancerName = "LoadBalancer1";
	public static final String managerID = "i-2386d5d9";
	
	public Map<String, Double> getCPUUsageMap(){
		Map<String, Double> cpuUsageMap = new HashMap<String, Double>();
		//AWSCredentials-->a service class
		BasicAWSCredentials awsCredentials = AWSCredentials.getAWSManager().getAWSCredentials();
		AmazonCloudWatch cw = new AmazonCloudWatchClient(awsCredentials);
		
		try{
			ListMetricsRequest listMetricsRequest = new ListMetricsRequest();
        	listMetricsRequest.setMetricName("CPUUtilization");
        	listMetricsRequest.setNamespace("AWS/EC2");
        	ListMetricsResult result = cw.listMetrics(listMetricsRequest);
        	java.util.List<Metric> 	metrics = result.getMetrics();
        	for (Metric metric : metrics) {
        		String namespace = metric.getNamespace();
        		String metricName = metric.getMetricName();
        		List<Dimension> dimensions = metric.getDimensions();
            	GetMetricStatisticsRequest statisticsRequest = new GetMetricStatisticsRequest();
            	statisticsRequest.setNamespace(namespace);
            	statisticsRequest.setMetricName(metricName);
            	statisticsRequest.setDimensions(dimensions);
            	Date endTime = new Date();
            	Date startTime = new Date();
            	startTime.setTime(endTime.getTime()-600000);
            	statisticsRequest.setStartTime(startTime);
            	statisticsRequest.setEndTime(endTime);
            	statisticsRequest.setPeriod(60);
            	Vector<String>statistics = new Vector<String>();
            	statistics.add("Maximum");
            	statisticsRequest.setStatistics(statistics);
            	GetMetricStatisticsResult stats = cw.getMetricStatistics(statisticsRequest);
            	
//            	System.out.print("Namespace = " + namespace + " Metric = " + metricName + " Dimensions = " + dimensions);
//            	System.out.print("Values = " + stats.toString());
            	
            	if(stats.getDatapoints().size()>0 && metric.getDimensions()!=null 
            			&& metric.getDimensions().size()>0 && metric.getDimensions().get(0)!=null 
            			&& stats.getDatapoints().get(0)!=null) {
            		cpuUsageMap.put(metric.getDimensions().get(0).getValue(), stats.getDatapoints().get(0).getMaximum());
            	}
        	}
		}
		catch (AmazonServiceException as) {
            System.out.println("Error AmazonServiceException:    " + as.getMessage());

        } catch (AmazonClientException ac) {
            System.out.println("Error AmazonClientException: " + ac.getMessage());
        }
		return cpuUsageMap;
	}

	public List<WorkerMode> getWorkersInfo(){
		List<WorkerMode> workers = new ArrayList<WorkerMode>();
		BasicAWSCredentials awsCredentials = AWSCredentials.getAWSManager().getAWSCredentials();
		AmazonEC2 ec2 = new AmazonEC2Client(awsCredentials);
		for(Reservation res : ec2.describeInstances().getReservations()){
			for(Instance instance : res.getInstances()){
				String id = instance.getInstanceId();
				String state = instance.getState().getName();
				workers.add(new WorkerMode(id,state));
//				System.out.println("get worker state: " + state);
			}
		}
		return workers;
	}

	public List<WorkerMode> getRunningWorkers(){
		Map<String, Double> cpuUsageMap = getCPUUsageMap();
		List<WorkerMode> workers = getWorkersInfo();
		List<WorkerMode> runningWorkers = new ArrayList<WorkerMode>();
		//
		for(int i=0; i<workers.size();i++){
			if(workers.get(i).getStatus().equals("running")&& !(workers.get(i).getId().equals(managerID))){
				runningWorkers.add(workers.get(i));
			}
		}
		
		for(int i=0; i<runningWorkers.size(); i++){
			String id = runningWorkers.get(i).getId();
			if(cpuUsageMap.containsKey(id)){
				runningWorkers.get(i).setCpuUsage(cpuUsageMap.get(id));
			}
			else{
				runningWorkers.get(i).setCpuUsage(0);
			}
		}
		return runningWorkers;
	}

	public double averageCPU(){
		Map<String, Double> cpuUsageMap = getCPUUsageMap();
		//the manager instance is not in the runningWorkers List
		int workerNum = getRunningWorkers().size();
		
		double cpuUsage = 0;       		 
  		double total = 0;
  		Set<String> keys = cpuUsageMap.keySet();
  		for(Iterator<String> i = keys.iterator(); i.hasNext();) {
  			 double value = (Double)cpuUsageMap.get((String)i.next());
  			 total = total + value;
  		 }
		
  		if(workerNum != 0){
  			cpuUsage = total/workerNum;
  		}
		return cpuUsage;
	}
	
	public void adjustLoadBalancer(List<String> ids, boolean add){
		BasicAWSCredentials awsCredentials = AWSCredentials.getAWSManager().getAWSCredentials();
		AmazonElasticLoadBalancingClient lb = new AmazonElasticLoadBalancingClient(awsCredentials);
		List<com.amazonaws.services.elasticloadbalancing.model.Instance> instances = new ArrayList<com.amazonaws.services.elasticloadbalancing.model.Instance>();
		if(ids != null){
			for(int i=0; i<ids.size();i++){
				instances.add(new com.amazonaws.services.elasticloadbalancing.model.Instance(ids.get(i)));
			}
			
			if(add){
				RegisterInstancesWithLoadBalancerRequest register = new RegisterInstancesWithLoadBalancerRequest();
				register.setLoadBalancerName(LoadBalancerName);
				register.setInstances(instances);
				lb.registerInstancesWithLoadBalancer(register);
			}
			else{
				DeregisterInstancesFromLoadBalancerRequest deregister = new DeregisterInstancesFromLoadBalancerRequest();
				deregister.setLoadBalancerName(LoadBalancerName);
				deregister.setInstances(instances);
				lb.deregisterInstancesFromLoadBalancer(deregister);
			}
		}
		
	}
	
	public void createInstances(int number){
		BasicAWSCredentials awsCredentials = AWSCredentials.getAWSManager().getAWSCredentials();
		AmazonEC2 ec2 = new AmazonEC2Client(awsCredentials);
		try{
			RunInstancesRequest request = new RunInstancesRequest(imageID, number, number);
			//securityGroups  connect to .pem
			List<String> securityGroups = new ArrayList<String>();
			securityGroups.add("sg-0964dd6d");
			request.setSecurityGroupIds(securityGroups);
			
			request.setKeyName(keyPair);
			request.setMonitoring(true);
			
			RunInstancesResult result = ec2.runInstances(request);
			List<Instance> instances = result.getReservation().getInstances();
			
			List<String> ids = new ArrayList<String>();
			for(int i=0; i<instances.size(); i++) {
				ids.add(instances.get(i).getInstanceId());
			}
			
			adjustLoadBalancer(ids,true);
			
		}
		catch (AmazonServiceException as) {
			System.out.println("Error Message: " + as.getMessage());
        } 
        catch (AmazonClientException ac) {
        	System.out.println("Error Message: " + ac.getMessage());
        }
	}
	
	private void startInstances(List<String> ids){
		BasicAWSCredentials awsCredentials = AWSCredentials.getAWSManager().getAWSCredentials();
		AmazonEC2 ec2 = new AmazonEC2Client(awsCredentials);
		try{
			StartInstancesRequest request = new StartInstancesRequest();
			request.setInstanceIds(ids);
			adjustLoadBalancer(ids, true);
			ec2.startInstances(request);
		}
		catch(AmazonClientException ace) {
            System.out.println("startInstances Error: " + ace.getMessage());
		}
	}
	
	public void increaseWorkers(int number){
		List<WorkerMode> workers = getWorkersInfo();
		List<WorkerMode> stoppedWorkers = new ArrayList<WorkerMode>();
		
		System.out.println("worker size" + workers.size());
		
		for(int i=0; i<workers.size();i++){
			if(!(workers.get(i).getStatus().equals("running"))&& !(workers.get(i).getStatus().equals("terminated"))){
				stoppedWorkers.add(workers.get(i));
				System.out.println("stopped id:" + workers.get(i).getId());
			}
		}
		
		List<String> ids = new ArrayList<String>();
		if(stoppedWorkers.size() >= number){
			for(int i=0; i<number; i++){
				ids.add(stoppedWorkers.get(i).getId());
			}
		}
		else{
			for(int i=0; i<stoppedWorkers.size(); i++){
				ids.add(stoppedWorkers.get(i).getId());
			}
			createInstances(number - stoppedWorkers.size());
		}
		startInstances(ids);
	}
	
	public void stopInstances(List<String> ids){
		BasicAWSCredentials awsCredentials = AWSCredentials.getAWSManager().getAWSCredentials();
		AmazonEC2 ec2 = new AmazonEC2Client(awsCredentials);
	
		try{
			StopInstancesRequest request = new StopInstancesRequest();
			request.setInstanceIds(ids);
			adjustLoadBalancer(ids,false);
			ec2.stopInstances(request);
		}
		catch(AmazonClientException ac) {
            System.out.println("Error Message: " + ac.getMessage());
		}
	}
	
	public void reduceWorkers(int number){
		List<WorkerMode> runningWorkers = getRunningWorkers();
		List<String> instances = new ArrayList<String>();
		for(int i=0; i< number; i++){
			instances.add(runningWorkers.get(runningWorkers.size()-1-i).getId());
		}
		stopInstances(instances);
	}
	
	//////////////////////Manager configuration
	Connection con = null;
	PreparedStatement ptmt = null;
	PreparedStatement ptmt2 = null;
	ResultSet rs = null;
	
	public void updateAutoScalingParameter(ManagerMode managermode){
		try{
			con = ConnectionPool.getInstance().getConnection();
            String sql = "UPDATE users SET password=? WHERE login=?";
            ptmt = con.prepareStatement(sql);
            
            ptmt.setString(1, Double.toString(managermode.getThresholdGrow()));
            ptmt.setString(2, "thresholdGrow");
            ptmt.executeUpdate();
            
            ptmt.setString(1, Double.toString(managermode.getThresholdShrink()));
            ptmt.setString(2, "thresholdShrink");
            ptmt.executeUpdate();
            
            ptmt.setString(1, Integer.toString(managermode.getRatioExpand()));
            ptmt.setString(2, "ratioExpand");
            ptmt.executeUpdate();
            
            ptmt.setString(1, Integer.toString(managermode.getRatioShrink()));
            ptmt.setString(2, "ratioShrink");
            ptmt.executeUpdate();
            
            ptmt.setString(1, managermode.getAutoScaling());
            ptmt.setString(2, "autoScaling");
            ptmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try{
				con.close();
				ptmt.close();
//				rs.close();
			}
			catch (SQLException e) {
			     e.printStackTrace();
			} catch (Exception e) {
			     e.printStackTrace();
			}
		}
	}

	public ManagerMode getAutoScalingParameter(){
		 double thresholdGrow = 50.0;
		 double thresholdShrink = 10.0;
		 int ratioExpand = 1;
		 int ratioShrink = 1;
		 String autoScaling = "NO";
		try{
			String queryString = "SELECT password FROM users WHERE login = ?";
			String insertString = "insert into users (login, password) values (?,?)";
			con = ConnectionPool.getInstance().getConnection();
			ptmt = con.prepareStatement(queryString);
			ptmt2 = con.prepareStatement(insertString);
			
			ptmt.setString(1, "thresholdGrow");
			rs = ptmt.executeQuery();
			if(rs.next()){
				thresholdGrow = Double.parseDouble(rs.getString(1)); 
			}
			else{
				thresholdGrow = 50.0;
				ptmt2.setString(1, "thresholdGrow");
				ptmt2.setString(2, "50.0");
				ptmt2.executeUpdate();
			}
			
			ptmt.setString(1, "thresholdShrink");
			rs = ptmt.executeQuery();
			if(rs.next()){
				thresholdShrink = Double.parseDouble(rs.getString(1));
			}
			else{
				thresholdShrink = 10.0;
				ptmt2.setString(1, "thresholdShrink");
				ptmt2.setString(2, "20.0");
	            ptmt2.executeUpdate();
			}
			
			ptmt.setString(1, "ratioExpand");
			rs = ptmt.executeQuery();
			if(rs.next()){
				ratioExpand = Integer.parseInt(rs.getString(1));
			}
			else{
				ratioExpand = 1;
				ptmt2.setString(1, "ratioExpand");
				ptmt2.setString(2, "1");
	            ptmt2.executeUpdate();
			}
			
			ptmt.setString(1, "ratioShrink");
			rs = ptmt.executeQuery();
			if(rs.next()){
				ratioShrink = Integer.parseInt(rs.getString(1));
			}
			else{
				ratioShrink = 1;
				ptmt2.setString(1, "ratioShrink");
				ptmt2.setString(2, "1");
	            ptmt2.executeUpdate();
			}
			
			ptmt.setString(1, "autoScaling");
			rs = ptmt.executeQuery();
			if(rs.next()){
				autoScaling = rs.getString(1);
			}
			else{
				autoScaling = "NO";
				ptmt2.setString(1, "autoScaling");
				ptmt2.setString(2, "NO");
	            ptmt2.executeUpdate();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try{
				con.close();
				ptmt.close();
				rs.close();
			}
			catch (SQLException e) {
			     e.printStackTrace();
			} catch (Exception e) {
			     e.printStackTrace();
			}
		}
		ManagerMode managermode = new ManagerMode(thresholdGrow,thresholdShrink,ratioExpand,ratioShrink,autoScaling);
		return managermode;
		
	}
	
	public static String getImageid() {
		return imageID;
	}

	
	public static String getKeypair() {
		return keyPair;
	}
}
