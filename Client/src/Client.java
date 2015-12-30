import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.StringTokenizer;


/**
 * Created by Mounica on 12/28/15.
 */
public class Client {

    public LogManager logger = LogManager.getInstance();
    private ClientHelper helper;
    private BufferedReader inFromUser;

    public Client(Socket clientSocket, BufferedReader inFromUser){
        try {
            this.helper = new ClientHelper(clientSocket);
            this.inFromUser = inFromUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startSession(String hostname) throws IOException {
        helper.writeToServer("connect");
        helper.writeToServer(hostname);

        while(true){
            StringTokenizer tokenizer = getUserActions();
            sendToServer(tokenizer);
        }
    }

    public StringTokenizer getUserActions(){
        String inputs = null;
        try {
            inputs = inFromUser.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringTokenizer tokenizer = new StringTokenizer(inputs, " ");
        return tokenizer;
    }

    private void sendToServer(StringTokenizer tokenizer) throws IOException {
        String action, key;

        while (tokenizer.hasMoreTokens()){
            action = tokenizer.nextToken();
            key = tokenizer.nextToken();
            helper.writeToServer(action);
            helper.writeToServer(key);

            if(action.equals("put")){
                String val = tokenizer.nextToken();
                helper.writeToServer(val);
            } else if(action.equals("get")){
                String val = helper.readFromServer();
                System.out.println("Read from Server: (" + key + ", " + val + ")");
            } else if(action.equals("delete")){
                String ack = helper.readFromServer();
                System.out.println("Read from server: " + ack);
            }
        }
    }

    public void closeConnection(){
        try {
            helper.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket();

        int TIMEOUT = 1000;


        try {
            clientSocket.connect(new InetSocketAddress(hostName, portNumber), TIMEOUT);
            clientSocket.setSoTimeout(TIMEOUT);
            Client clientInstance = new Client(clientSocket, inFromUser);
            clientInstance.startSession(hostName);
            clientInstance.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
