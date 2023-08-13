package chapter16;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class chatroom_server {
    static ArrayList<Socket> online =new ArrayList<Socket>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8989);//打开服务器接收端口

        while(true){
            Socket accpet = serverSocket.accept();//这个会阻塞

            online.add(accpet);

            //每个client开一个线程
            Messager ms = new Messager(accpet);
            ms.start();
        }

    }

    static class Messager extends Thread{
        private Socket socket;
        private String ip;

        public Messager(Socket socket) {
            super();
            this.socket = socket;
        }

        public void sendToOthers(String message) throws IOException {//用于转发所有信息
            for(Socket on: online){
                OutputStream out = on.getOutputStream();
                PrintStream printStream = new PrintStream(out);
                printStream.println(message);
            }
        }
        @Override
        public void run() {
            try {
                ip=socket.getInetAddress().getHostAddress();
                //需要封装一个方法给其他client发送消息
                sendToOthers(ip+"上线了！");
                InputStream input = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(input);
                BufferedReader buffer = new BufferedReader(reader);

                String str;
                while((str = buffer.readLine())!=null){
                    sendToOthers(ip+":"+str);
                }
                //一直发  跳出循环意味着 client掉线
                sendToOthers(ip+"下线了！");


            } catch (IOException e) {
                try{
                    sendToOthers(ip+"掉线了！");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }finally {
                online.remove(socket);
            }

        }

    }
}
