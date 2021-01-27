package es.gac.repository;

import org.springframework.data.repository.CrudRepository;

import es.gac.model.Tabla;

/**
 * Interfaz que contiene las operaciones a realizar en la BBDD
 * @author diego
 *
 */
public interface TablaRepository extends CrudRepository<Tabla, Long> {

}
