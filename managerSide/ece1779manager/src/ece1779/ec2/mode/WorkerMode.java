package ece1779.ec2.mode;

public class WorkerMode {

	private String id;
	private double cpuUsage;
	private String status;
	
	public WorkerMode(){}
	
	public WorkerMode(String id, String status){
		this.id = id;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
