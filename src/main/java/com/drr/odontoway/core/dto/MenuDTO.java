package com.drr.odontoway.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO {
	
	private Integer idMenu;
	
	private String nombreMenu;
	
	private String rutaUrl;
	
	private String iconoMenu;
	
	private Integer menuPadre;
	
	private String idEstado;

}
