package es.gac.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.gac.model.Tabla;
import es.gac.repository.TablaRepository;

/**
 * Clase controlador de Spring Web para la entidad Tabla
 * @author diego
 */
@Controller
@RequestMapping("/tabla")
public class TablaController {
	
	/**
	 * Variable que contiene las trazas en fichero
	 */
	private static final Logger LOGGER = Logger.getLogger(TablaController.class);
    
	/**
	 * Interfaz del repositorio que contiene las operaciones básicas a realizar en la BBDD 
	 */
	private TablaRepository tablaRepository;


	/**
	 * Constructor con parámetros
	 * @param tablaRepository TablaRepository
	 */
    @Autowired
    public TablaController(TablaRepository tablaRepository) {
        this.tablaRepository = tablaRepository;
    }
    
    /**
     * Método que permite mostrar el formulario de alta
     * @param tabla Entidad
     * @param model Model
     * @return add-tabla View
     */
    @GetMapping("/showAddForm")
    public String showAddForm(Tabla tabla, Model model) {
    	model.addAttribute("entity", tabla);
        return "add-tabla";
    }
    
    /**
     * Método que permite añadir un registro a la tabla
     * @param tabla Entidad
     * @param result BindingResult
     * @param model Model
     * @return View
     */
    @PostMapping("/add")
    public String add(@Valid Tabla tabla, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-tabla";
        }
        
        tablaRepository.save(tabla);
        model.addAttribute("rows", tablaRepository.findAll());
        return "list-tabla";
    }
    
    /**
     * Método que permite visualizar un registro a la tabla para la edición
     * @param id Identificador del registro
     * @param model Model
     * @return update-tabla View
     */
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Tabla tabla = tablaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        model.addAttribute("entity", tabla);
        return "update-tabla";
    }
    
    /**
     * Método que permite editar un registro a la tabla
     * @param id Identificador del registro
     * @param tabla Entidad
     * @param result BindingResult
     * @param model Model
     * @return update-tabla View
     */
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Tabla tabla, BindingResult result, Model model) {
        if (result.hasErrors()) {
        	tabla.setId(id);
            return "update-tabla";
        }
        
        tablaRepository.save(tabla);
        model.addAttribute("rows", tablaRepository.findAll());
        return "list-tabla";
    }
    
    /**
     * Método que permite eliminar un registro de la tabla
     * @param id Identificador del registro
     * @param model Model
     * @return index View
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        Tabla tabla = tablaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        tablaRepository.delete(tabla);
        model.addAttribute("rows", tablaRepository.findAll());
        return "list-tabla";
    }
    
    /**
     * Método que lista todos los registro de la tabla
     * @param model Model
     * @return index View
     */
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("rows", tablaRepository.findAll());
        return "list-tabla";
    }
}
