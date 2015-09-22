package ece1779.ec2.monitor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MonitorListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent arg0)  { 
         MonitorThread monitor = new MonitorThread();
         new Thread(monitor).start();
    }

    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }
	
}
