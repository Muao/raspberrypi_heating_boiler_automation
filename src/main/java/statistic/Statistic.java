package statistic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Statistic {

    public static String get() {
        final String raspberryUptime = getRaspberryUptime();
        final String appUptime = getAppUptime();
        return "app run uptime: " + appUptime + "; \\n Raspberry running uptime: " + raspberryUptime;
    }

    private static String getRaspberryUptime() {
        String uptime = "";
        Process uptimeProc;
        try {
            uptimeProc = Runtime.getRuntime().exec("uptime");
        } catch (IOException e) {
            e.printStackTrace();
            return "can't execute uptime";
        }
        final BufferedReader in = new BufferedReader(new InputStreamReader(uptimeProc.getInputStream()));

        try {
            uptime = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "can't read line from buffer";
        }
        return uptime;
    }

    private static String getAppUptime() {
        final RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
        final SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        return formatter.format(new Date(mxBean.getUptime()));
    }
}
