package es.gac.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase que encapsula la entidad del modelo de datos.
 * @author diego
 */
@Entity
@Table(name = "Tabla")
public class Tabla {

	/**
	 * Identificador de la tabla
	 */
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * Campo texto
	 */
	@Column
	private String texto;

	/**
	 * Fecha de Actualización
	 */
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyy")
	private java.util.Date updated;

	/**
	 * Método que devuelve el id
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Método que establece el id
	 * @param id long
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Método que devuelve el texto
	 * @return texto
	 */
	public String getTexto() {
		return texto;
	}

	/**
	 * Método que estalece el texto
	 * @param texto String
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}

	/**
	 * Método que devuelve el updated
	 * @return updated
	 */
	public java.util.Date getUpdated() {
		return updated;
	}

	/**
	 * Método que establece el updated
	 * @param updated Dated
	 */
	public void setUpdated(java.util.Date updated) {
		this.updated = updated;
	}

}
