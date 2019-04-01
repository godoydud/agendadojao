/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.agendadojao.controller;

import br.com.agendadojao.dao.ObrigacaoDao;
import br.com.agendadojao.model.Obrigacao;

/**
 *
 * @author renato
 */
public class ObrigacaoController {
    private Obrigacao obrigacao;
    private ObrigacaoDao dao;
    
    public ObrigacaoController(){
        dao = new ObrigacaoDao();
        novo();
    }
    
    public void novo(){
        setObrigacao(new Obrigacao());
    }
    
    public Obrigacao getObrigacao() {
        return obrigacao;
    }

    public void setObrigacao(Obrigacao obrigacao) {
        this.obrigacao = obrigacao;
    }
    
    public void salvar(){
        dao.create(obrigacao);
        novo();
    }
}
