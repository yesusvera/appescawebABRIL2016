package br.org.unesco.appesca.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TB_TIPO_FORMULARIO", schema="appesca")
public class TipoFormulario implements java.io.Serializable {

	private static final long serialVersionUID = -1968974358816603465L;

	private Integer id;
	private String nome;
	private String descricao;
	private Integer ordem;

	public TipoFormulario() {
	}

	public TipoFormulario(String nome) {
		this.nome = nome;
	}

	public TipoFormulario(String nome, String descricao, Integer ordem) {
		this.nome = nome;
		this.descricao = descricao;
		this.ordem = ordem;
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

	@Column(name = "nome", nullable = false, length = 100)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "descricao", length = 150)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "ordem")
	public Integer getOrdem() {
		return this.ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

}
