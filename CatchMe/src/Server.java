import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

public class Server extends JFrame {
    private JPanel mainPanel;
    private JPanel serverStatus;
    private JPanel serverList;
    private JLabel sStatus;
    private JTextArea msgBox;


    ServerSocket ss;
    HashMap clientsMap = new HashMap();


    public Server(String title) {
        super(title);

        try {
            //Initialization of GUI components
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setContentPane(mainPanel);
            this.pack();
            //Server features
            ss = new ServerSocket(8000);
            this.sStatus.setText("Serwer uruchomiony");
            new ClientAccept().start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    class ClientAccept extends Thread {

        @Override
        public void run() {
            try {
                while (true) {
                    Socket s = ss.accept();
                    String i = new DataInputStream(s.getInputStream()).readUTF();
                    if (clientsMap.containsKey(i)) {
                        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                        dout.writeUTF("Jesteś już zarejestrowany");
                    } else {
                        clientsMap.put(i, s);
                        msgBox.append(i + " dołączył(a)! \n");
                        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                        dout.writeUTF("");
                        new MsgRead(s,i).start();
                    }
                }
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class MsgRead extends Thread{
        Socket s;
        String ID;

        MsgRead(Socket s, String ID){
            this.s=s;
            this.ID=ID;
        }

        @Override
        public void run() {
           try {
                while(!clientsMap.isEmpty()){
                    String i =new DataInputStream(s.getInputStream()).readUTF();
                    if(i.equals("closedWindow")){
                        clientsMap.remove(ID);
                        msgBox.append(ID + ": usunięty(a)! \n");
                        new PrepareClientList().start();
                        Set<String> k= clientsMap.keySet();
                        Iterator itr = k.iterator();
                        while(itr.hasNext()){
                            String key=(String)itr.next();
                            if(!key.equalsIgnoreCase(ID)){
                                try{
                                    new DataOutputStream(((Socket) clientsMap.get(key)).getOutputStream()).writeUTF(i);
                                }catch(Exception ex){
                                    clientsMap.remove(key);
                                    msgBox.append(key+": usunięty(a)!");
                                    new PrepareClientList().start();
                                }
                            }
                        }
                    }else if(i.contains("PrivateMsg")){
                        i=i.substring(20);
                        StringTokenizer st=new StringTokenizer(i,":");
                        String id=st.nextToken();
                        i=st.nextToken();
                        try{
                            new DataOutputStream(((Socket) clientsMap.get(id)).getOutputStream()).writeUTF("< "+ID+" do "+id+" > "+i);
                        }catch(Exception ex){
                            clientsMap.remove(id);
                            msgBox.append(id+": usunięte!");
                            new PrepareClientList().start();
                        }
                    }else{
                        Set k= clientsMap.keySet();
                        Iterator itr=k.iterator();
                        while(itr.hasNext()){
                            String key=(String)itr.next();
                            if(!key.equalsIgnoreCase(ID)){
                                try{
                                    new DataOutputStream(((Socket) clientsMap.get(key)).getOutputStream()).writeUTF("< "+ID+" do wszystkich >"+i);
                                }catch(Exception ex){
                                    clientsMap.remove(key);
                                    msgBox.append(key+": usunięte!");
                                    new PrepareClientList().start();
                                }
                            }
                        }
                    }
                }
            }catch (Exception ex){
               ex.printStackTrace();
           }
        }
    }
    class PrepareClientList extends Thread{
        public void run(){
            try{
                String ids="";
                Set k= clientsMap.keySet();
                Iterator itr=k.iterator();
                while(itr.hasNext()){
                    String key=(String)itr.next();
                    ids+=key+",";
                }
                if(ids.length()!=0)
                    ids=ids.substring(0,ids.length()-1);
                itr=k.iterator();
                while(itr.hasNext()){
                    String key=(String)itr.next();
                    try{
                        new DataOutputStream(((Socket) clientsMap.get(key)).getOutputStream()).writeUTF(":;.,/="+ids);
                    }catch(Exception ex){
                        clientsMap.remove(key);
                        msgBox.append(key+": usunięte!");

                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new Server("CatchMe Server");
        frame.setVisible(true);
    }
}


