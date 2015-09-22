package ece1779.ec2.mode;

public class ManagerMode {

	private double thresholdGrow;
	private double thresholdShrink;
	private int ratioExpand;
	private int ratioShrink;
	private String autoScaling;
	
	public ManagerMode(double thresholdGrow, double thresholdShrink, int ratioExpand, int ratioShrink, String autoScaling) {
		this.thresholdGrow = thresholdGrow;
		this.thresholdShrink = thresholdShrink;
		this.ratioExpand = ratioExpand;
		this.ratioShrink = ratioShrink;
		this.autoScaling = autoScaling;
	}

	public ManagerMode() {
		// TODO Auto-generated constructor stub
	}

	public double getThresholdGrow() {
		return thresholdGrow;
	}

	public void setThresholdGrow(double thresholdGrow) {
		this.thresholdGrow = thresholdGrow;
	}

	public double getThresholdShrink() {
		return thresholdShrink;
	}

	public void setThresholdShrink(double thresholdShrink) {
		this.thresholdShrink = thresholdShrink;
	}

	public int getRatioExpand() {
		return ratioExpand;
	}

	public void setRatioExpand(int ratioExpand) {
		this.ratioExpand = ratioExpand;
	}

	public int getRatioShrink() {
		return ratioShrink;
	}

	public void setRatioShrink(int ratioShrink) {
		this.ratioShrink = ratioShrink;
	}

	public String getAutoScaling() {
		return autoScaling;
	}

	public void setAutoScaling(String autoScaling) {
		this.autoScaling = autoScaling;
	}
	
	
}
