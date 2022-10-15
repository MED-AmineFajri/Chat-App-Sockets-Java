import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.*;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame {
    
    static int nb =1;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //components
    private JLabel heading = new JLabel("Client");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Client()
    {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("localhost", 2011);
            System.out.println("connection done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            startWriting();
            

        } catch(Exception e) {

        }
    }
    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10)
                {
                    //System.out.println("send message");
                    String ContentTosend = messageInput.getText();
                    messageArea.append("Me : " + ContentTosend + "\n");
                    out.println(ContentTosend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

        });
    }
    public void createGUI() {
        this.setTitle("Client Session");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        ImageIcon imageIcon = new ImageIcon("./img/logo"); // load the image to a imageIcon
        Image imagee = imageIcon.getImage(); // transform it 
        Image newimg = imagee.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        imageIcon = new ImageIcon(newimg);  // transform it back
        heading.setIcon(new ImageIcon(newimg));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLayout(new BorderLayout());

        this.add(heading,BorderLayout.NORTH);
        JScrollPane JScrollPane = new JScrollPane(messageArea);
        this.add(JScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void startReading()
    {
        Runnable r1 = () -> {
            System.out.println("reader start");
            try {
            while(true)
            {
                
                    String msg = br.readLine();
                    if (msg.equals("exit"))
                    {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "server Ternineted");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Server:" + msg);
                    messageArea.append( msg + "\n");
                
            }
        } catch (IOException e) {
            System.out.println("Connect closed");
        }
        };
        new Thread(r1).start();
    }
    public void startWriting()
    {
        System.out.println("writer start ...");
        Runnable r2 = () -> {
            try {
            while(!socket.isClosed() ) {
            
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                String content = br1.readLine();
                String msg = br.readLine();
                out.println(content);
                out.flush();
                if (content.equals("exit"))
                {
                    socket.close();
                    break;
                }
            }
                System.out.println("connection closed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    new Thread(r2).start();
    }
    public static void main (String [] args)
    {
        System.out.println("this is client");
        new Client();
    }
}
