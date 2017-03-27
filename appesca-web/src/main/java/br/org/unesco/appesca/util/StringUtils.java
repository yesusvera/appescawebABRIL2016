/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.unesco.appesca.util;

/**
 *
 * @author yesus
 */
import java.text.Normalizer;

public class StringUtils {

	/**
	 * Remove toda a acentuação da string substituindo por caracteres simples sem acento.
	 */
	public static String unaccent(String src) {
		return Normalizer
				.normalize(src, Normalizer.Form.NFD)
				.replaceAll("[^\\p{ASCII}]", "");
	}
	
}