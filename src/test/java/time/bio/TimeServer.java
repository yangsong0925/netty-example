package time.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Classname TimeServer
 * @Description TODO
 * @Date 2020/11/9 21:52
 * @Created by Administrator
 */
public class TimeServer {

    public static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
            Socket socket = null;
            while (true) {
                socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }
        } catch (IOException e) {
            System.out.println(" Time server fail... ");
        }
    }

}