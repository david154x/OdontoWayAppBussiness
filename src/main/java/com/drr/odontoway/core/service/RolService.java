package com.drr.odontoway.core.service;

import java.util.List;

import com.drr.odontoway.core.dto.RolDTO;

public interface RolService {
	
	Boolean consultarSiRolExisteXNombre(String nombreRol);
	
	Boolean crearNuevoRol(String nombreRol, String descripcionRol, String usuarioCreacion);
	
	List<RolDTO> consultarTodosLosRoles();
	
	List<RolDTO> consultarRolXFiltros(RolDTO consultaXFiltro);
	
	void modificarEstadoRol(RolDTO rolParaModificar);
	
	Boolean actualizarRol(RolDTO rolParaModificar);
	
	RolDTO consultarRolXId(Integer idRol);
	
}
