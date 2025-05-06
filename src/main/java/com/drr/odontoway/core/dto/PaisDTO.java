package com.drr.odontoway.core.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.enterprise.context.Dependent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Dependent
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaisDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer idPais;
	
	private String nombrePais;
	
	private String nombreCorto;
	
	private String idEstado;
	
	private List<Date> lstFechasRango;
	
	private Date fechaCreacion;
	
	public boolean isActivo() {
		return "Activo".equalsIgnoreCase(this.idEstado);
	}

	public void setActivo(boolean activo) {
		this.idEstado = activo ? "Activo" : "Inactivo";
	}

}
