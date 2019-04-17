/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chat.app.service;

import com.cht.app.chat.Mensagem;
import com.cht.app.chat.Mensagem.Action;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdk.nashorn.tools.ShellFunctions.input;

/**
 *
 * @author Carlos Eduardo
 */
public class ServidorService {

    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String, ObjectOutputStream> mapOnlines = new HashMap<String, ObjectOutputStream>();

    public ServidorService() {

        try {
          
            serverSocket = new ServerSocket(666);
            
            
            System.out.println("Servidor Online...");

            while (true) {

                socket = serverSocket.accept();

                new Thread(new ListenerSocket(socket)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class ListenerSocket implements Runnable {

        private ObjectOutputStream output;
        private ObjectInputStream input;

        public ListenerSocket(Socket socket) {

            try {
                this.output = new ObjectOutputStream(socket.getOutputStream());
                this.input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        public void run() {

            Mensagem mensagem = null;

            try {
                while ((mensagem = (Mensagem) input.readObject()) != null) {

                    Action action = mensagem.getAction();

                    switch (action) {
                        case CONECTAR:
                            boolean isConnect = conectar(mensagem, output);
                            if (isConnect) {
                                mapOnlines.put(mensagem.getNome(), output);
                                enviarListaOnlines();
                            }
                            break;
                        case DESCONECTAR:
                            desconectar(mensagem, output);
                            enviarListaOnlines();
                            return;
                           
                        case ENVIAR_RESERVADO:
                            enviarReservado(mensagem);
                            break;
                        case ENVIAR_TODOS:
                            enviarParaTodos(mensagem);
                            break;
                        
                        default:
                            break;
                    }
                }
            } catch (IOException ex) {
                
                Mensagem cm = new Mensagem();
                cm.setNome(mensagem.getNome());
                
                desconectar(cm,output);
                
                enviarListaOnlines();
                System.out.println(mensagem.getNome() + " saiu!");
                
             } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        private boolean conectar(Mensagem mensagem, ObjectOutputStream output) {

            if (mapOnlines.size() == 0) {

                mensagem.setMensagem("OK");
                enviar(mensagem, output);

                return true;
            }

           
                if (mapOnlines.containsKey(mensagem.getNome())) {

                    mensagem.setMensagem("NO");
                    enviar(mensagem, output);

                    return false;

                } else {

                    mensagem.setMensagem("OK");
                    enviar(mensagem, output);
                    return true;
                }
            
        }

        private void desconectar(Mensagem mensagem, ObjectOutputStream output) {

            mapOnlines.remove(mensagem.getNome());

            mensagem.setMensagem(" Saiu!");

            mensagem.setAction(Action.ENVIAR_RESERVADO);

            enviarParaTodos(mensagem);

            System.out.println("Cliente " + mensagem.getNome() + " saiu do chat");
        }
        
        private void enviar(Mensagem mensagem, ObjectOutputStream output) {

            try {
                output.writeObject(mensagem);
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void enviarReservado(Mensagem mensagem) {

            for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {

                if (kv.getKey().equals(mensagem.getNomeReservado())) {
                    try {
                        kv.getValue().writeObject(mensagem);
                    } catch (IOException ex) {
                        Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        private void enviarParaTodos(Mensagem mensagem) {

            for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {

                if (!kv.getKey().equals(mensagem.getNome())) {
                    
                    mensagem.setAction(Action.ENVIAR_RESERVADO);
                    try {
                        kv.getValue().writeObject(mensagem);
                    } catch (IOException ex) {
                        Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
    private void enviarListaOnlines(){
        
        Set<String> setNomes = new HashSet<String>();
        for(Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()){
            
            setNomes.add(kv.getKey());
        }
        
        Mensagem mensagem = new Mensagem();
        mensagem.setAction(Action.USUARIOS_ONLINE);
        mensagem.setSetOnlines(setNomes);
       
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            
            mensagem.setNome(kv.getKey());
            
            try {
                kv.getValue().writeObject(mensagem);
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
        public String toString(){
        
        return "Socket: " + socket.toString() + "\nMensagem: " + serverSocket.toString() + "\nService: " + mapOnlines.toString();
    }
    
    public int hashCode(){
        
        int ret = 7;
        
        ret = 7 * socket.hashCode() + ret;
        ret = 7 * serverSocket.hashCode() + ret;
        ret = 7 * mapOnlines.hashCode() + ret;
        
        return ret;
    }
    
    public boolean equals(Object obj){
        
        if(obj == null) return false;
        if(obj == this) return true;
        
        if(obj instanceof ServidorService){
            
            ServidorService ss = (ServidorService) obj;
            if(ss.socket.equals(this.socket) && ss.serverSocket.equals(this.serverSocket) && ss.mapOnlines.equals(this.mapOnlines))
                return true;
        }
        return false;
    }
    
}
