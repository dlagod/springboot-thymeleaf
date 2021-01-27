package es.gac;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import es.gac.controller.TablaController;
import es.gac.model.Tabla;
import es.gac.repository.TablaRepository;

/**
 * Clase que prueba los métodos del Controlador de Spring
 * @author diego
 *
 */
@RunWith(SpringRunner.class)
public class TablaControllerUnitTest {
	
	/**
	 * Variable miembro TablaController
	 */
	private static TablaController tablaController;
	
	/**
	 * Mock TablaRepository
	 */
    private static TablaRepository mockedTablaRepository;
    
    /**
     * Mock BindingResult
     */
    private static BindingResult mockedBindingResult;
    
    /**
     * Mock Model
     */
    private static Model mockedModel;

    /**
     * Método que se invoca antes de la ejecución de los métodos de prueba
     * para inicializar los mocks y ka clase del controlador
     */
    @BeforeClass
    public static void setUpTablaControllerInstance() {
        mockedTablaRepository = mock(TablaRepository.class);
        mockedBindingResult = mock(BindingResult.class);
        mockedModel = mock(Model.class);
        tablaController = new TablaController(mockedTablaRepository);
    }

    /**
     * Método de prueba que devuelve la vista del formulario de alta
     */
    @Test
    public void whenCalledshowAddForm_thenCorrect() {
        Tabla tabla = new Tabla(); 	      
        assertThat(tablaController.showAddForm(tabla, mockedModel)).isEqualTo("add-tabla");
    }
    
    /**
     * Método de prueba que devuelve la vista con la lista de todos los registros
     */
    @Test
    public void whenCalledAddAndValidEntity_thenCorrect() {
    	Tabla tabla = new Tabla();
        when(mockedBindingResult.hasErrors()).thenReturn(false);
        assertThat(tablaController.add(tabla, mockedBindingResult, mockedModel)).isEqualTo("list-tabla");
    }

    /**
     * Método de prueba que cuando se produce un error en el alta se retorna a la vista del formulario de alta
     */
    @Test
    public void whenCalledAddAndInValidEntity_thenCorrect() {
    	Tabla tabla = new Tabla();
        when(mockedBindingResult.hasErrors()).thenReturn(true);
        assertThat(tablaController.add(tabla, mockedBindingResult, mockedModel)).isEqualTo("add-tabla");
    }

    /**
     * Método de prueba que cuando se actualiza un registro con un identificador no válido lanza la 
     * excepción IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void whenCalledshowUpdateForm_thenIllegalArgumentException() {
        assertThat(tablaController.showUpdateForm(0, mockedModel)).isEqualTo("update-tabla");
    }
    
    /**
     * Método de prueba que se retorna a la lista de registro una vez se produce la actualización de uno de ellos
     */
    @Test
    public void whenCalledUpdateAndValidEntity_thenCorrect() {
    	Tabla tabla = new Tabla();
        when(mockedBindingResult.hasErrors()).thenReturn(false);
        assertThat(tablaController.update(1l, tabla, mockedBindingResult, mockedModel)).isEqualTo("list-tabla");
    }

    /**
     * Método de prueba que comprueba que cuando se produce un error en la actualización se retorna a la vista 
     * del formulario de actualización.
     */
    @Test
    public void whenCalledUpdateAndInValidEntity_thenCorrect() {
    	Tabla tabla = new Tabla();
        when(mockedBindingResult.hasErrors()).thenReturn(true);
        assertThat(tablaController.update(1l, tabla, mockedBindingResult, mockedModel)).isEqualTo("update-tabla");
    }
    
    /**
     * Método de prueba que cuando se elimina un registro con un identificador no válido lanza la 
     * excepción IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void whenCalledDelete_thenIllegalArgumentException() {
        assertThat(tablaController.delete(1l, mockedModel)).isEqualTo("list-tabla");
    }
    
    /**
     * Método de prueba verifica que al invicar al método list se devuelve la vista del listado de registros
     */
    @Test
    public void whenCalledList_thenCorrect() {
        assertThat(tablaController.list(mockedModel)).isEqualTo("list-tabla");
    }
}
