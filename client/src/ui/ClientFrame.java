package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import socket.ClientSocket;

public class ClientFrame extends JFrame {

    private JButton connBtn;
    private JButton sendBtn;
    private JButton endBtn;
    private JTextField sendMessage;
    private JTextArea showMessage;

    private ClientSocket clientSocket = null;
    private ActionListener onClickListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == connBtn) {
                sendBtn.setEnabled(true);
                connBtn.setEnabled(false);
                clientSocket = new ClientSocket("127.0.0.1", 7779, showMessage);
                clientSocket.start();
            } else if (e.getSource() == sendBtn) {
                clientSocket.send(sendMessage.getText());
                sendMessage.setText("");
            } else {
                clientSocket.close();
            }
        }

    };

    protected void init() {
        connBtn = new JButton("连接服务器");
        endBtn = new JButton("关闭");
        sendBtn = new JButton("发送");

        connBtn.addActionListener(onClickListener);
        endBtn.addActionListener(onClickListener);
        sendBtn.addActionListener(onClickListener);
        sendBtn.setEnabled(false);

        sendMessage = new JTextField(16);
        showMessage = new JTextArea();

        JPanel connPanel = new JPanel();
        connPanel.add(connBtn);
        connPanel.add(endBtn);

        JPanel sendPanel = new JPanel();
        sendPanel.add(sendMessage);
        sendPanel.add(sendBtn);

        this.getContentPane().add(connPanel, BorderLayout.NORTH);
        this.getContentPane().add(new JScrollPane(showMessage),
                BorderLayout.CENTER);
        this.getContentPane().add(sendPanel, BorderLayout.SOUTH);
    }

    public ClientFrame() {
        init();

        this.setTitle("Node.js客户端");
        this.setSize(300, 250);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
