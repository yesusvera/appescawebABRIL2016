package br.org.unesco.appesca.model.exp;

public class RowExportCVS {

    public String bloco;
    private Integer numeroBloco;
    public String questao;
    private Integer numeroQuestao;
    public String descricao;
    public String tipoVariavel;
    public String codigoExportacao;
    public String cod1AppescaAndroid;
    public String cod2AppescaAndroid;

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        if (bloco != null) {
            bloco = bloco.trim();
        }
        this.bloco = bloco;

        try {
            this.setNumeroBloco(Integer.valueOf(this.bloco));
        } catch (NumberFormatException e) {
        }
    }

    public String getQuestao() {
        return questao;
    }

    public void setQuestao(String questao) {
        if (questao != null) {
            questao = questao.trim();
        }

        this.questao = questao;

        try {
            if (this.questao.contains(".")) {
                String[] questaoSplit = this.questao.split("\\.");
                if (questaoSplit != null && questaoSplit.length > 1) {
                    setNumeroQuestao(new Integer(questaoSplit[0]));
                }
            }else{
                setNumeroQuestao(new Integer(this.questao));
            }
        } catch (Exception e) {

        }
    }
    
    

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoVariavel() {
        return tipoVariavel;
    }

    public void setTipoVariavel(String tipoVariavel) {
        this.tipoVariavel = tipoVariavel;
    }

    public String getCodigoExportacao() {
        return codigoExportacao;
    }

    public void setCodigoExportacao(String codigoExportacao) {
        this.codigoExportacao = codigoExportacao;
    }

    public String getCod1AppescaAndroid() {
        return cod1AppescaAndroid;
    }

    public void setCod1AppescaAndroid(String cod1AppescaAndroid) {
        this.cod1AppescaAndroid = cod1AppescaAndroid;
    }

    public String getCod2AppescaAndroid() {
        return cod2AppescaAndroid;
    }

    public void setCod2AppescaAndroid(String cod2AppescaAndroid) {
        this.cod2AppescaAndroid = cod2AppescaAndroid;
    }

    /**
     * @return the numeroBloco
     */
    public Integer getNumeroBloco() {
        return numeroBloco;
    }

    /**
     * @param numeroBloco the numeroBloco to set
     */
    public void setNumeroBloco(Integer numeroBloco) {
        this.numeroBloco = numeroBloco;
    }

    /**
     * @return the numeroQuestao
     */
    public Integer getNumeroQuestao() {
        return numeroQuestao;
    }

    /**
     * @param numeroQuestao the numeroQuestao to set
     */
    public void setNumeroQuestao(Integer numeroQuestao) {
        this.numeroQuestao = numeroQuestao;
    }
}
