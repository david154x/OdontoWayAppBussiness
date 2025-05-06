package com.drr.odontoway.core.service;

import java.util.List;

import com.drr.odontoway.core.dto.MenuDTO;

public interface MenuService {
	
	List<MenuDTO> consultarMenuXUsuario(Integer idUsuario);
	
	String ubicacionMenu(Integer idMenu);
	
	List<MenuDTO> consultarTodosLosMenu();
	
	List<MenuDTO> consultarMenuXPerfil(Integer idPerfil);
	
	MenuDTO consultarMenuXId(Integer idMenu);
	
	void eliminarTodosLosMenusAsociadosAPerfil(Integer idPerfil);
	
	Boolean actualizarMenusXPerfil(Integer idPeril, List<MenuDTO> lstMenusAsignados, String usuarioCreacion);
	
}
