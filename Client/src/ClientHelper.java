import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Mounica on 12/28/15.
 */
public class ClientHelper {
    private Socket clientSocket;
    private DataOutputStream outToServer;
    private BufferedReader inFromServer;
    private LogManager logger = LogManager.getInstance();

    public ClientHelper(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        outToServer = new DataOutputStream(clientSocket.getOutputStream());
        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }


    public void closeConnection() throws IOException
    {
        outToServer.close();
        inFromServer.close();
        clientSocket.close();
    }

    private String appendNewLineCharacter(String message){
        int length = message.length();
        if(message.charAt(length - 1) != '\n'){
            message = message + '\n';
        }
        return message;
    }

    public String readFromServer() throws IOException{
        String val = inFromServer.readLine();
        logger.logINFO("Read from Server: " + val);
        return val;
    }

    public void writeToServer(String message) throws IOException{
        logger.logINFO("Writing to Server: " + message);
        outToServer.writeBytes(appendNewLineCharacter(message));
    }
}
