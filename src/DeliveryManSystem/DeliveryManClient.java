package DeliveryManSystem;

import LockerSystem.Package;
import ObserverPattern.Observer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DeliveryManClient {

    BufferedReader in = null;
    PrintStream out = null;
    Socket socket = null;
    DeliveryMan deliveryMan;
    private boolean loggedIn=false;
    private ArrayList<Observer> observers = new ArrayList<>();

    /*
     *  -logIn: si connette al ManagerServer e invia id e password per autenticarsi, in
     *  caso positivo viene creato un deliveryman con quell'id e password.
     *  -sendList: invia al PickupPointServer la lista di pacchi in possesso dal corriere,
     *  riceve dal PickupPointServer l'id dei pacchi e i relativi codici delle box in cui
     *  inserirli, dopodichè notifica il DeliveryMan in modo da svuotare la lista dei pacchi.
     *  -updateList: si connette al ManagerServer per ricevere la lista dei pacchi.
     *  -addPackagesFromList: aggiunge i pacchi alla lista del DeliveryMan avendo in input un
     *  testo con packID e dimensioni.
     */

    public void logIn(String id, String password) throws IOException {
        if(authentication(id,password)){
            deliveryMan = new DeliveryMan(id,password);
            addObserver(deliveryMan);
            loggedIn=true;
            System.out.println("Successful login");
        }  else {
            System.err.println("Incorrect ID and password!");
        }
    }

    public void connectPickupPoint() throws IOException {
        socket = new Socket("localhost", 8000);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream(), true);
    }

    public void connectManager() throws IOException {
        socket = new Socket("localhost", 5000);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream(), true);
        send("DeliveryMan");
    }

    public void sendList() throws IOException {
        if(loggedIn) {
            connectPickupPoint();
            send(deliveryMan.getId()+"\t"+deliveryMan.getPassword());
            String response = readMessage();
            if(response.equals("authenticated")) {
                send(deliveryMan.packageListToString());
                System.out.println(readMessage());
            } else {
                System.err.println("Not authenticated!");
            }
                in.close();
                out.close();
                socket.close();
                notifyObservers();
        } else {
            System.err.println("You have to login!");
        }
    }

    public void updateList() throws IOException {
        if(loggedIn) {
            connectManager();
            send("updatelist");
            send(deliveryMan.getId());
            addPackagesFromList();
            in.close();
            out.close();
            socket.close();
        } else {
            System.err.println("You have to login!");
        }
    }

    public boolean authentication(String id, String password) throws IOException {
        connectManager();
        send("authentication");
        send(id+"\t"+password);
        String response =  readMessage();
        if(response.equals("authenticated")){
            return  true;
        }  else {
            return false;
        }
    }

    public void addPackagesFromList() throws IOException {
        while(!in.ready()){

        }
        while(in.ready()){
            StringTokenizer st = new StringTokenizer(in.readLine());
            while(st.hasMoreTokens()) {
                String id = st.nextToken();
                deliveryMan.addPackage(new Package(id, Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken())));
            }
        }
    }
    
    public void send(String text){
        out.print(text +"\n");
    }

    public String readMessage() throws IOException {
        String message = "";
        while(!in.ready()){
            
        }
        while(in.ready()){
            message+= in.readLine();
        }
        return message;
    }

    public void addObserver(Observer ob){
        observers.add(ob);
    }

    public void notifyObservers(){
        for(Observer ob : observers){
            ob.update();
        }
    }
}
