package br.org.unesco.appesca.model;

import java.io.Serializable;

public enum DiasSemanaEnum implements Serializable{
    SEGUNDA(1, "ms/micons/green-dot.png"),
    TERCA(2, "ms/micons/green-dot.png"),
    QUARTA(3, "ms/micons/yellow-dot.png"),
    QUINTA(4, "ms/micons/orange-dot.png"),
    SEXTA(5, "ms/micons/red-dot.png"),
    SABADO(6,"marker_black.png"),
    DOMINGO(7, "marker_grey.png");

	private final int valor;
	private final String icone;
	
	private DiasSemanaEnum(int valor, String icone){
		this.valor = valor;
		this.icone = icone;
	}
	
	public int getValor(){
		return valor;
	}
	
	public static DiasSemanaEnum fromValue(int valor){
		return DiasSemanaEnum.values()[valor-1];
	}
	
	public String getIcone(){
		return icone;
	}
}