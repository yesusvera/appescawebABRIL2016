package br.org.unesco.appesca.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_ident_entrevistado", schema="speed046_appesca")
public class IdentEntrevistado implements java.io.Serializable {

	private static final long serialVersionUID = -4061574933952985453L;
	
	private Integer id;
	private String nomeCompleto;
	private String apelido;
	private String comunidade;
	private Boolean possuiCertNasc;
	private Boolean possuiCartIdent;
	private Boolean possuiCpf;
	private Boolean possuiCartTrab;
	private Boolean possuiRgp;
	private Boolean possuiDap;
	private String possuiOutro;
	private Integer idade;
	private String sexo;
	private String telefone;
	private Integer acessoComunidade;
	private Integer distMunProx;
	private Integer estCivil;
	private Integer escolaridade;
	private Boolean responsUnFam;
	private Formulario formulario;
	private MunicipiosIbge municipio;

	public IdentEntrevistado() {
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

	@Column(name = "nome_completo", length = 100)
	public String getNomeCompleto() {
		return this.nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	@Column(name = "apelido", length = 45)
	public String getApelido() {
		return this.apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	@Column(name = "comunidade", length = 45)
	public String getComunidade() {
		return this.comunidade;
	}

	public void setComunidade(String comunidade) {
		this.comunidade = comunidade;
	}

	@Column(name = "possui_cert_nasc")
	public Boolean getPossuiCertNasc() {
		return this.possuiCertNasc;
	}

	public void setPossuiCertNasc(Boolean possuiCertNasc) {
		this.possuiCertNasc = possuiCertNasc;
	}

	@Column(name = "possui_cart_ident")
	public Boolean getPossuiCartIdent() {
		return this.possuiCartIdent;
	}

	public void setPossuiCartIdent(Boolean possuiCartIdent) {
		this.possuiCartIdent = possuiCartIdent;
	}

	@Column(name = "possui_cpf")
	public Boolean getPossuiCpf() {
		return this.possuiCpf;
	}

	public void setPossuiCpf(Boolean possuiCpf) {
		this.possuiCpf = possuiCpf;
	}

	@Column(name = "possui_cart_trab")
	public Boolean getPossuiCartTrab() {
		return this.possuiCartTrab;
	}

	public void setPossuiCartTrab(Boolean possuiCartTrab) {
		this.possuiCartTrab = possuiCartTrab;
	}

	@Column(name = "possui_RGP")
	public Boolean getPossuiRgp() {
		return this.possuiRgp;
	}

	public void setPossuiRgp(Boolean possuiRgp) {
		this.possuiRgp = possuiRgp;
	}

	@Column(name = "possui_DAP")
	public Boolean getPossuiDap() {
		return this.possuiDap;
	}

	public void setPossuiDap(Boolean possuiDap) {
		this.possuiDap = possuiDap;
	}

	@Column(name = "possui_outro", length = 45)
	public String getPossuiOutro() {
		return this.possuiOutro;
	}

	public void setPossuiOutro(String possuiOutro) {
		this.possuiOutro = possuiOutro;
	}

	@Column(name = "idade")
	public Integer getIdade() {
		return this.idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	@Column(name = "sexo", length = 1)
	public String getSexo() {
		return this.sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	@Column(name = "telefone", length = 15)
	public String getTelefone() {
		return this.telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Column(name = "acesso_comunidade")
	public Integer getAcessoComunidade() {
		return this.acessoComunidade;
	}

	public void setAcessoComunidade(Integer acessoComunidade) {
		this.acessoComunidade = acessoComunidade;
	}

	@Column(name = "dist_mun_prox")
	public Integer getDistMunProx() {
		return this.distMunProx;
	}

	public void setDistMunProx(Integer distMunProx) {
		this.distMunProx = distMunProx;
	}

	@Column(name = "est_civil")
	public Integer getEstCivil() {
		return this.estCivil;
	}

	public void setEstCivil(Integer estCivil) {
		this.estCivil = estCivil;
	}

	@Column(name = "escolaridade")
	public Integer getEscolaridade() {
		return this.escolaridade;
	}

	public void setEscolaridade(Integer escolaridade) {
		this.escolaridade = escolaridade;
	}

	@Column(name = "respons_un_fam")
	public Boolean getResponsUnFam() {
		return this.responsUnFam;
	}

	public void setResponsUnFam(Boolean responsUnFam) {
		this.responsUnFam = responsUnFam;
	}


	@OneToOne
	@JoinColumn(name="id_formulario")
	public Formulario getFormulario() {
		return this.formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}

	@OneToOne
	@JoinColumn(name="id_municipio")
	public MunicipiosIbge getMunicipio() {
		return municipio;
	}

	public void setMunicipio(MunicipiosIbge municipio) {
		this.municipio = municipio;
	}
}
