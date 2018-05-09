/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.senac.main;

import br.com.senac.view.CltView;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author thiag
 */
public class Client implements Runnable {

    private String ip;
    private String nome;
    private Socket clientSocket;
    private PrintWriter printWriter;
    private Runnable receiveMessage;
    private Thread threadClient;
    private String text;
    private int port;
    private CltView cltView;

    public Client(CltView cv) {
        this.cltView = cv;
    }

    public void sendText(String text) {
        this.printWriter.println(text);
    }

    @Override
    public void run() {

        try {
            this.clientSocket = new Socket(this.ip, this.port);
            this.printWriter = new PrintWriter(this.clientSocket.getOutputStream(), true); //Usando true não precisa usar o .flush();
        } catch (IOException ex) {
            this.printWriter.println("Erro ao conectar");
        }
        this.cltView.setViewText("Connecting port " + this.port + " - IP " + this.ip);

        iniciaReceber();
    }

    public PrintWriter getPw() {
        return this.printWriter;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void iniciaReceber() {
        this.receiveMessage = new ReceiveMessage(this.clientSocket, this.cltView, this.nome, this);
        this.threadClient = new Thread(this.receiveMessage);
        this.threadClient.start();
    }
}
