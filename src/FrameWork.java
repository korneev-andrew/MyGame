import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
    int curi = -1;
    int curt = 0;
    short nextS = 0;
    boolean start = true;
    int time = 10000;

    JFrame frame = new JFrame("MyGame by andrew_korneev");
    JPanel panel = new JPanel();
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
        catch (FileNotFoundException fnf){JOptionPane.showMessageDialog(panel, "Please create MyGame.txt file with appropriate format", "Error", JOptionPane.ERROR_MESSAGE);}
        catch (UnsupportedEncodingException ue){JOptionPane.showMessageDialog(panel, "Unsupported encoding error. UTF-8 basic is needed", "Error", JOptionPane.ERROR_MESSAGE);}
    }


    void fillFrameWithStuff()
    {
        panel.setOpaque(true);
        panel.setLayout(null);
        panel.setBackground(Color.black);

        setTitle("MyGame by andrew_korneev");
        setPreferredSize(new Dimension((int) width,(int)height - 40));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(0,0);
        setContentPane(panel);
        setResizable(false);
        setBackground(Color.black);
        pack();


        JTextArea auction = new JTextArea();
        auction.setSize(100, 30);
        auction.setLocation((int)(width/2) - 60,(int) (height - 190));
        auction.setFont(new Font("Dialog", Font.BOLD,20));
        auction.setForeground(Color.white);
        auction.setBackground(Color.black);

        JTextArea Theme = new JTextArea();
        JTextArea Question = new JTextArea();
        JTextArea Answer = new JTextArea();
        //JTextField Answer = new JTextField();

        Theme.setLocation(0,0);
        Theme.setSize(new Dimension((int) (width-2), 50));
        Theme.setEditable(false);
        Theme.setText(theme[curt]);
        Theme.setFont(new Font("Serif", Font.PLAIN, 40));
        Theme.setLineWrap(true);
        Theme.setWrapStyleWord(true);
        Theme.setOpaque(false);
        Theme.setForeground(Color.white);

        Question.setLocation(0,(int)60);
        Question.setSize(new Dimension((int) (width - 2), (int) (height/2) ));
        Question.setEditable(false);
        Question.setFont(new Font("Dialog", Font.ITALIC,50));
        Question.setLineWrap(true);
        Question.setWrapStyleWord(true);
        Question.setOpaque(false);
        Question.setForeground(Color.white);



        JButton next = new JButton();
        next.setIcon(new ImageIcon(getClass().getClassLoader().getResource("next.png")));
        next.setSize(100,100);
        next.setLocation((int) width - 102,(int) (height - 170));
        next.setToolTipText("next question");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                start = true;
                auction.setBackground(Color.black);
                auction.setForeground(Color.white);
                if (curi < question.length)
                {
                    curi++;
                    time = 10000;
                    if (curi % 5 == 0 && curi!=0 && !Question.getText().equals(""))
                    {
                            curi--;
                            curt++;
                            Theme.setText(theme[curt]);
                            Question.setText("");
                            Answer.setText("");
                    }
                    else if(cost[curi].equals("var") && !Question.getText().equals("ВОПРОС-АУКЦИОН"))
                    {
                        curi--;
                        Question.setText("ВОПРОС-АУКЦИОН");
                        Answer.setText("");
                        auction.setBackground(Color.red.darker().darker());
                        auction.setForeground(Color.black);
                    }
                    else
                    {
                        if(curi>0) {
                            if (cost[curi - 1].equals("var")) {
                                auction.setText("");
                            }
                        }
                        Question.setText(cost[curi] + "$ " + question[curi]);
                        Answer.setText("");
                    }
                    if(curi>=0)
                    if (cost[curi].equals("var"))
                    {
                        time = 18000;
                    }
                }
            }
        });

        JButton previous = new JButton();
        previous.setIcon(new ImageIcon(getClass().getClassLoader().getResource("previous.png")));
        previous.setSize(100, 100);
        previous.setLocation(0,(int) (height - 170));
        previous.setToolTipText("previous question");
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                start = true;
                auction.setBackground(Color.black);
                auction.setForeground(Color.white);
                if (curi > 0)
                {
                    time = 10000;
                    curi--;
                    if(cost[curi].equals("var"))
                    {
                        Answer.setText("");
                        auction.setBackground(Color.red.darker().darker());
                        auction.setForeground(Color.black);
                        time = 18000;
                    }
                    if ((curi + 1) % 5 == 0)
                    {
                        curt--;
                        Theme.setText(theme[curt]);
                    }
                    if(Question.getText().equals("")&&!cost[curi].equals("var"))
                    {
                        curi++;
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
            player.setForeground(Color.white);
            player.setOpaque(false);

            points.setSize(130,30);
            points.setLocation(i * (int) (width / 6) + 40, (int) (height - 220));
            points.setFont(new Font("Dialog", Font.BOLD,20));
            points.setEditable(false);
            points.setText("0");
            points.setForeground(Color.white);
            points.setOpaque(false);

            plus.setSize(30,30);
            plus.setLocation(i * (int) (width / 6) + 40,(int)(height - 260));
            plus.setIcon(new ImageIcon(getClass().getClassLoader().getResource("plus.jpg")));
            plus.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if(cost[curi].equals("var") && curi>=0)
                    {
                        if(!auction.getText().startsWith(" ")&&!auction.equals(""))
                        {
                            int sum = Integer.parseInt(points.getText());
                            sum += Integer.parseInt(auction.getText());
                            points.setText(sum + "");
                        }
                    }
                    else if(curi>=0) {
                        int sum = Integer.parseInt(points.getText());
                        sum += Integer.parseInt(cost[curi]);
                        points.setText(sum + "");
                    }
                }
            });

            minus.setSize(30,30);
            minus.setLocation(i * (int) (width / 6) + 140,(int)(height - 260));
            minus.setIcon(new ImageIcon(getClass().getClassLoader().getResource("minus.png")));
            minus.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if(cost[curi].equals("var") && curi>=0)
                    {
                        if(points.getText().length()>0) {
                            int sum = Integer.parseInt(points.getText());
                            sum -= Integer.parseInt(auction.getText());
                            points.setText(sum + "");
                        }
                    }
                    else if(curi>=0) {
                        int sum = Integer.parseInt(points.getText());
                        sum -= Integer.parseInt(cost[curi]);
                        points.setText(sum + "");
                    }
                }
            });

            zero.setSize(30,30);
            zero.setLocation(i * (int) (width / 6) + 90,(int)(height - 260));
            zero.setIcon(new ImageIcon(getClass().getClassLoader().getResource("zero.png")));
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

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("Gummy Boy - Night Driver.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

            AudioInputStream audioIn1 = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("The Smiths - This Charming man.wav"));
            Clip clip1 = AudioSystem.getClip();
            clip1.open(audioIn1);

            AudioInputStream audioIn2 = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("HOME - Head First.wav"));
            Clip clip2 = AudioSystem.getClip();
            clip2.open(audioIn2);

            JButton play = new JButton();
            play.setLocation((int)( width - 320),(int) (height - 170));
            play.setSize(100, 100);
            play.setIcon(new ImageIcon(getClass().getClassLoader().getResource("pause.png")));
            play.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(nextS==0)
                    {
                        if (clip.isRunning())
                        {
                            play.setIcon(new ImageIcon(getClass().getClassLoader().getResource("play.png")));
                            clip.stop();
                        }
                        else
                        {
                            play.setIcon(new ImageIcon(getClass().getClassLoader().getResource("pause.png")));
                            clip.start();
                        }
                    }
                    if(nextS==1)
                    {
                        if (clip1.isRunning())
                        {
                            play.setIcon(new ImageIcon(getClass().getClassLoader().getResource("play.png")));
                            clip1.stop();
                        }
                        else
                        {
                            play.setIcon(new ImageIcon(getClass().getClassLoader().getResource("pause.png")));
                            clip1.start();
                        }
                    }
                    if(nextS==2)
                    {
                        if (clip2.isRunning())
                        {
                            play.setIcon(new ImageIcon(getClass().getClassLoader().getResource("play.png")));
                            clip2.stop();
                        }
                        else
                        {
                            play.setIcon(new ImageIcon(getClass().getClassLoader().getResource("pause.png")));
                            clip2.start();
                        }
                    }
                }
            });

            JButton nextSong = new JButton();
            nextSong.setLocation((int) width - 210,(int) (height - 170));
            nextSong.setSize(100, 100);
            nextSong.setToolTipText("Play next song");
            nextSong.setIcon(new ImageIcon(getClass().getClassLoader().getResource("nextSong.jpg")));
            nextSong.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (nextS==0)
                    {
                        nextS++;
                        clip.stop();
                        clip1.setFramePosition(0);
                        clip1.start();
                        play.setIcon(new ImageIcon(getClass().getClassLoader().getResource("pause.png")));
                    }
                    else if(nextS==1)
                    {
                        nextS++;
                        clip1.stop();
                        clip2.setFramePosition(0);
                        clip2.start();
                        play.setIcon(new ImageIcon(getClass().getClassLoader().getResource("pause.png")));
                    }
                    else if(nextS==2)
                    {
                        nextS=0;
                        clip2.stop();
                        clip.setFramePosition(0);
                        clip.start();
                        play.setIcon(new ImageIcon(getClass().getClassLoader().getResource("pause.png")));
                    }
                }
            });

            panel.add(play);
            panel.add(nextSong);

            }
        catch (Exception lu){}


        JButton timer = new JButton();
        timer.setLocation(110,(int) (height - 170));
        timer.setSize(100, 100);
        timer.setToolTipText("Start timer");
        timer.setIcon(new ImageIcon(getClass().getClassLoader().getResource("timer.jpg")));
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(start)
                {
                    start = false;
                    new Timer(time);
                    time = 10000;
                }
            }
        });

        JButton getAnswer = new JButton();
        getAnswer.setSize(100,100);
        getAnswer.setLocation(220,(int) (height - 170));
        getAnswer.setIcon(new ImageIcon(getClass().getClassLoader().getResource("answer.png")));
        getAnswer.setToolTipText("Get the answer to it");
        getAnswer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Answer.getText().length()>0)
                {
                    Answer.setText("");
                }
                else if(curi>=0)
                Answer.setText(answer[curi]);
            }
        });

        Answer.setSize((int)width/2,84);
        Answer.setLocation(340, (int) (height - 160));
        Answer.setEditable(false);
        Answer.setFont(new Font("Dialog", Font.BOLD + Font.ITALIC,30));
        Answer.setOpaque(false);
        Answer.setForeground(Color.white);
        Answer.setBackground(Color.black);
        Answer.setLineWrap(true);



        panel.add(getAnswer);
        panel.add(Answer);
        panel.add(Theme);
        panel.add(Question);
        panel.add(next);
        panel.add(previous);
        panel.add(timer);
        panel.add(auction);

        // background image
        JLabel bg = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("brains.jpg")));
        bg.setVisible(true);
        bg.setLocation((int)(width/2 - 509),(int)(height/2 - 40 - 236));
        bg.setSize(1018,472);
        panel.add(bg);

        validate();
    }
}
