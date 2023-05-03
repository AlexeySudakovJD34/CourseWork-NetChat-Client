package client;

import logger.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final Logger logger = Logger.getInstance();
    private final Scanner scanner = new Scanner(System.in);
    private String name = null;
    private PrintWriter outcome;
    private BufferedReader income;
    private Settings settings = new Settings();

    public boolean connectToServer() {
        String host = settings.getHostName();
        int port = settings.getPort();

        try {
            Socket clientSocket = new Socket(host, port);
            outcome = new PrintWriter(clientSocket.getOutputStream(), true);
            income = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            regService(income, outcome);

            Thread listener = new Thread(() -> {
                messageListener(income);
            });
            listener.setDaemon(true);
            listener.start();

            Thread sender = new Thread(() -> {
                messageSender(outcome);
            });
            sender.start();

        } catch (IOException exp) {
            String troubles = "Troubles with connection";
            System.out.println(troubles);
            logger.log(troubles, LogType.ERROR);
            return false;
        }
        return true;
    }

    public boolean regService(BufferedReader income, PrintWriter outcome) {
        try {
            String msgFromServer = income.readLine();
            System.out.println(msgFromServer);
            Scanner scanner = new Scanner(System.in);
            String nickName;
            do {
                nickName = scanner.nextLine();
                outcome.println(nickName);
                msgFromServer = income.readLine();
                System.out.println(msgFromServer);
            } while (msgFromServer.contains("This nickname is already taken, please chose another one"));
            name = nickName;
            String join = "You have joined SuChat";
            logger.log(join, LogType.INFO);
        } catch (IOException exp) {
            String trouble = "Cannot connect to server";
            System.out.println(trouble);
            logger.log(trouble, LogType.ERROR);
            return false;
        }
        return true;
    }

    public void messageListener(BufferedReader income) {
        while (true) {
            try {
                String message = income.readLine();
                if (message != null) {
                    System.out.println(message);
                    logger.log(message, LogType.MESSAGE);
                }
            } catch (IOException exp) {
                String troubles = "Troubles with connection";
                System.out.println(troubles);
                logger.log(troubles, LogType.ERROR);
            }
        }
    }

    public void messageSender(PrintWriter outcome) {
        String message;
        while (true) {
            message = scanner.nextLine();
            outcome.println(message);
            if (message.equalsIgnoreCase("/exit")) {
                String leave = "You have left SuChat";
                System.out.println(leave);
                logger.log(leave, LogType.INFO);
                System.exit(0);
            } else {
                String youSaid = name + " : " + message;
                System.out.println(youSaid);
                logger.log(youSaid, LogType.MESSAGE);
            }
        }
    }
}
