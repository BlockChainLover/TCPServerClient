import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Mounica on 12/25/15.
 */


public class ServerHelper {

    private DataOutputStream outToClient;
    private BufferedReader inFromClient;
    private Socket connectionSocket;
    private LogManager logger = LogManager.getInstance();
    public String clientMachineName;

    public void initializeConnection(Socket connectionSocket) throws IOException {
        this.connectionSocket = connectionSocket;

        outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
    }

    public void closeConnection() throws IOException{
        outToClient.close();
        inFromClient.close();
        connectionSocket.close();
        logger.logINFO("Connection terminated with client: " + clientMachineName);
    }

    private String appendNewLineCharacter(String message){
        int length = message.length();
        if (message.charAt(length - 1) != '\n') {
            message = message + '\n';
        }
        return message;
    }

    public String readFromClient() throws IOException{
        String message = inFromClient.readLine();
        logger.logINFO("Read from Client " + clientMachineName + " : " + message );
        return message;
    }

    public void writeToClient(String message) throws IOException{
        logger.logINFO("Writing to Client " + clientMachineName + " : " + message);
        outToClient.writeBytes(appendNewLineCharacter(message));
    }
}
