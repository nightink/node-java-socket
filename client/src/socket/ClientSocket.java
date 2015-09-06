package client.src.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextArea;

public class ClientSocket extends Thread {

  private String serverIP;
  private int port;
  private JTextArea showMessage;

  private Socket socket;
  private BufferedReader br = null;
  private BufferedWriter bw = null;

  public ClientSocket(String serverIP, int port, JTextArea showMessage) {
    this.serverIP = serverIP;
    this.port = port;
    this.showMessage = showMessage;
    init();
  }

  private void init() {
    try {
      socket = new Socket(serverIP, port);
      br = new BufferedReader(new InputStreamReader(socket
          .getInputStream()));
      bw = new BufferedWriter(new OutputStreamWriter(socket
          .getOutputStream()));

      showMessage.append("连接成功\n");
      showMessage.append(br.readLine() + "\n");
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 客户端发送数据
  public void send(String message) {
    try {
      bw.write(message);
      bw.flush();
      showMessage.append(message + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 负责接收服务器端发送的消息数据
  public void run() {
    System.out.println("run!");
    String message = null;
    do {
      try {
        System.out.println("run in");
        // readLine()函数是个阻塞函数
        message = br.readLine();
        System.out.println("run wei");
        if (message != null) {
          showMessage.append(message + "\n");
          showMessage
            .setCaretPosition(showMessage.getText().length());
        } else {
          System.out.println(message);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    } while (!message.equals("bye"));
  }

  public void close() {
    // cReadThead.stop();
    /*
     * try { if (objectOutputStream != null) { objectOutputStream.close(); }
     * if (objectInputStream != null) { objectInputStream.close(); } if
     * (socket != null) { socket.close(); } } catch (IOException e) {
     * e.printStackTrace(); socket = null; objectOutputStream = null;
     * objectInputStream = null; }
     */
  }
}
