import com.corp.tsfile.schema.converter.TSDataTypeConverter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
        try {
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
                    // first time to enter
                    exeStat = "select mem, tid from root.test1.ras where root.test1.ras.tid > 180787111";
                    hasRes = statement.execute(exeStat);
                    if (hasRes){
                        rs = statement.getResultSet();
                        while (rs.next()){
                            if (tid_base < new Long(rs.getString("root.test1.ras.tid")))
                            {
                                System.out.println("tid:");
                                System.out.println(rs.getString("root.test1.ras.tid"));
                                tid_base = new Long(rs.getString("root.test1.ras.tid"));
                            }
                        }
                    }
                }
                else {
                    //n_tid = System.currentTimeMillis() % 1000000000;
                    System.out.println(tid_base);
                    exeStat = "select mem from root.test1.ras where root.test1.ras.tid >=" + (tid_base - 1001);
                    hasRes = statement.execute(exeStat);
                    if (hasRes) {
                        rs = statement.getResultSet();
                        System.out.println(rs.getString("root.test1.ras.mem"));
                    }
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
