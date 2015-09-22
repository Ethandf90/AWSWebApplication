package ece1779.ec2.monitor;

import ece1779.ec2.mode.ManagerMode;
import ece1779.ec2.service.ManagerService;

public class MonitorThread implements Runnable{

	public void run() {
		ManagerService managerservice = new ManagerService();
		try{
			while(true){
				ManagerMode manager = managerservice.getAutoScalingParameter();
				double cpu = managerservice.averageCPU();
				//if autoscaling activated
				if(manager.getAutoScaling().equals("YES")){
					if(manager.getThresholdGrow()!=0 && manager.getRatioExpand()!=0 && cpu >manager.getThresholdGrow()){
						System.out.println("auto add");
						int runningNum = managerservice.getRunningWorkers().size();
						int increaseNum = runningNum * (manager.getRatioExpand() - 1);
						if(runningNum + increaseNum > 20){
							increaseNum = 20 - runningNum;
						}
						/////////////
						managerservice.increaseWorkers(increaseNum);
						System.out.println(increaseNum);
					}
					if(manager.getThresholdShrink()!=0 && manager.getRatioShrink()!=0 && cpu <manager.getThresholdShrink()){
						System.out.println("auto stop");
						int runningNum = managerservice.getRunningWorkers().size();
						int decreaseNum = runningNum * (manager.getRatioShrink()-1)/manager.getRatioShrink();
						if(decreaseNum > 0 && decreaseNum < runningNum){
							/////////////
							managerservice.reduceWorkers(decreaseNum);
							System.out.println(decreaseNum);
						}
					}
				}
				/////////3 minutes
				Thread.sleep(180000);
				System.out.println("sleeping");
			}
		}
		catch(Exception e) {
	       	 System.out.println("thread Error Message: " + e.getMessage());
	        }
		
	}

}
