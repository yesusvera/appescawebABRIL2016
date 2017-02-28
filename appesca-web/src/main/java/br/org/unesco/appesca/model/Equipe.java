package br.org.unesco.appesca.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.org.unesco.appesca.util.ConstantesUNESCO;

@Entity
@Table(name = "tb_equipe", schema = ConstantesUNESCO.SCHEMA_APPESCA)
public class Equipe implements java.io.Serializable, Cloneable {
	
	private static final long serialVersionUID = -4258798850777666253L;
	
	private Integer id;
	private String nome;
	private String ultimosAvisos;
	private String regulamento;
	
	private Usuario coordenador;
	
	private List<Usuario> listaMembrosEquipe;
	
	private Date data;

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "nome", nullable = false, length = 100)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "ultimos_avisos")
	public String getUltimosAvisos() {
		return this.ultimosAvisos;
	}

	public void setUltimosAvisos(String ultimosAvisos) {
		this.ultimosAvisos = ultimosAvisos;
	}
	
	@Column(name = "regulamento")
	public String getRegulamento() {
		return this.regulamento;
	}

	public void setRegulamento(String regulamento) {
		this.regulamento = regulamento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = false, length = 19)
	public Date getData() {
		return this.data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_coordenador")
	public Usuario getCoordenador() {
		return coordenador;
	}

	public void setCoordenador(Usuario coordenador) {
		this.coordenador = coordenador;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "tb_membros_equipe", schema = ConstantesUNESCO.SCHEMA_APPESCA, joinColumns = { 
			@JoinColumn(name = "id_equipe", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "id_usuario", 
					nullable = false, updatable = false) })
	public List<Usuario> getListaMembrosEquipe() {
		return listaMembrosEquipe;
	}

	public void setListaMembrosEquipe(List<Usuario> listaMembrosEquipe) {
		this.listaMembrosEquipe = listaMembrosEquipe;
	}
}
