package com.drr.odontoway.core.dto;

import java.io.Serializable;
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
public class UsuarioDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer idUsuario;
	
	private String tipoDocumentoUsuario;
	
	private String numeroDocumentoUsuario;
	
	private String nombreUsuario;
	
	private String passUsuario;
	
	private RolDTO rolAsignado;
	
    private String primerNombre;

    private String segundoNombre;
    
    private String primerApellido;

    private String segundoApellido;
    
    private String direccion;

    private String telefono;

    private String email;
    
    private PaisDTO paisAsignado;
    
    private CiudadDTO ciudadAsignada;
    
    private String genero;
    
    private Date fechaNacimiento;
	
	private String idEstado;
	
	private List<Date> lstFechasRango;
	
	private Date fechaCreacion;
	
	private Boolean cambiarClaveGenerica;
	
	public boolean isActivo() {
		return "Activo".equalsIgnoreCase(this.idEstado);
	}

	public void setActivo(boolean activo) {
		this.idEstado = activo ? "Activo" : "Inactivo";
	}
	
	public String getGeneroCompleto() {
	    if ( this.genero == null ) {
	        return "";
	    }
	    
	    switch ( this.genero.toUpperCase()) {
	        case "M":
	            return "Masculino";
	        case "F":
	            return "Femenino";
	        case "O":
	            return "Otro";
	        default:
	            return "No especificado";
	    }
	}

}
