/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.unesco.appesca.model;

/**
 *
 * @author yesus
 */
public class FormFiltroPesquisa {
    private String uf;
    private String municipio;
    private String comunidade;
    private String pesquisador;
    private String sexo;
    private String idadeEntrevistado;
    private String intervaloBlocoQuestoes;
    private String situacao;
    private boolean pescadorB2Q1Ra;
    private boolean pescadorCamCarB2Rb;

    /**
     * @return the uf
     */
    public String getUf() {
        return uf;
    }

    /**
     * @param uf the uf to set
     */
    public void setUf(String uf) {
        this.uf = uf;
    }

    /**
     * @return the municipio
     */
    public String getMunicipio() {
        return municipio;
    }

    /**
     * @param municipio the municipio to set
     */
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    /**
     * @return the comunidade
     */
    public String getComunidade() {
        return comunidade;
    }

    /**
     * @param comunidade the comunidade to set
     */
    public void setComunidade(String comunidade) {
        this.comunidade = comunidade;
    }

    /**
     * @return the pesquisador
     */
    public String getPesquisador() {
        return pesquisador;
    }

    /**
     * @param pesquisador the pesquisador to set
     */
    public void setPesquisador(String pesquisador) {
        this.pesquisador = pesquisador;
    }

    /**
     * @return the sexo
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * @param sexo the sexo to set
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     * @return the idadeEntrevistado
     */
    public String getIdadeEntrevistado() {
        return idadeEntrevistado;
    }

    /**
     * @param idadeEntrevistado the idadeEntrevistado to set
     */
    public void setIdadeEntrevistado(String idadeEntrevistado) {
        this.idadeEntrevistado = idadeEntrevistado;
    }

    /**
     * @return the intervaloBlocoQuestoes
     */
    public String getIntervaloBlocoQuestoes() {
        return intervaloBlocoQuestoes;
    }

    /**
     * @param intervaloBlocoQuestoes the intervaloBlocoQuestoes to set
     */
    public void setIntervaloBlocoQuestoes(String intervaloBlocoQuestoes) {
        this.intervaloBlocoQuestoes = intervaloBlocoQuestoes;
    }

    /**
     * @return the pescadorB2Q1Ra
     */
    public boolean isPescadorB2Q1Ra() {
        return pescadorB2Q1Ra;
    }

    /**
     * @param pescadorB2Q1Ra the pescadorB2Q1Ra to set
     */
    public void setPescadorB2Q1Ra(boolean pescadorB2Q1Ra) {
        this.pescadorB2Q1Ra = pescadorB2Q1Ra;
    }

    /**
     * @return the pescadorCamCarB2Rb
     */
    public boolean isPescadorCamCarB2Rb() {
        return pescadorCamCarB2Rb;
    }

    /**
     * @param pescadorCamCarB2Rb the pescadorCamCarB2Rb to set
     */
    public void setPescadorCamCarB2Rb(boolean pescadorCamCarB2Rb) {
        this.pescadorCamCarB2Rb = pescadorCamCarB2Rb;
    }

    /**
     * @return the situacao
     */
    public String getSituacao() {
        return situacao;
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
    
}
