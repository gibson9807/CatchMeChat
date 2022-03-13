import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientRegister extends JFrame {
    static String info;

    private JPanel mainPanel;
    private JButton button;
    private JTextField idText;
    private JLabel headerLabel;

    public ClientRegister(String title) {
        super(title);
        try {
            //Initialization of GUI components
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setContentPane(mainPanel);
            this.pack();
            button.addActionListener(this::idButtonActionPerformed);


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void idButtonActionPerformed(ActionEvent e) {
        try {
            String id = idText.getText();
            Socket s = new Socket("localhost", 8000);
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            dout.writeUTF(id);
            String i = new DataInputStream(s.getInputStream()).readUTF();
            if (i.equals("Jesteś już zarejestrowany")) {
                JOptionPane.showMessageDialog(this, "Jesteś już zarejestrowany\n");
            } else {
                new Client(id, s,"Klient "+id).setVisible(true);
                this.dispose();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public static void main(String[] args) {
        JFrame frame = new ClientRegister("CatchMe Client Register");
        frame.setVisible(true);
    }
}
