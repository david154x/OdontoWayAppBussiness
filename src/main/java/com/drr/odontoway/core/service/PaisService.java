package com.drr.odontoway.core.service;

import java.util.List;

import com.drr.odontoway.core.dto.PaisDTO;

public interface PaisService {
	
	Boolean consultarSiPaisExisteXNombre(String nombrePais);
	
	Boolean consultarSiPaisExisteXNombreCorto(String nombreCorto);
	
	Boolean crearPais(PaisDTO paisDTO, String nombreUsuario) throws Exception;
	
	List<PaisDTO> consultarTodosLosPaises();
	
	List<PaisDTO> consultarPaisXFiltros(PaisDTO consultaXFiltro);
	
	void activarODesactivarPais(PaisDTO paisSeleccionado);
	
	Boolean actualizarPais(PaisDTO paisNuevosValores);
	
	PaisDTO consultarPaisXId(Integer idPais);

}
