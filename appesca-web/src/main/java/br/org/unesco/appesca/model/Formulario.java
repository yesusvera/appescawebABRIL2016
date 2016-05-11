package br.org.unesco.appesca.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "TB_FORMULARIO", schema="appesca", uniqueConstraints = @UniqueConstraint(columnNames = "nome") )
public class Formulario implements java.io.Serializable {

	private static final long serialVersionUID = 2165508619825487958L;

	private Integer id;
	private String idSincronizacao;
	private String nome;
	private int idTipoFormulario;
	private int idUsuario;
	private Date dataAplicacao;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private int situacao;
	
	private IdentEntrevistado entrevistado;
	
	private List<Questao> listaQuestoes;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}	

	public void setId(Integer id) {
		this.id = id;
	}

	//agora vai ser usada como histórico.
	@Column(name = "nome", nullable = false, unique=false)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	@Column(name = "id_sincronizacao", unique = true, nullable = true, length = 10)
	public String getIdSincronizacao() {
		return idSincronizacao;
	}

	public void setIdSincronizacao(String idSincronizacao) {
		this.idSincronizacao = idSincronizacao;
	}

	@Column(name = "id_tipo_formulario", nullable = false)
	public int getIdTipoFormulario() {
		return this.idTipoFormulario;
	}

	public void setIdTipoFormulario(int idTipoFormulario) {
		this.idTipoFormulario = idTipoFormulario;
	}

	@Column(name = "id_usuario", nullable = false)
	public int getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_aplicacao", nullable = false, length = 19)
	public Date getDataAplicacao() {
		return this.dataAplicacao;
	}

	public void setDataAplicacao(Date dataAplicacao) {
		this.dataAplicacao = dataAplicacao;
	}
	
	@Transient
	public String getData(){
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		return sd.format(getDataAplicacao());
	}
	@Transient
	public String getHora(){
		SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
		return sd.format(getDataAplicacao());
	}

	@OneToOne(mappedBy = "formulario")
	public IdentEntrevistado getEntrevistado() {
		return entrevistado;
	}

	public void setEntrevistado(IdentEntrevistado identEntrevistado) {
		this.entrevistado = identEntrevistado;
	}
	
	@OneToMany(fetch = FetchType.EAGER, cascade=javax.persistence.CascadeType.ALL ,mappedBy = "formulario")
	@Fetch(FetchMode.SUBSELECT)
	public List<Questao> getListaQuestoes() {
		return listaQuestoes;
	}

	public void setListaQuestoes(List<Questao> listaQuestoes) {
		this.listaQuestoes = listaQuestoes;
	}
	
	
	@Column(name = "latitude", nullable = true, precision=10, scale=8)
	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	@Column(name = "longitude", nullable = true, precision=11, scale=8)
	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	@Column(name = "situacao")
	public int getSituacao() {
		return situacao;
	}
	
	public void setSituacao(int situacao) {
		this.situacao = situacao;
	}
}
