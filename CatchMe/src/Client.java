import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends JFrame {

    private JPanel mainPanel;
    private JTextField sendText;
    private JButton sendButton;
    private JTextArea msgBox;
    //   private JTextPane userList;
    private JLabel labelName;
    private JButton selectAllButton;
    private JList userList;

    String ID, clientID = "";
    DataInputStream din;
    DataOutputStream dout;
    DefaultListModel dlm;

    public Client(String id, Socket socket, String title) {
        super(title);
        ID = id;
        initializeGuiComponents();
        try {
            dlm = new DefaultListModel();
            userList.setModel(dlm);
            labelName.setText(id);
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            new Read().start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initializeGuiComponents() {
        //Initialization of GUI components
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        sendButton.addActionListener(this::sendButtonActionPerformed);
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        String i = "closedWindow";
        try {
            System.out.println("WINDOW CLOSED test z Client");
            dout.writeUTF(i);
            this.dispose();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendButtonActionPerformed(ActionEvent e) {
        try {
            String message = sendText.getText(), mm = message;
            String clientID = this.clientID;
            if (!this.clientID.isEmpty()) {
                message = "PrivateMsg" + clientID + ":" + mm;
                dout.writeUTF(message);
                sendText.setText("");
                msgBox.append("< TY do " + clientID + " > " + mm + "\n");
            } else {
                dout.writeUTF(message);
                sendText.setText("");
                msgBox.append("< TY do wszystkich > " + mm + "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Taki użytkownik nie istnieje");
        }
    }

    class Read extends Thread {
        DefaultListModel<String> model = new DefaultListModel<>();
        public void run() {
           // System.out.println("RUN w Client");
            while (true) {
                try {
                  //  System.out.println("TRY w RUN w Client");
                    String message = din.readUTF();
                    System.out.println("Message z Client: "+message);
                    if (message.startsWith("user:")) {
                       // System.out.println("STARTS WITH USER: w Client");
                        userList.setModel(model);
                        model.addElement(message.substring(5)+"Test w Client");
                    } else if (message.contains(":;.,/=")) {
                      // System.out.println("CONTAINS;,.;");
                        message = message.substring(6);
                        dlm.clear();
                        StringTokenizer st = new StringTokenizer(message, ",");
                        while (st.hasMoreTokens()) {
                            String u = st.nextToken();
                            if (!ID.equals(u)) {
                                dlm.addElement(u);
                            }
                        }
                    } else {
                       // System.out.println("ELSE w Client");
                        msgBox.append("" + message + "\n");
                    }
                } catch (Exception ex) {
//                    ex.printStackTrace();
                    break;
                }
            }
        }
    }
}
