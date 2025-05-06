package com.drr.odontoway.core.service;

import java.util.List;

import com.drr.odontoway.core.dto.PerfilDTO;
import com.drr.odontoway.core.dto.UsuarioDTO;

public interface UsuarioService {
	
	UsuarioDTO iniciarSesion(String nombreUsuario, String clave);
	
	Boolean validarNumeroDocumentoExistente(String numeroDocumento);
	
	Boolean actualizarClaveUsuario(String nombreUsuario, String claveRegistrada);
	
	String nombreUsuarioAsignado(String primerNombre, String segundoNombre, String primerApellido, String segundoApellido);
	
	Boolean crearUsuario(UsuarioDTO usuarioParaCrear, String usuarioCreacion) throws Exception;
	
	List<UsuarioDTO> consultarTodosLosUsuario();
	
	List<UsuarioDTO> consultarUsuarioXFiltro(UsuarioDTO usuarioConFiltros);
	
	void activarODesactivarUsuario(UsuarioDTO usuarioSeleccionado);
	
	UsuarioDTO consultarUsuarioXId(Integer idUsuario);
	
	Boolean actualizarInformacionUsuario(UsuarioDTO usuarioParaModificar);
	
	List<UsuarioDTO> consultarUsuarioBusqueda(String busquedaUsuario);
	
	List<PerfilDTO> obtenerPerfilesUsuario(UsuarioDTO usuarioBusqueda);
	
	Boolean actualizarPerfilesDelUsuario(Integer idUsuario, List<PerfilDTO> lstPerfilesAsignados, String usuarioCreacion);
	
	void eliminarTodosLosPerfilesAsignadosUsuario(Integer idUsuario);

}
