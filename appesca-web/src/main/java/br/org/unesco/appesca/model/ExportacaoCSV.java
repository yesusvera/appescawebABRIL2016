package br.org.unesco.appesca.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import br.org.unesco.appesca.util.ConstantesUNESCO;

@Entity
@Table(name = "tb_exportacao_csv", schema=ConstantesUNESCO.SCHEMA_APPESCA)
public class ExportacaoCSV {

	private Integer id;
	private String linhaCSV;
	private String idSincronizacao;
	private int idTipoFormulario;
	private int idFormulario;

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "linha_csv", nullable = false)
	public String getLinhaCSV() {
		return linhaCSV;
	}

	public void setLinhaCSV(String linhaCSV) {
		this.linhaCSV = linhaCSV;
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
		return idTipoFormulario;
	}

	public void setIdTipoFormulario(int idTipoFormulario) {
		this.idTipoFormulario = idTipoFormulario;
	}

	@Column(name = "id_formulario", nullable = false)
	public int getIdFormulario() {
		return idFormulario;
	}

	public void setIdFormulario(int idFormulario) {
		this.idFormulario = idFormulario;
	}
	
	
}
