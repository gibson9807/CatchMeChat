import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
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

    String ID,clientID="";
    DataInputStream din;
    DataOutputStream dout;
    DefaultListModel dlm;

    public Client(String i, Socket s,String title) {
        super(title);
        ID=i;
        try {
            //Initialization of GUI components
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setContentPane(mainPanel);
            this.pack();
            sendButton.addActionListener(this::sendButtonactionPerformed);


            dlm=new DefaultListModel();
            userList.setModel(dlm);
            labelName.setText(i);
            din=new DataInputStream(s.getInputStream());
            dout=new DataOutputStream(s.getOutputStream());
            new Read().start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void formWindowClosing(java.awt.event.WindowEvent evt){
        String i="closedWindow";
        try{
            dout.writeUTF(i);
            this.dispose();
        }catch(IOException ex){
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE,null,ex);
        }
    }


    public void sendButtonactionPerformed(ActionEvent e) {
        try {
            String m = sendText.getText(), mm = m;
            String CI = clientID;
            if (!clientID.isEmpty()) {
                m = "PrivateMsg" + CI + ":" + mm;
                dout.writeUTF(m);
                sendText.setText("");
                msgBox.append("< TY do " + CI + " > " + mm + "\n");

            } else {
                dout.writeUTF(m);
                sendText.setText("");
                msgBox.append("< TY do wszystkich > " + mm + "\n");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Taki użytkownik nie istnieje");
        }
    }

    class Read extends Thread {

        public void run() {
            while (true) {
                try {
                    String m = din.readUTF();
                    if (m.contains(":;.,/=")) {
                        m = m.substring(6);
                        dlm.clear();
                        StringTokenizer st = new StringTokenizer(m, ",");
                        while (st.hasMoreTokens()) {
                            String u = st.nextToken();
                            if (!ID.equals(u)) {
                                dlm.addElement(u);
                            }
                        }

                    } else {
                        msgBox.append("" + m + "\n");
                    }
                } catch (Exception ex) {
                    break;
                }

            }
        }
    }

}
