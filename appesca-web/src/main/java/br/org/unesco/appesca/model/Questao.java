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
@Table(name = "TB_QUESTAO", schema="appesca")
public class Questao implements java.io.Serializable {

	private static final long serialVersionUID = -7214392109110721089L;

	private Integer id;
	private String titulo;
	private Integer ordem;
	private Formulario formulario;
	private List<Pergunta> listaPerguntas;

	public Questao() {
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "titulo", length = 100)
	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	@Column(name = "ordem")
	public Integer getOrdem() {
		return this.ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_formulario", insertable=true, updatable=true)
	public Formulario getFormulario() {
		return this.formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "questao")
	@Fetch(FetchMode.SUBSELECT)
	public List<Pergunta> getListaPerguntas() {
		return listaPerguntas;
	}


	public void setListaPerguntas(List<Pergunta> pergunta) {
		this.listaPerguntas = pergunta;
	}

}
