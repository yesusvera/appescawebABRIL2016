package br.org.unesco.appesca.model;
// Generated Jan 7, 2016 4:12:20 AM by Hibernate Tools 4.3.1.Final

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_loc_residencia", schema="speed046_appesca")
public class LocResidencia implements java.io.Serializable {

	private static final long serialVersionUID = 3251866057864092677L;

	private Integer id;
	private Integer localResid;
	private Boolean residUnConserv;
	private int idFormulario;

	public LocResidencia() {
	}

	public LocResidencia(int idFormulario) {
		this.idFormulario = idFormulario;
	}

	public LocResidencia(Integer localResid, Boolean residUnConserv, int idFormulario) {
		this.localResid = localResid;
		this.residUnConserv = residUnConserv;
		this.idFormulario = idFormulario;
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

	@Column(name = "local_resid")
	public Integer getLocalResid() {
		return this.localResid;
	}

	public void setLocalResid(Integer localResid) {
		this.localResid = localResid;
	}

	@Column(name = "resid_un_conserv")
	public Boolean getResidUnConserv() {
		return this.residUnConserv;
	}

	public void setResidUnConserv(Boolean residUnConserv) {
		this.residUnConserv = residUnConserv;
	}

	@Column(name = "id_formulario", nullable = false)
	public int getIdFormulario() {
		return this.idFormulario;
	}

	public void setIdFormulario(int idFormulario) {
		this.idFormulario = idFormulario;
	}

}
