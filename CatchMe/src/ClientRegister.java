import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
            Socket socket = new Socket("localhost", 8080);
            //DataInputStream din = new DataInputStream(socket.getInputStream());
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            dout.writeUTF(id);
            String i = new DataInputStream(socket.getInputStream()).readUTF();
            if (i.equals("Jesteś już zarejestrowany")) {
                JOptionPane.showMessageDialog(this, "Jesteś już zarejestrowany\n");
            } else {
                new Client(id, socket, "Klient " + id).setVisible(true);
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
