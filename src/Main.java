import javax.swing.*;

/**
 * Created by andrew_korneev on 29.03.2016.
 */
public class Main
{
    public static void main(String[] args)
    {    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {}
        SwingUtilities.invokeLater(new Runnable() {
            public void run()
            {
                JFrame frame = new FrameWork();
            }
        });
    }
}
