import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Mounica on 12/25/15.
 */
public class Server {

    private static LogManager logger = LogManager.getInstance();
    private ServerHelper helper;
    private storeManager store;


    public Server(){
        this.helper = new ServerHelper();
        this.store = new storeManager();
    }

    public void startClientSession(Socket connectionSocket) throws IOException{
        helper.initializeConnection(connectionSocket);
        String action;

        while(true){
            action = helper.readFromClient();

            if(action.equals("connect")){
                helper.clientMachineName = helper.readFromClient();
                logger.logINFO("Connected successfully with client: " + helper.clientMachineName);
            } else if(action.equals("exit")){
                helper.closeConnection();
                logger.logINFO("Closed connection with client: " + helper.clientMachineName);
                return;
            } else {
                processRequest(action);
            }
        }
    }

    private void processRequest(String action){
        try {
            int key = Integer.parseInt(helper.readFromClient());
            if (action.equals("put")) {
                int val = Integer.parseInt(helper.readFromClient());
                store.put(key, val);
                logger.logINFO("Put (" + key + "," + val + ")");
                return;
            } else if (action.equals("get")) {

                Integer val = store.get(key);
                if (val == null) {
                    helper.writeToClient("The key does not exist");
                    logger.logError("Recieved invalid get request with key: " + key);
                } else {
                    logger.logINFO("Fetched value " + val + "for key " + key);
                    helper.writeToClient(val + "");
                }
                return;
            } else if (action.equals("delete")) {
                try {
                    store.delete(key);
                } catch (Exception e) {
                    logger.logError("Key: '" + key + "' does not exist to delete");
                    helper.writeToClient("Key: '" + key + "' does not exist to delete");
                }
                logger.logINFO("Deleted key " + key);
                helper.writeToClient("Deleted key " + key);
                return;
            }
        } catch (IOException e){
            logger.logError(e.toString());
        }
    }

    public static void main(String args[]){
        int port = Integer.parseInt(args[0]);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket connectionSocket;
            Server serverInstance = new Server();
            while (true){
                System.out.println("TCP server is running. Waiting for connections...");

                connectionSocket = serverSocket.accept();
                System.out.println("Connection accepted from port: " + port);
                logger.logINFO("Connection accepted from port: " + port);
                serverInstance.startClientSession(connectionSocket);

            }

        } catch (Exception e){
            logger.logError(e.getMessage());
            logger.logError("Could not connect to " + port);
        }
    }


}
