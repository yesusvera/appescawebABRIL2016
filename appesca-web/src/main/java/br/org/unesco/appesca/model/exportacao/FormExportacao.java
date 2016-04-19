package br.org.unesco.appesca.model.exportacao;

import java.util.HashMap;

public class FormExportacao {

	private int tipo;
	private HashMap<Integer, Q> mapQuestoes = new HashMap<>();

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public Q getQuestao(Integer ordem){
		if(mapQuestoes.get(ordem)==null){
			Q q = new Q();
			q.setOrdem(ordem);
			mapQuestoes.put(ordem, q);
		}
		return mapQuestoes.get(ordem);
	}

	public HashMap<Integer, Q> getMapQuestoes() {
		return mapQuestoes;
	}

	public void setMapQuestoes(HashMap<Integer, Q> mapQuestoes) {
		this.mapQuestoes = mapQuestoes;
	}
}
