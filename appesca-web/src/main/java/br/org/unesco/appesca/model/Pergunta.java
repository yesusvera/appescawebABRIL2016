package br.org.unesco.appesca.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "tb_pergunta", schema="speed046_appesca")
public class Pergunta implements java.io.Serializable {

	private static final long serialVersionUID = -8806813412918825907L;
	
	private Integer id;
	private Boolean booleana;
	private Boolean respBooleana;
	private Integer ordem;
	private Questao questao;
	private List<Resposta> listaRespostas;

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "booleana")
	public Boolean getBooleana() {
		return this.booleana;
	}

	public void setBooleana(Boolean booleana) {
		this.booleana = booleana;
	}

	@Column(name = "resp_booleana")
	public Boolean getRespBooleana() {
		return this.respBooleana;
	}

	public void setRespBooleana(Boolean respBooleana) {
		this.respBooleana = respBooleana;
	}

	@Column(name = "ordem")
	public Integer getOrdem() {
		return this.ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}


	@ManyToOne
	@JoinColumn(name="id_questao")
	public Questao getQuestao() {
		return questao;
	}



	public void setQuestao(Questao questao) {
		this.questao = questao;
	}


	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "pergunta")
	@Fetch(FetchMode.SUBSELECT)
	public List<Resposta> getListaRespostas() {
		return listaRespostas;
	}



	public void setListaRespostas(List<Resposta> resposta) {
		this.listaRespostas = resposta;
	}


}
