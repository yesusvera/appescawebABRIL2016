package br.org.unesco.appesca.model.exportacao;

import java.util.HashMap;

public class Q{

	private int ordem;
	private HashMap<Integer, Perg> mapPerg = new HashMap<>();
	private int quantidade;
	
	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}
	
	public HashMap<Integer, Perg> getMapPerg() {
		return mapPerg;
	}
	
	public void setMapPerg(HashMap<Integer, Perg> mapPerg) {
		this.mapPerg = mapPerg;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	public Perg getPergunta(Integer ordem){
		if(mapPerg.get(ordem)==null){
			Perg perg = new Perg();
			perg.setOrdem(ordem);
			mapPerg.put(ordem, perg);
		}
		return mapPerg.get(ordem);
	}

	public void increment(){
		quantidade++;
	}
}