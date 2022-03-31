import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

import java.awt.BorderLayout;

import javax.swing.JButton;
//import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * A simple Swing-based client for the chat server. Graphically it is a frame with a text
 * field for entering messages and a textarea to see the whole dialog.
 *
 * The client follows the following Chat Protocol. When the server sends "SUBMITNAME" the
 * client replies with the desired screen name. The server will keep sending "SUBMITNAME"
 * requests as long as the client submits screen names that are already in use. When the
 * server sends a line beginning with "NAMEACCEPTED" the client is now allowed to start
 * sending the server arbitrary strings to be broadcast to all chatters connected to the
 * server. When the server sends a line beginning with "MESSAGE" then all characters
 * following this string should be displayed in its message area.
 */
public class ChatClient {

    String serverAddress;
    Scanner in;
    PrintWriter out;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(50);
    JTextArea messageArea = new JTextArea(16, 50);
    JTextArea memberArea = new JTextArea(1,50);
    JButton sendButton = new JButton("Send");
    JPanel panel = new JPanel();
    SocketAddress clientAddress;
    String client;
   
    //@Override
    //public String toString() {
    	//String[] split = client.split(":");
    	//String ip = split[0];
    	//String port = split[1];
    	//return "IP='" + ip +'\'' + ", port='" + port + '\'' ;
    //}

    /**
     * Constructs the client by laying out the GUI and registering a listener with the
     * textfield so that pressing Return in the listener sends the textfield contents
     * to the server. Note however that the textfield is initially NOT editable, and
     * only becomes editable AFTER the client receives the NAMEACCEPTED message from
     * the server.
     */
    public ChatClient(String serverAddress) {
        this.serverAddress = serverAddress;
        textField.setEditable(false);
        messageArea.setEditable(false);
        textField.setEditable(false); //Where the user types messages 
        messageArea.setEditable(false); // Where all messages are seen
        memberArea.setEditable(false);//Where a list of members currently connect is seen
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.getContentPane().add(new JScrollPane(memberArea), BorderLayout.EAST);
        frame.getContentPane().add(sendButton,BorderLayout.EAST);
        frame.setVisible(true);
        frame.pack();
        
        
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
			}
		});
		

        // Send on enter then clear to prepare for next message
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }

	private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE
        );
    }
	/*private String getIP() {
        return JOptionPane.showInputDialog(
            frame,
            "Enter an IP Address:",
            "IP Selection",
            JOptionPane.PLAIN_MESSAGE
        );
    }
	private String getPort() {
        return JOptionPane.showInputDialog(
    		frame,
            "Enter an port to listen to:",
            "Port Selection",
            JOptionPane.PLAIN_MESSAGE
        );
	}*/
	
    private void run() throws IOException {
        try {
            Socket socket = new Socket(serverAddress, 59001);
            clientAddress  = socket.getLocalSocketAddress();
            client = "" + clientAddress;
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            //out.print(client);
            // ClientIP address
            System.out.print("Client IP/Port: " + clientAddress);

            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.startsWith("SUBMITNAME")) {
                    out.println(getName());
                } else if (line.startsWith("NAMEACCEPTED")) {
                    this.frame.setTitle("Chatter - " + line.substring(13));
                    textField.setEditable(true);
                } else if (line.startsWith("MESSAGE")) {
                    messageArea.append(line.substring(8) + "\n");
               /* } else if (line.startsWith("SUBMITIP")) {
                    out.println(getIP());
                } else if (line.startsWith("SUBMITPORT")) {
                	out.println(getPort());
                */}
            }
        } finally {
            frame.setVisible(false);
            frame.dispose();
        }
    }


    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        ChatClient client = new ChatClient(args[0]);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}
