import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by BD-Loen on 2017/4/14.
 */
public class rasRun {

    static long totalPhysicMemSize = 0L;
    static private OperatingSystemMXBean osmxb = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    static {
        totalPhysicMemSize = osmxb.getTotalPhysicalMemorySize();
    }

    private static double memWatchDog(){
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();

        //System.out.println(freePhysicalMemorySize);
        Double compare =  (1 - freePhysicalMemorySize * 1.0 / totalPhysicMemSize) * 100;

        // String str = compare.intValue() + "%";
        // System.out.println(compare);
        return compare;
    }

    public static String exeHist[] = {
            "create timeseries root.excavator.Beijing.rasp.mem with datatype = FLOAT , encoding = RLE",
            "select mem from root.test1.ras"
    };

    public static void main(String[] args)throws Exception {
        String localHost = "jdbc:tsfile://127.0.0.1:6667";
        String raspHost = "jdbc:tsfile://192.168.130.128:6667/";

        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.corp.tsfile.jdbc.TsfileDriver");
            connection = DriverManager.getConnection(localHost, "root", "root");
            statement = connection.createStatement();

            statement.execute(exeHist[exeHist.length - 1]);

            ResultSet res = statement.getResultSet();

            while (res.next()){

                System.out.println(res.getString(0));
                //System.out.println(res.getString("Timestamp") + '|' + res.getString("s1"));
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
