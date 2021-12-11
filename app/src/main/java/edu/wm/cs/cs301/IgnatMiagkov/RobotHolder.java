package edu.wm.cs.cs301.IgnatMiagkov;

import edu.wm.cs.cs301.IgnatMiagkov.gui.Robot;
import edu.wm.cs.cs301.IgnatMiagkov.gui.RobotDriver;

public class RobotHolder {
    private static String sensors;
    private static RobotDriver driver;

    public static String getDataSensors(){
        return sensors;
    }

    public static void setDataSensors(String s){
        sensors = s;
    }

    public static RobotDriver getDataDriver(){
        return driver;
    }

    public static void setDataDriver(RobotDriver d){
        driver = d;
    }

}
