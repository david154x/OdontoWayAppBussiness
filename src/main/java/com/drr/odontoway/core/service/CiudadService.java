package com.drr.odontoway.core.service;

import java.util.List;

import com.drr.odontoway.core.dto.CiudadDTO;

public interface CiudadService {
	
	Boolean consultarSiExisteCiudadXPais(Integer idPais, String nombreCiudad);
	
	Boolean crearCiudad(CiudadDTO ciudadParaCrear, String usuarioCreacion);
	
	List<CiudadDTO> consultarTodasLasCiudades();
	
	List<CiudadDTO> consultarCiudadXFiltros(CiudadDTO consultaFiltroCiudad);
	
	void activarODesactivarCiudad(CiudadDTO ciudadSeleccionada);
	
	Boolean modificarCiudad(CiudadDTO ciudadParaModificar);
	
	List<CiudadDTO> consultarCiudadesXPais(Integer idPais);
	
	CiudadDTO consultarCiudadXId(Integer idCiudad);
	
}
