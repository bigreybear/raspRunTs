import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by leon on 17/4/21.
 */
public class drawPoints {

    public static RealTimeChart createCharter(){
        JFrame frame=new JFrame("Monitor Chart");
        RealTimeChart rtcp=new RealTimeChart("Raspberry Memory Percentage","Mem Percent","Percentage");
        frame.getContentPane().add(rtcp,new BorderLayout().CENTER);
        frame.pack();
        frame.setVisible(true);
        // (new Thread(rtcp)).start();
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent windowevent)
            {
                System.exit(0);
            }

        });
        return rtcp;
    }

    public static void main(String[] args) throws InterruptedException{

        JFrame frame=new JFrame("Monitor Chart");
        RealTimeChart rtcp=new RealTimeChart("Raspberry Memory Percentage","Mem Percent","Percentage");
        frame.getContentPane().add(rtcp,new BorderLayout().CENTER);
        frame.pack();
        frame.setVisible(true);
        (new Thread(rtcp)).start();
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent windowevent)
            {
                System.exit(0);
            }

        });

        while(true){
            Thread.sleep(1000);
            System.out.println("cool");
        }
    }
}
