/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.unesco.appesca.model;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author yesus
 */
public class FormFiltroExportacao {

    private int blocoIni = 1;
    private int blocoFim = 9;

    private int blocosQtdeQuestoes[];
    
    private int blocosQuestaoIni[] = {1, 1, 1, 1, 1, 1, 1, 1, 1};
    private int blocosQuestaoFim[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    
//    private int bloco1QuestaoIni;
//    private int bloco1QuestaoFim;
//
//    private int bloco2QuestaoIni;
//    private int bloco2QuestaoFim;
//
//    private int bloco3QuestaoIni;
//    private int bloco3QuestaoFim;
//
//    private int bloco4QuestaoIni;
//    private int bloco4QuestaoFim;
//
//    private int bloco5QuestaoIni;
//    private int bloco5QuestaoFim;
//
//    private int bloco6QuestaoIni;
//    private int bloco6QuestaoFim;
//
//    private int bloco7QuestaoIni;
//    private int bloco7QuestaoFim;
//
//    private int bloco8QuestaoIni;
//    private int bloco8QuestaoFim;
//
//    private int bloco9QuestaoIni;
//    private int bloco9QuestaoFim;

   
    public FormFiltroExportacao(int blocosQtdeQuestoes[]){
        this.blocosQtdeQuestoes = blocosQtdeQuestoes;
        this.blocosQuestaoFim = blocosQtdeQuestoes.clone();
    }
    
    
    public List<SelectItem> listaBlocosDe() {
        List<SelectItem> items = new ArrayList<>();
        for(int i=1; i<10; i++){
            items.add(new SelectItem(i,  "Bloco "+ i));
        }
        return items;
    }
    
     public List<SelectItem> listaBlocosAte() {
        List<SelectItem> items = new ArrayList<>();
        for(int i=blocoIni; i<10; i++){
            items.add(new SelectItem(i, "Bloco "+ i));
        }
        return items;
    }
     
     public List<SelectItem> listaQuestoesDe(int bloco){
        List<SelectItem> items = new ArrayList<>();
        for(int i=1; i<=blocosQtdeQuestoes[bloco-1]; i++){
            items.add(new SelectItem(i, "Questão "+ i));
        }
        return items;
     }
     
     
     public List<SelectItem> listaQuestoesAte(int bloco){
        List<SelectItem> items = new ArrayList<>();
        for(int i=getBlocosQuestaoIni()[bloco-1]; i<=blocosQtdeQuestoes[bloco-1]; i++){
            items.add(new SelectItem(i, "Questão "+ i));
        }
        return items;
     }
     
     public boolean renderizaBloco(int bloco){
        return bloco>=blocoIni && bloco<= blocoFim;
     }
     
    /**
     * @return the blocoIni
     */
    public int getBlocoIni() {
        return blocoIni;
    }

    /**
     * @param blocoIni the blocoIni to set
     */
    public void setBlocoIni(int blocoIni) {
        this.blocoIni = blocoIni;
    }

    /**
     * @return the blocoFim
     */
    public int getBlocoFim() {
        return blocoFim;
    }

    /**
     * @param blocoFim the blocoFim to set
     */
    public void setBlocoFim(int blocoFim) {
        this.blocoFim = blocoFim;
    }

    /**
     * @return the bloco1QuestaoIni
     */
    public int getBloco1QuestaoIni() {
        return getBlocosQuestaoIni()[0];
    }

    /**
     * @param bloco1QuestaoIni the bloco1QuestaoIni to set
     */
    public void setBloco1QuestaoIni(int bloco1QuestaoIni) {
        this.blocosQuestaoIni[0] = bloco1QuestaoIni;
    }

    /**
     * @return the bloco1QuestaoFim
     */
    public int getBloco1QuestaoFim() {
        return getBlocosQuestaoFim()[0];
    }

    /**
     * @param bloco1QuestaoFim the bloco1QuestaoFim to set
     */
    public void setBloco1QuestaoFim(int bloco1QuestaoFim) {
        this.blocosQuestaoFim[0] = bloco1QuestaoFim;
    }

    /**
     * @return the bloco2QuestaoIni
     */
    public int getBloco2QuestaoIni() {
        return getBlocosQuestaoIni()[1];
    }

    /**
     * @param bloco2QuestaoIni the bloco2QuestaoIni to set
     */
    public void setBloco2QuestaoIni(int bloco2QuestaoIni) {
        this.blocosQuestaoIni[1] = bloco2QuestaoIni;
    }

    /**
     * @return the bloco2QuestaoFim
     */
    public int getBloco2QuestaoFim() {
        return getBlocosQuestaoFim()[1];
    }

    /**
     * @param bloco2QuestaoFim the bloco2QuestaoFim to set
     */
    public void setBloco2QuestaoFim(int bloco2QuestaoFim) {
        this.blocosQuestaoFim[1] = bloco2QuestaoFim;
    }

    /**
     * @return the bloco3QuestaoIni
     */
    public int getBloco3QuestaoIni() {
        return getBlocosQuestaoIni()[2];
    }

    /**
     * @param bloco3QuestaoIni the bloco3QuestaoIni to set
     */
    public void setBloco3QuestaoIni(int bloco3QuestaoIni) {
        this.blocosQuestaoIni[2] = bloco3QuestaoIni;
    }

    /**
     * @return the bloco3QuestaoFim
     */
    public int getBloco3QuestaoFim() {
        return getBlocosQuestaoFim()[2];
    }

    /**
     * @param bloco3QuestaoFim the bloco3QuestaoFim to set
     */
    public void setBloco3QuestaoFim(int bloco3QuestaoFim) {
        this.blocosQuestaoFim[2] = bloco3QuestaoFim;
    }

    /**
     * @return the bloco4QuestaoIni
     */
    public int getBloco4QuestaoIni() {
        return getBlocosQuestaoIni()[3];
    }

    /**
     * @param bloco4QuestaoIni the bloco4QuestaoIni to set
     */
    public void setBloco4QuestaoIni(int bloco4QuestaoIni) {
        this.blocosQuestaoIni[3] = bloco4QuestaoIni;
    }

    /**
     * @return the bloco4QuestaoFim
     */
    public int getBloco4QuestaoFim() {
        return getBlocosQuestaoFim()[3];
    }

    /**
     * @param bloco4QuestaoFim the bloco4QuestaoFim to set
     */
    public void setBloco4QuestaoFim(int bloco4QuestaoFim) {
        this.blocosQuestaoFim[3] = bloco4QuestaoFim;
    }

    /**
     * @return the bloco5QuestaoIni
     */
    public int getBloco5QuestaoIni() {
        return getBlocosQuestaoIni()[4];
    }

    /**
     * @param bloco5QuestaoIni the bloco5QuestaoIni to set
     */
    public void setBloco5QuestaoIni(int bloco5QuestaoIni) {
        this.blocosQuestaoIni[4] = bloco5QuestaoIni;
    }

    /**
     * @return the bloco5QuestaoFim
     */
    public int getBloco5QuestaoFim() {
        return getBlocosQuestaoFim()[4];
    }

    /**
     * @param bloco5QuestaoFim the bloco5QuestaoFim to set
     */
    public void setBloco5QuestaoFim(int bloco5QuestaoFim) {
        this.blocosQuestaoFim[4] = bloco5QuestaoFim;
    }

    /**
     * @return the bloco6QuestaoIni
     */
    public int getBloco6QuestaoIni() {
        return getBlocosQuestaoIni()[5];
    }

    /**
     * @param bloco6QuestaoIni the bloco6QuestaoIni to set
     */
    public void setBloco6QuestaoIni(int bloco6QuestaoIni) {
        this.blocosQuestaoIni[5] = bloco6QuestaoIni;
    }

    /**
     * @return the bloco6QuestaoFim
     */
    public int getBloco6QuestaoFim() {
        return getBlocosQuestaoFim()[5];
    }

    /**
     * @param bloco6QuestaoFim the bloco6QuestaoFim to set
     */
    public void setBloco6QuestaoFim(int bloco6QuestaoFim) {
        this.blocosQuestaoFim[5] = bloco6QuestaoFim;
    }

    /**
     * @return the bloco7QuestaoIni
     */
    public int getBloco7QuestaoIni() {
        return getBlocosQuestaoIni()[6];
    }

    /**
     * @param bloco7QuestaoIni the bloco7QuestaoIni to set
     */
    public void setBloco7QuestaoIni(int bloco7QuestaoIni) {
        this.blocosQuestaoIni[6] = bloco7QuestaoIni;
    }

    /**
     * @return the bloco7QuestaoFim
     */
    public int getBloco7QuestaoFim() {
        return getBlocosQuestaoFim()[6];
    }

    /**
     * @param bloco7QuestaoFim the bloco7QuestaoFim to set
     */
    public void setBloco7QuestaoFim(int bloco7QuestaoFim) {
        this.blocosQuestaoFim[6] = bloco7QuestaoFim;
    }

    /**
     * @return the bloco8QuestaoIni
     */
    public int getBloco8QuestaoIni() {
        return getBlocosQuestaoIni()[7];
    }

    /**
     * @param bloco8QuestaoIni the bloco8QuestaoIni to set
     */
    public void setBloco8QuestaoIni(int bloco8QuestaoIni) {
        this.blocosQuestaoIni[7] = bloco8QuestaoIni;
    }

    /**
     * @return the bloco8QuestaoFim
     */
    public int getBloco8QuestaoFim() {
        return getBlocosQuestaoFim()[7];
    }

    /**
     * @param bloco8QuestaoFim the bloco8QuestaoFim to set
     */
    public void setBloco8QuestaoFim(int bloco8QuestaoFim) {
        this.blocosQuestaoFim[7] = bloco8QuestaoFim;
    }

    /**
     * @return the bloco9QuestaoIni
     */
    public int getBloco9QuestaoIni() {
        return getBlocosQuestaoIni()[8];
    }

    /**
     * @param bloco9QuestaoIni the bloco9QuestaoIni to set
     */
    public void setBloco9QuestaoIni(int bloco9QuestaoIni) {
        this.blocosQuestaoIni[8] = bloco9QuestaoIni;
    }

    /**
     * @return the bloco9QuestaoFim
     */
    public int getBloco9QuestaoFim() {
        return getBlocosQuestaoFim()[8];
    }

    /**
     * @param bloco9QuestaoFim the bloco9QuestaoFim to set
     */
    public void setBloco9QuestaoFim(int bloco9QuestaoFim) {
        this.blocosQuestaoFim[8] = bloco9QuestaoFim;
    }

    /**
     * @return the blocosQuestaoIni
     */
    public int[] getBlocosQuestaoIni() {
        return blocosQuestaoIni;
    }

    /**
     * @return the blocosQuestaoFim
     */
    public int[] getBlocosQuestaoFim() {
        return blocosQuestaoFim;
    }

}
