package br.org.unesco.appesca.model.exportacao;

public class Resp {
	
	private int ordem;
	private int quantidade;
	
	public int getOrdem() {
		return ordem;
	}
	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	public void increment(){
		quantidade++;
	}
}
