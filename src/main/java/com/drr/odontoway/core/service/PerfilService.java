package com.drr.odontoway.core.service;

import java.util.List;

import com.drr.odontoway.core.dto.PerfilDTO;

public interface PerfilService {
	
	Boolean consultarSiPerfilXNombreExiste(String nombrePerfil);
	
	Boolean crearPerfil(PerfilDTO perfilParaCrear, String usuarioCreacion);
	
	List<PerfilDTO> consultarTodosLosPerfiles();
	
	List<PerfilDTO> consultarPerfilXFiltros(PerfilDTO consultaXFiltro);
	
	void activarODesactivarPerfil(PerfilDTO perfilSeleccionado);
	
	Boolean actualizarPerfil(PerfilDTO perfilParaModificar);
	
	PerfilDTO consultarPerfilXId(Integer idPerfil);
	
	List<PerfilDTO> consultarPerfilBusqueda(String busquedaPerfil);

}
