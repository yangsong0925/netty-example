package bioExample.time;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @Classname TimeCilent
 * @Description TODO
 * @Date 2020/11/10 20:40
 * @Created by ys
 */
public class TimeClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", TimeServer.SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println("QUERY TIME ORDER");
            String resp = in.readLine();
            System.out.println("Now is :" + resp);
        } catch (Exception e) {
            System.out.println(" Time Client fail... ");
        }
    }

}
