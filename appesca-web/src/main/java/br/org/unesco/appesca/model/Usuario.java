package br.org.unesco.appesca.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.org.unesco.appesca.enums.PerfilEnum;
import br.org.unesco.appesca.util.ConstantesUNESCO;

@Entity
@Table(name = "tb_usuario", schema = ConstantesUNESCO.SCHEMA_APPESCA)
public class Usuario implements java.io.Serializable {

	private static final long serialVersionUID = -7140175801442164346L;

	private Integer id;
	private String nome;
	private String email;
	private String endereco;
	private String login;
	private String senha;
	private PerfilEnum perfil;
	private byte[] imagem;
	private String uf;

	private List<Equipe> listaEquipes;

	private List<LocalizacaoUsuario> listaLocalizacoes;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "nome", length = 45)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "email", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "endereco", length = 45)
	public String getEndereco() {
		return this.endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	@NotNull
	@Size(min = 1, max = 20, message = "Digite o login.")
	@Column(name = "login", length = 20)
	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@NotNull
	@Size(min = 1, max = 20, message = "Digite a senha.")
	@Column(name = "senha", length = 20)
	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Column(name = "perfil")
	@Enumerated(EnumType.ORDINAL)
	public PerfilEnum getPerfil() {
		return this.perfil;
	}

	public void setPerfil(PerfilEnum perfil) {
		this.perfil = perfil;
	}

	@Column(name = "imagem")
	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	@Transient
	public StreamedContent getImageGraphics() {
		if (imagem != null) {
			return new DefaultStreamedContent(new ByteArrayInputStream(imagem));
		} else {
			return null;
		}
	}

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "listaMembrosEquipe", targetEntity=Equipe.class)
	public List<Equipe> getListaEquipes() {
		return listaEquipes;
	}

	public void setListaEquipes(List<Equipe> listaEquipes) {
		this.listaEquipes = listaEquipes;
	}

	@NotNull
	@Size(min = 2, max = 2)
	@Column(length = 2)
	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "usuario")
	@Fetch(FetchMode.SUBSELECT)
	public List<LocalizacaoUsuario> getListaLocalizacoes() {
		return listaLocalizacoes;
	}

	public void setListaLocalizacoes(List<LocalizacaoUsuario> listaLocalizacoes) {
		this.listaLocalizacoes = listaLocalizacoes;
	}
}
