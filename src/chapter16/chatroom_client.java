package chapter16;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class chatroom_client {
    public static void main(String[] args) throws InterruptedException, IOException {
        //1.连接服务器
        Socket socket = new Socket("hadoop102",8989);

        //2.开启两个线程一个发送消息  一个接受消息
        Receive receive = new Receive(socket);
        receive.start();

        Send send = new Send(socket);
        send.start();

        send.join();

        socket.close();


    }
}

class Send extends Thread{
    private Socket socket;

    public Send(Socket socket) {
        super();
        this.socket = socket;
    }

    @Override
    public void run() {
        try{
            OutputStream outputStream = socket.getOutputStream();//获得输出流
            PrintStream printStream = new PrintStream(outputStream);
            Scanner input = new Scanner(System.in);

            while(true){
                System.out.println("请输入：");
                String s = input.nextLine();
                if("bye".equals(s)){
                    break;
                }
                printStream.println(s);
            }
            input.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class Receive extends Thread{
    private Socket socket;

    public Receive(Socket socket) {
        super();
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();//获得输入流

            Scanner scanner = new Scanner(inputStream);
            while(scanner.hasNextLine()){
                String s = scanner.nextLine();
                System.out.println(s);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
