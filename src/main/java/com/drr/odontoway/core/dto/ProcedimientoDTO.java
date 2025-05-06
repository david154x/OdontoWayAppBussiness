package com.drr.odontoway.core.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcedimientoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer idProcedimiento;
	
	private String nombreProcedimiento;
	
	private String tipoProcedimiento;
	
	private String descripcionProcedimiento;
	
	private Integer cantidad;
	
	private String tiempoDuracion;
	
	private BigDecimal valorCosto;
	
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
