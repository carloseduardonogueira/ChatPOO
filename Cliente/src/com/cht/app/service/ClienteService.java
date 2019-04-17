/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cht.app.service;

import com.cht.app.chat.Mensagem;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos Eduardo
 */
public class ClienteService {


    private Socket socket;
    private ObjectOutputStream output;

    public Socket conectar(){
        
        try {
            this.socket = new Socket("localhost", 666);
            this.output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return socket;
    }
    
    public void enviar(Mensagem mensagem){
        
        try {
            output.writeObject(mensagem);
        } catch (IOException ex) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String toString(){
        
        return "Socket: " + socket.toString() + "\nOutput:" + output.toString();
    }
    
    @Override
    public boolean equals(Object obj){
        
        if(obj == null) return false;
        if(obj == this) return true;
        
        if(obj instanceof ClienteService){
            
            ClienteService cs = (ClienteService) obj;
            if(cs.socket.equals(this.socket) && cs.output.equals(this.output))
                return true;
            
        }
        return false;
    }
    
    @Override
    public int hashCode(){
        
        int ret = 7;
        
        ret = 7 * socket.hashCode() + ret;
        ret = 7 * output.hashCode() + ret;
        
        return ret;
    }
}
