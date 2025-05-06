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
	
	private String nombreDocumento;
	
	private String iconoMenu;
	
	private Integer moduloMenu;
	
	private Integer subMenu;
	
	private String nombreClaseView;
	
	private Integer numeroItem;
	
	private String idEstado;

}
