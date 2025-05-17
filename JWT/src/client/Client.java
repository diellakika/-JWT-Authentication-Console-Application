package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            // Auth: Username dhe Password
            System.out.println(in.readLine());
            out.println(scanner.nextLine());

            System.out.println(in.readLine());
            out.println(scanner.nextLine());

            // JWT ose fail
            String response = in.readLine();
            System.out.println(response);

            // Nëse është auth me sukses, vazhdo me logout/exit
            if (response != null && response.startsWith("Authentication successful")) {
                System.out.println(in.readLine()); // "Type 'logout' to log out or 'exit' to quit."

                String command = scanner.nextLine();
                out.println(command); // Dergo komanden tek serveri

                System.out.println(in.readLine()); // Përgjigjja nga serveri për logout ose exit
            }

        } catch (IOException e) {
            System.out.println("Nuk u mor përgjigje nga serveri. Kontrollo nëse serveri është aktiv.");
        }
    }
}
