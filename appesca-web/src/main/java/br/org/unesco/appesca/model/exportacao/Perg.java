package br.org.unesco.appesca.model.exportacao;

import java.util.HashMap;

public class Perg {

	private int ordem;
	private HashMap<Integer, Resp> mapRespostas = new HashMap<>();
	private int quantidade;
	
	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public HashMap<Integer, Resp> getMapRespostas() {
		return mapRespostas;
	}
	
	public void setMapRespostas(HashMap<Integer, Resp> mapRespostas) {
		this.mapRespostas = mapRespostas;
	}
	
	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public Resp getResposta(Integer ordem){
		if(mapRespostas.get(ordem)==null){
			Resp resp = new Resp();
			resp.setOrdem(ordem);
			mapRespostas.put(ordem, resp);
		}
		return mapRespostas.get(ordem);
	}

	public void increment(){
		quantidade++;
	}
}
