/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cht.app.chat;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Carlos Eduardo
 */
public class Mensagem implements Serializable {
    
    private String nome;
    private String mensagem;
    private String nomeReservado;
    private Set<String> setOnlines = new HashSet<>();
    private Action action;
    
    public Mensagem(){
        
        this.mensagem = mensagem;
        this.nome = nome;
        this.nomeReservado = nomeReservado;
        this.action = action;
        this.setOnlines = setOnlines;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNomeReservado() {
        return nomeReservado;
    }

    public void setNomeReservado(String nomeReservado) {
        this.nomeReservado = nomeReservado;
    }

    public Set<String> getSetOnlines() {
        return setOnlines;
    }

    public void setSetOnlines(Set<String> setOnlines) {
        this.setOnlines = setOnlines;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
    
    
    
    public enum Action{
        
        CONECTAR, DESCONECTAR, ENVIAR_RESERVADO, ENVIAR_TODOS, USUARIOS_ONLINE;
    }
    
    
    public String toString(){
        
        return "Nome: " + nome + "\nMensage:" + mensagem + "\nNome Reservado:" + nomeReservado + "\nsetOnlines:" + setOnlines.toString() + "\nAction:" + action.toString();
    }
    
    public int hashCode(){
        
        int ret = 7;
        
        ret = 7 * nome.hashCode() + ret;
        ret = 7 * mensagem.hashCode() + ret;
        ret = 7 * nomeReservado.hashCode() + ret;
        ret = 7 * setOnlines.hashCode() + ret;
        ret = 7 * action.hashCode() + ret;
        
        return ret;
    }
    
    public boolean equals(Object obj){
        
        if(obj == null) return false;
        if(obj == this) return true;
        
        if(obj instanceof Mensagem){
            
            Mensagem m = (Mensagem) obj;
            
            if(!(m.nome.equals(this.nome)))
                return false;
            if(!(m.mensagem.equals(this.mensagem)))
                return false;
            if(!(m.nomeReservado.equals(this.nomeReservado)))
                return false;
            if(!(m.action.equals(this.action)))
                return false;
            if(!(m.setOnlines.equals(this.setOnlines)))
                return false;
        }
        return true;
    }
    
    public Mensagem(Mensagem m) throws Exception{
        
        if(m == null) throw new Exception("Nulo");
        this.mensagem = m.mensagem;
        this.nome = m.nome;
        this.nomeReservado = m.nomeReservado;
        this.action = m.action;
        this.setOnlines = m.setOnlines;
    }
    
    public Object clone(){
        
        Mensagem copia = null;
        
        try{
            copia = new Mensagem(this);
        }catch(Exception e)
        {}
        
        return copia;
    }
}
 