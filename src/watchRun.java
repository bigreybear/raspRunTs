import com.corp.tsfile.schema.converter.TSDataTypeConverter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Created by BD-Loen on 2017/4/14.
 */
public class watchRun {


    public static void main(String[] args)throws Exception {
        String raspHost = "jdbc:tsfile://192.168.130.128:6667/";

        Connection connection = null;
        Statement statement = null;
        BufferedReader wt = new BufferedReader(new InputStreamReader(System.in));
        // long n_tid = 0;
        long tid_base = new Long(0);
        float res_get = 50.0f;
        int rep_count = 0;

        try {
            RealTimeChart rtc = drawPoints.createCharter();

            Class.forName("com.corp.tsfile.jdbc.TsfileDriver");
            connection = DriverManager.getConnection(raspHost, "root", "root");
            statement = connection.createStatement();

            String exeStat = "";

            int count = 100;
            boolean hasRes = false;
            ResultSet rs = null;
            while (count<36000){
                count ++;
                if (count%120==0){
                    System.out.println("Enter 'Y' to CONTINUE.");
                    String ansToContinue = wt.readLine();
                    if (ansToContinue.equals("Y") | ansToContinue.equals("y")){
                        System.out.println("CONTINUE");
                    }
                    else{
                        break;
                    }
                }

                if (tid_base==0){
                    // first time to
                    if (tid_base == 0) {
                        exeStat = "select mem, tid from root.test1.ras where time > 1492227389774";
                        hasRes = statement.execute(exeStat);
                    }
                    if (hasRes){
                        rs = statement.getResultSet();
                        while (rs.next()){
                            if (tid_base < new Long(rs.getString("Timestamp")))
                            {
                                tid_base = new Long(rs.getString("Timestamp"));
                            }
                        }
                        if (tid_base ==0) {
                            System.out.println("final initial base tid:");
                            System.out.println(tid_base);
                        }

                    }
                }
                if(tid_base!=0) {
                    //n_tid = System.currentTimeMillis() % 1000000000;
                    // System.out.println(tid_base);
                    exeStat = "select mem from root.test1.ras where time >" + tid_base;
                    hasRes = statement.execute(exeStat);
                    if (hasRes) {
                        rs = statement.getResultSet();
                        rep_count = 0;
                        while(rs.next()){
                            System.out.println(rs.getString("root.test1.ras.mem"));
                            rep_count ++ ;
                            res_get =Float.parseFloat(rs.getString("root.test1.ras.mem"));
                        }
                        rtc.addDataPoint(res_get);
                        if (rep_count>1){
                            if (rep_count>2){
                                tid_base += 1000*(rep_count-2);
                            }
                            tid_base += 500;
                        }
                        System.out.println("--------------------------------------------------------");
                    }
                    Thread.sleep(1005);
                    tid_base += 1000;
                }


            }




        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if(statement != null){
                statement.close();
            }
            if(connection != null){
                connection.close();
            }
        }
    }
}
