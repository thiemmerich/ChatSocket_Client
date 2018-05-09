/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.senac.main;

import br.com.senac.view.CltView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author thiag
 */
public class ReceiveMessage implements Runnable {

    private Socket socket;
    private CltView clientView;
    private String nome;
    private Client client;

    public ReceiveMessage(Socket skt, CltView cv, String nome, Client clt) {
        this.socket = skt;
        this.clientView = cv;
        this.nome = nome;
        this.client = clt;
    }

    @Override
    public synchronized void run() {

        try {

            InputStream in = this.socket.getInputStream();
            InputStreamReader inRead = new InputStreamReader(in);
            BufferedReader bRead = new BufferedReader(inRead);

            while (true) {

                String linha = bRead.readLine();
                String[] filteredLine = linha.split("//");

                /*
                 * Algoritmo que recebe a mensagem do servidor com a lista de clientes online;
                 */
                if (filteredLine[0].equalsIgnoreCase("listaonline")) {
                    this.clientView.setOnlineText(filteredLine);
                }
                /*
                 * Algoritmo que recebe a mensagem do servidor solicitando o nome e o envia;
                 */
                if (filteredLine[0].equalsIgnoreCase("qualseunome")) {
                    this.client.getPw().println(this.nome);
                    //this.client.sendText(this.nome);
                }
                if(filteredLine[0].equalsIgnoreCase("pvt")){
                    this.clientView.setViewText(filteredLine[1]);
                    JOptionPane.showMessageDialog(null, "New private message!");
                }
                if(filteredLine[0].equalsIgnoreCase("exitmessage")){
                    this.clientView.setViewText(filteredLine[1]);
                }
                /*
                 * Algoritmo que recebe todas as mensagens do servidor e mostra na tela para o usuario;
                 */
                if (!linha.equalsIgnoreCase("null")
                        && !filteredLine[0].equalsIgnoreCase("qualseunome")
                        && !filteredLine[0].equalsIgnoreCase("listaonline")
                        && !filteredLine[0].equalsIgnoreCase("exitmessage")
                        && !filteredLine[0].equalsIgnoreCase("pvt")) {
                    this.clientView.setViewText(linha);
                }
            }
        } catch (IOException ex) {
            System.out.println("Erro");
        }
    }
}
