import javafx.scene.media.MediaPlayer;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

/**
 * Created by andrew_korneev on 29.03.2016.
 */
public class FrameWork extends JFrame
{
    String[] data;
    String[] cost;
    String[] question;
    String[] answer;
    String[] theme;
    int i = 0;
    int t = 0;
    String currentString;
    int curi = 0;
    int curt = 0;

    JFrame frame = new JFrame("MyGame by andrew_korneev");
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();

    FrameWork()
    {
        read();
        fillFrameWithStuff();
    }

    void read()
    {
        try {
            FileInputStream file = new FileInputStream("MyGame.txt");
            Scanner reader = new Scanner(new InputStreamReader(file,"UTF-8"));

            int lines = Integer.parseInt(reader.nextLine());
            data = new String[lines];
            cost = new String[lines];
            question = new String[lines];
            answer = new String[lines];
            theme = new String[lines/5];

            while(reader.hasNextLine())
            {
                currentString = reader.nextLine();
                if(!currentString.startsWith("Тема") && !currentString.equals("") && !currentString.startsWith(" "))
                {
                    String[] splittedData = currentString.split("[\\$()]");
                    cost[i] = splittedData[0];
                    question[i] = splittedData[1];
                    answer[i] = splittedData[2];
                    i++;
                }
                else if(currentString.startsWith("Тема"))
                {
                    theme[t] = currentString;
                    t++;
                }
            }
            reader.close();
        }
        catch (FileNotFoundException fnf){}
        catch (UnsupportedEncodingException ue){}
    }


    void fillFrameWithStuff()
    {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setLayout(null);
        panel.setBackground(Color.getHSBColor(0.36f, 0.20f, 0.9f));

        setTitle("MyGame by andrew_korneev");
        setPreferredSize(new Dimension((int) width,(int)height - 40));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(0,0);
        setContentPane(panel);
        setResizable(false);
        pack();


        JTextArea Theme = new JTextArea();
        JTextArea Question = new JTextArea();
        JTextArea Answer = new JTextArea();

        Theme.setLocation(0,0);
        Theme.setSize(new Dimension((int) (width),110));
        Theme.setBackground(Color.getHSBColor(0.36f, 0.20f, 0.9f));
        Theme.setEditable(false);
        Theme.setText(theme[curt]);
        Theme.setFont(new Font("Serif", Font.PLAIN, 40));
        Theme.setLineWrap(true);
        Theme.setWrapStyleWord(true);

        Question.setLocation(0,(int)height/4 - 80);
        Question.setSize(new Dimension((int) (width),350));
        Question.setBackground(Color.getHSBColor(0.36f, 0.20f, 0.9f));
        Question.setEditable(false);
        Question.setText(cost[curi] + "$ " + question[curi]);
        Question.setFont(new Font("Dialog", Font.ITALIC,50));
        Question.setLineWrap(true);
        Question.setWrapStyleWord(true);



        JButton next = new JButton();
        next.setIcon(new ImageIcon(getClass().getClassLoader().getResource("next.jpg")));
        next.setBackground(Color.green);
        next.setSize(100,100);
        next.setLocation((int) width - 102,(int) (height - 170));
        next.setToolTipText("next question");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (curi < question.length)
                {
                    curi++;
                    if (curi % 5 == 0)
                    {
                        curt++;
                        Theme.setText(theme[curt]);
                    }
                    Question.setText(cost[curi] + "$ " + question[curi]);
                    Answer.setText("");
                }
            }
        });

        JButton previous = new JButton();
        previous.setIcon(new ImageIcon(getClass().getClassLoader().getResource("previous.jpg")));
        previous.setBackground(Color.green);
        previous.setSize(100, 100);
        previous.setLocation(0,(int) (height - 170));
        previous.setToolTipText("previous question");
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (curi > 0) {
                    curi--;
                    if ((curi + 1) % 5 == 0 && curi != 0) {
                        curt--;
                        Theme.setText(theme[curt]);
                    }
                    Question.setText(cost[curi] + "$ " + question[curi]);
                    Answer.setText("");
                }
            }
        });

        for(int i =0;i<6;i++)
        {
            JTextArea player = new JTextArea();
            JTextArea points = new JTextArea();
            JButton plus = new JButton();
            JButton minus = new JButton();
            JButton zero = new JButton();

            player.setSize(130, 30);
            player.setLocation(i * (int) (width / 6) + 40, (int) (height - 300));
            player.setFont(new Font("Dialog", Font.BOLD,20));
            player.setBackground(Color.getHSBColor(0.26f, 0.30f, 0.9f));

            points.setSize(130,30);
            points.setLocation(i * (int) (width / 6) + 40, (int) (height - 220));
            points.setFont(new Font("Dialog", Font.BOLD,20));
            points.setEditable(false);
            points.setText("0");
            points.setBackground(Color.getHSBColor(0.26f, 0.20f, 0.9f));

            plus.setSize(30,30);
            plus.setLocation(i * (int) (width / 6) + 40,(int)(height - 260));
            plus.setIcon(new ImageIcon(getClass().getClassLoader().getResource("plus.jpg")));
            plus.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int sum = Integer.parseInt(points.getText());
                    sum += Integer.parseInt(cost[curi]);
                    points.setText(sum + "");
                }
            });

            minus.setSize(30,30);
            minus.setLocation(i * (int) (width / 6) + 140,(int)(height - 260));
            minus.setIcon(new ImageIcon(getClass().getClassLoader().getResource("minus.jpg")));
            minus.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int sum = Integer.parseInt(points.getText());
                    sum -= Integer.parseInt(cost[curi]);
                    points.setText(sum + "");
                }
            });

            zero.setSize(30,30);
            zero.setLocation(i * (int) (width / 6) + 90,(int)(height - 260));
            zero.setIcon(new ImageIcon(getClass().getClassLoader().getResource("zero.jpg")));
            zero.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    points.setText("0");
                }
            });

            panel.add(player);
            panel.add(points);
            panel.add(plus);
            panel.add(minus);
            panel.add(zero);
        }

        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("Royal Crown Revue - Hey Pachuco.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

            JButton play = new JButton();
            play.setLocation((int)120, (int) (height - 170));
            play.setSize(100, 100);
            play.setIcon(new ImageIcon(getClass().getClassLoader().getResource("play.jpg")));
            play.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (clip.isOpen()) {
                        if (clip.isActive())
                            clip.stop();
                        else clip.start();
                    }
                }
            });

            panel.add(play);

            }
        catch (Exception lu){}


        JButton getAnswer = new JButton();
        getAnswer.setSize(100,100);
        getAnswer.setLocation(230,(int) (height - 170));
        getAnswer.setIcon(new ImageIcon(getClass().getClassLoader().getResource("answer.jpg")));
        getAnswer.setToolTipText("Get the answer to it");
        getAnswer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Answer.setText(answer[curi]);
            }
        });

        Answer.setSize((int)width/2,100);
        Answer.setLocation(340, (int) (height - 170));
        Answer.setBackground(Color.getHSBColor(0.26f, 0.30f, 0.9f));
        Answer.setEditable(false);
        Answer.setFont(new Font("Dialog", Font.BOLD + Font.ITALIC,40));


        panel.add(getAnswer);
        panel.add(Answer);
        panel.add(Theme);
        panel.add(Question);
        panel.add(next);
        panel.add(previous);


        validate();
    }
}
