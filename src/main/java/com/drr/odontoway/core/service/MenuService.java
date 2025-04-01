package com.drr.odontoway.core.service;

import java.util.List;

import com.drr.odontoway.core.dto.MenuDTO;

public interface MenuService {
	
	List<MenuDTO> consultarMenuXUsuario(Integer idUsuario);

}
