import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static java.lang.System.in;
import static java.lang.System.setOut;

/**
 * Created by BD-Loen on 2017/4/14.
 */
public class test {
    public static String  inits[] = {
//            "create timeseries root.excavator.Beijing.d1.s1 with datatype=INT32,encoding=RLE",
//            "create timeseries root.excavator.Beijing.d1.s2 with datatype=FLOAT,encoding=RLE",
//            "create timeseries root.excavator.Beijing.d2.s1 with datatype=INT32,encoding=RLE",
//            "create timeseries root.excavator.Beijing.d2.s2 with datatype=FLOAT,encoding=RLE",
//            "create timeseries root.excavator.Shanghai.d3.s1 with datatype=INT32,encoding=RLE",
//            "create timeseries root.excavator.Shanghai.d3.s2 with datatype=FLOAT,encoding=RLE",
//            "set storage group to root.excavator.Beijing.d1",
//            "delete from root.excavator.Beijing.d1.s1 where time < 1479247862729",
//            "create timeseries root.test1.ras.mem with datatype=FLOAT, encoding=RLE",
            "set storage group to root.test1.ras",
            "insert into root.test1.ras.mem values(2, 1)",
            "insert into root.test1.ras.mem values(6, 2)",
            "insert into root.test1.ras.mem values(8, 3)",


    };

    public static String exeHist[] = {
            "create timeseries root.excavator.Beijing.rasp.mem with datatype = FLOAT , encoding = RLE",
            "select mem from root.test1.ras"
    };

    public static void main(String[] args)throws Exception {
        boolean ifQuery = true;
        boolean hasResSet = true;
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.corp.tsfile.jdbc.TsfileDriver");
            connection = DriverManager.getConnection("jdbc:tsfile://192.168.130.128:6667/", "root", "root");
            statement = connection.createStatement();
            for(String exes : inits){
                statement.execute(exes);
                System.out.println(exes);
            }
            ;
            if(!ifQuery) {
                statement.execute(exeHist[exeHist.length - 1]);
            }
            else{
                hasResSet = statement.execute(exeHist[exeHist.length-1]);
            }
            if(ifQuery & hasResSet){
                ResultSet res = statement.getResultSet();

                while (res.next()){

                    System.out.println(res.getString(0));
                    //System.out.println(res.getString("Timestamp") + '|' + res.getString("s1"));
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
