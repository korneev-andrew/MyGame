import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Created by andrew_korneev on 10.04.2016.
 */


// timer
public class Timer implements Runnable
{
    int time;

    Timer(int time)
    {
        this.time=time;
        new Thread(this).start();
    }

    @Override
    public void run()
    {
        try
        {
            Thread.sleep(time);
        }
        catch (InterruptedException inter) {}

        try {
            AudioInputStream audioIn2 = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("notification.wav"));
            Clip notification = AudioSystem.getClip();
            notification.open(audioIn2);
            notification.start();
            audioIn2.close();
        }
        catch (Exception note) {}

    }
}
