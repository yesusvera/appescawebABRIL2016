package br.org.unesco.appesca.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tb_membros_equipe", schema = "speed046_appesca")
public class MembrosEquipe implements java.io.Serializable {

	private static final long serialVersionUID = -472814831281223441L;
	
	private Integer id;
	private int idEquipe;
	private int idUsuario;
	private Date data;

	public MembrosEquipe() {
	}

	public MembrosEquipe(int idEquipe, int idUsuario, Date data) {
		this.idEquipe = idEquipe;
		this.idUsuario = idUsuario;
		this.data = data;
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

	@Column(name = "id_equipe", nullable = false)
	public int getIdEquipe() {
		return this.idEquipe;
	}

	public void setIdEquipe(int idEquipe) {
		this.idEquipe = idEquipe;
	}

	@Column(name = "id_usuario", nullable = false)
	public int getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = false, length = 19)
	public Date getData() {
		return this.data;
	}

	public void setData(Date data) {
		this.data = data;
	}

}
