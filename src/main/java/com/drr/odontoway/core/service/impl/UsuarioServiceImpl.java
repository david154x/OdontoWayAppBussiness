package com.drr.odontoway.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.mindrot.jbcrypt.BCrypt;

import com.drr.odontoway.core.dto.CiudadDTO;
import com.drr.odontoway.core.dto.PaisDTO;
import com.drr.odontoway.core.dto.PerfilDTO;
import com.drr.odontoway.core.dto.RolDTO;
import com.drr.odontoway.core.dto.UsuarioDTO;
import com.drr.odontoway.core.service.CiudadService;
import com.drr.odontoway.core.service.PaisService;
import com.drr.odontoway.core.service.PerfilService;
import com.drr.odontoway.core.service.RolService;
import com.drr.odontoway.core.service.UsuarioService;
import com.drr.odontoway.entity.CiudadEntity;
import com.drr.odontoway.entity.PaisEntity;
import com.drr.odontoway.entity.PerfilUsuarioEntity;
import com.drr.odontoway.entity.RolEntity;
import com.drr.odontoway.entity.UsuarioEntity;
import com.drr.odontoway.repository.CiudadRepository;
import com.drr.odontoway.repository.PaisRepository;
import com.drr.odontoway.repository.PerfilRepository;
import com.drr.odontoway.repository.PerfilUsuarioRepository;
import com.drr.odontoway.repository.RolRepository;
import com.drr.odontoway.repository.UsuarioRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {
	
	@Inject
	private UsuarioRepository usuarioRepository;
	
	@Inject
	private PerfilRepository perfilRepository;
	
	@Inject
	private PerfilUsuarioRepository perfilUsuarioRepository;
	
	@Inject
	private PerfilService perfilService;
	
	@Inject
	private RolRepository rolRepository;
	
	@Inject
	private PaisRepository paisRepository;
	
	@Inject
	private CiudadRepository ciudadRepository;
	
	@Inject
	private RolService rolService;
	
	@Inject
	private PaisService paisService;
	
	@Inject
	private CiudadService ciudadService;
	
	@Override
	public UsuarioDTO iniciarSesion(String nombreUsuario, String clave) {
		try {
			
			UsuarioEntity usuarioXNombre = this.usuarioRepository.consultarUsarioXNombre(nombreUsuario);
			
			if ( usuarioXNombre == null )
				return null;
			
			// COMPROBAMOS QUE LA CONTRASEÑA SEA IGUAL EN LA BASE DE DATOS
			if ( !BCrypt.checkpw(clave, usuarioXNombre.getPass()) )
				return null;
			
			RolDTO rolAsignado = this.rolService.consultarRolXId(usuarioXNombre.getRolEntity().getIdRol());
			
			UsuarioDTO usuarioAutenticadoDTO = UsuarioDTO
					.builder()
					.idUsuario(usuarioXNombre.getIdUsuario())
					.nombreUsuario(usuarioXNombre.getNombre())
					.passUsuario(usuarioXNombre.getPass())
					.rolAsignado(rolAsignado)
					.genero(usuarioXNombre.getGenero())
					.cambiarClaveGenerica(Boolean.FALSE)
					.build();
			
			if ( BCrypt.checkpw("estandar", usuarioXNombre.getPass()) )
				usuarioAutenticadoDTO.setCambiarClaveGenerica(Boolean.TRUE);
			
			return usuarioAutenticadoDTO;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Boolean validarNumeroDocumentoExistente(String numeroDocumento) {
		return this.usuarioRepository.validarNumeroDocumentoExistente(numeroDocumento);
	}
	
	@Override
	public Boolean actualizarClaveUsuario(String nombreUsuario, String claveRegistrada) {
		try {
			
			UsuarioEntity usuarioParaActualizar = this.usuarioRepository.consultarUsarioXNombre(nombreUsuario);
			usuarioParaActualizar.setPass(BCrypt.hashpw(claveRegistrada, BCrypt.gensalt()));
			
			if ( BCrypt.checkpw(claveRegistrada, usuarioParaActualizar.getPass()) ) {
				usuarioParaActualizar = this.usuarioRepository.update(usuarioParaActualizar);
				return Boolean.TRUE;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	@Override
	public String nombreUsuarioAsignado(String primerNombre, String segundoNombre, String primerApellido, String segundoApellido) {

	    if (primerNombre == null || primerNombre.isEmpty() || primerApellido == null || primerApellido.isEmpty())
	        return null;

	    primerNombre = primerNombre.trim().toLowerCase();
	    segundoNombre = (segundoNombre != null) ? segundoNombre.trim().toLowerCase() : "";
	    primerApellido = primerApellido.trim().toLowerCase();
	    segundoApellido = (segundoApellido != null) ? segundoApellido.trim().toLowerCase() : "";
	    
	    // Traemos el maximo valor de los nombres
	    int maxLong = Math.max(primerNombre.length(), segundoNombre.length());

	    for ( int i = 1; i <= maxLong; i++ ) {

	        String nombre1 = primerNombre.substring(0, Math.min(i, primerNombre.length()));

	        String nombre2 = (!segundoNombre.isEmpty()) ?
	                segundoNombre.substring(0, Math.min(i, segundoNombre.length())) : "";

	        // 1. nombre1 + nombre2 + apellido1
	        String candidato1 = nombre1 + nombre2 + primerApellido;
	        if ( !this.usuarioRepository.existeUsuario(candidato1) )
	            return candidato1;

	        // 2. nombre1 + nombre2 + apellido2
	        if ( !segundoApellido.isEmpty() ) {
	        	
	            String candidato2 = nombre1 + nombre2 + segundoApellido;
	            
	            if ( !this.usuarioRepository.existeUsuario(candidato2) )
	                return candidato2;
	            
	        }

	        // 3. nombre1 + apellido1
	        String candidato3 = nombre1 + primerApellido;
	        if ( !this.usuarioRepository.existeUsuario(candidato3) )
	            return candidato3;

	        // 4. nombre1 + apellido2
	        if (!segundoApellido.isEmpty()) {
	        	
	            String candidato4 = nombre1 + segundoApellido;
	            if ( !this.usuarioRepository.existeUsuario(candidato4) )
	                return candidato4;
	        
	        }
	    }

	    // Último recurso: agregar un número secuencial
	    int contador = 1;
	    String base = primerNombre.substring(0, 1) + (segundoNombre.isEmpty() ? "" : segundoNombre.substring(0, 1)) + primerApellido;
	    while ( this.usuarioRepository.existeUsuario(base + contador) ) {
	        contador++;
	    }

	    return base + contador;
	}

	@Override
	public Boolean crearUsuario(UsuarioDTO usuarioParaCrear, String usuarioCreacion) throws Exception {
		try {
			
			RolEntity rolAsignado = this.rolRepository.findById(usuarioParaCrear.getRolAsignado().getIdRol());
			PaisEntity paisAsignado = this.paisRepository.findById(usuarioParaCrear.getPaisAsignado().getIdPais());
			CiudadEntity ciudadAsignada = this.ciudadRepository.findById(usuarioParaCrear.getCiudadAsignada().getIdCiudad());
			
			UsuarioEntity usuarioACrear = this.usuarioRepository.create(UsuarioEntity.builder()
																					 .numeroDocumento(usuarioParaCrear.getNumeroDocumentoUsuario())
																					 .nombre(usuarioParaCrear.getNombreUsuario())
																					 .pass(usuarioParaCrear.getPassUsuario())
																					 .rolEntity(rolAsignado)
																					 .primerNombre(usuarioParaCrear.getPrimerNombre().toUpperCase())
																					 .segundoNombre(usuarioParaCrear.getSegundoNombre() != null ? 
																							 usuarioParaCrear.getSegundoNombre().toUpperCase() : null)
																					 .primerApellido(usuarioParaCrear.getPrimerApellido().toUpperCase())
																					 .segundoApellido(usuarioParaCrear.getSegundoApellido() != null ?
																							 usuarioParaCrear.getSegundoApellido().toUpperCase() : null)
																					 .direccion(usuarioParaCrear.getDireccion().toUpperCase())
																					 .telefono(usuarioParaCrear.getTelefono())
																					 .email(usuarioParaCrear.getEmail())
																					 .pais(paisAsignado)
																					 .ciudad(ciudadAsignada)
																					 .genero(usuarioParaCrear.getGenero())
																					 .fechaNacimiento(usuarioParaCrear.getFechaNacimiento())
																					 .estado("A")
																					 .fechaCreacion(new Date())
																					 .usuarioCreacion(usuarioCreacion)
																					 .build());
			
			if ( usuarioACrear != null && !Objects.isNull(usuarioACrear) )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return Boolean.FALSE;
	}

	@Override
	public List<UsuarioDTO> consultarTodosLosUsuario() {
		try {
			List<UsuarioDTO> lstUsuariosDTO = new ArrayList<>();
			
			List<UsuarioEntity> lstUsuariosEntity = this.usuarioRepository.findAll();
			
			lstUsuariosEntity.stream().forEach(x -> {
				
				lstUsuariosDTO.add(UsuarioDTO.builder()
											 .idUsuario(x.getIdUsuario())
											 .numeroDocumentoUsuario(x.getNumeroDocumento())
											 .nombreUsuario(x.getNombre())
											 .passUsuario(x.getPass())
											 .rolAsignado(this.rolService.consultarRolXId(x.getRolEntity().getIdRol()))
											 .primerNombre(x.getPrimerNombre())
											 .segundoNombre(x.getSegundoNombre() != null ?
													 x.getSegundoNombre() : null)
											 .primerApellido(x.getPrimerApellido())
											 .segundoApellido(x.getSegundoApellido() != null ?
													 x.getSegundoApellido() : null)
											 .direccion(x.getDireccion())
											 .telefono(x.getTelefono())
											 .email(x.getEmail())
											 .paisAsignado(this.paisService.consultarPaisXId(x.getPais().getIdPais()))
											 .ciudadAsignada(this.ciudadService.consultarCiudadXId(x.getCiudad().getIdCiudad()))
											 .genero(x.getGenero())
											 .fechaNacimiento(x.getFechaNacimiento())
											 .idEstado(x.getEstado() != null ?
													   (x.getEstado().equals("A") ? "Activo" : "Inactivo") : null)
											 .fechaCreacion(x.getFechaCreacion())
											 .build());
				
			});
			
			if ( lstUsuariosDTO != null && !lstUsuariosDTO.isEmpty() )
				return lstUsuariosDTO;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<UsuarioDTO> consultarUsuarioXFiltro(UsuarioDTO usuarioConFiltros) {
		try {
			
			List<UsuarioEntity> lstUsuariosEntity = this.usuarioRepository.consultarUsuarioXFiltro(
					usuarioConFiltros.getRolAsignado().getIdRol(), usuarioConFiltros.getPaisAsignado().getIdPais(), usuarioConFiltros.getCiudadAsignada().getIdCiudad(),
					usuarioConFiltros.getNombreUsuario(), usuarioConFiltros.getIdEstado(), usuarioConFiltros.getLstFechasRango());
			
			
			List<UsuarioDTO> lstUsuariosDTO = new ArrayList<>();
			
			if ( lstUsuariosEntity != null && !lstUsuariosEntity.isEmpty() ) {
				
				lstUsuariosEntity.stream().forEach(x -> {
					
					RolDTO rolAsignado = this.rolService.consultarRolXId(x.getRolEntity().getIdRol());
					PaisDTO paisAsignado = this.paisService.consultarPaisXId(x.getPais().getIdPais());
					CiudadDTO ciudadAsignada = this.ciudadService.consultarCiudadXId(x.getCiudad().getIdCiudad());
					
					lstUsuariosDTO.add(UsuarioDTO.builder()
												 .idUsuario(x.getIdUsuario())
												 .numeroDocumentoUsuario(x.getNumeroDocumento())
												 .nombreUsuario(x.getNombre())
												 .passUsuario(x.getPass())
												 .rolAsignado(rolAsignado)
												 .primerNombre(x.getPrimerNombre())
												 .segundoNombre(x.getSegundoNombre())
												 .primerApellido(x.getPrimerApellido())
												 .segundoApellido(x.getSegundoApellido())
												 .direccion(x.getDireccion())
												 .telefono(x.getTelefono())
												 .email(x.getEmail())
												 .paisAsignado(paisAsignado)
												 .ciudadAsignada(ciudadAsignada)
												 .genero(x.getGenero())
												 .fechaNacimiento(x.getFechaNacimiento())
												 .idEstado(x.getEstado())
												 .fechaCreacion(x.getFechaCreacion())
												 .build());
					
				});
				
			}
			
			if ( lstUsuariosEntity != null && !lstUsuariosEntity.isEmpty() )
				return lstUsuariosDTO;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void activarODesactivarUsuario(UsuarioDTO usuarioSeleccionado) {
		try {
			
			UsuarioEntity usuarioParaModificar = this.usuarioRepository.findById(usuarioSeleccionado.getIdUsuario());
			
			if ( usuarioParaModificar != null && !Objects.isNull(usuarioParaModificar) ) {
				
				if ( usuarioParaModificar.getEstado().equals("A") ) {
					usuarioParaModificar.setEstado("I");
				} else {
					usuarioParaModificar.setEstado("A");
				}
				
				usuarioParaModificar = this.usuarioRepository.update(usuarioParaModificar);
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public UsuarioDTO consultarUsuarioXId(Integer idUsuario) {
		try {
			
			UsuarioEntity usuarioActualEntity = this.usuarioRepository.findById(idUsuario);
			
			RolDTO rolAsignado = this.rolService.consultarRolXId(usuarioActualEntity.getRolEntity().getIdRol());
			PaisDTO paisAsignado = this.paisService.consultarPaisXId(usuarioActualEntity.getPais().getIdPais());
			CiudadDTO ciudadAsignada = this.ciudadService.consultarCiudadXId(usuarioActualEntity.getCiudad().getIdCiudad());
			
			UsuarioDTO usuarioActualDTO = UsuarioDTO.builder()
													.idUsuario(usuarioActualEntity.getIdUsuario())
													.numeroDocumentoUsuario(usuarioActualEntity.getNumeroDocumento())
													.nombreUsuario(usuarioActualEntity.getNombre())
													.passUsuario(usuarioActualEntity.getPass())
													.rolAsignado(rolAsignado)
													.primerNombre(usuarioActualEntity.getPrimerNombre())
													.segundoNombre(usuarioActualEntity.getSegundoNombre())
													.primerApellido(usuarioActualEntity.getPrimerApellido())
													.segundoApellido(usuarioActualEntity.getSegundoApellido())
													.direccion(usuarioActualEntity.getDireccion())
													.telefono(usuarioActualEntity.getTelefono())
													.email(usuarioActualEntity.getEmail())
													.paisAsignado(paisAsignado)
													.ciudadAsignada(ciudadAsignada)
													.genero(usuarioActualEntity.getGenero())
													.fechaNacimiento(usuarioActualEntity.getFechaNacimiento())
													.idEstado(usuarioActualEntity.getEstado())
													.fechaCreacion(usuarioActualEntity.getFechaCreacion())
													.build();
			
			if ( usuarioActualDTO != null && !Objects.isNull(usuarioActualDTO) )
				return usuarioActualDTO;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Boolean actualizarInformacionUsuario(UsuarioDTO usuarioParaModificar) {
		try {
			
			UsuarioEntity usuarioActualEntity = this.usuarioRepository.findById(usuarioParaModificar.getIdUsuario());
			
			RolEntity rolAsignado = this.rolRepository.findById(usuarioParaModificar.getRolAsignado().getIdRol());
			PaisEntity paisAsignado = this.paisRepository.findById(usuarioParaModificar.getPaisAsignado().getIdPais());
			CiudadEntity ciudadAsignada = this.ciudadRepository.findById(usuarioParaModificar.getCiudadAsignada().getIdCiudad());
			
			UsuarioEntity usuarioModificado = this.usuarioRepository.update(
					UsuarioEntity.builder()
								 .idUsuario(usuarioActualEntity.getIdUsuario())
								 .numeroDocumento(usuarioActualEntity.getNumeroDocumento())
								 .nombre(usuarioActualEntity.getNombre())
								 .pass(usuarioActualEntity.getPass())
								 .rolEntity(rolAsignado)
								 .primerNombre(usuarioActualEntity.getPrimerNombre())
								 .segundoNombre(usuarioActualEntity.getSegundoNombre())
								 .primerApellido(usuarioActualEntity.getPrimerApellido())
								 .segundoApellido(usuarioActualEntity.getSegundoApellido())
								 .direccion(usuarioParaModificar.getDireccion().toUpperCase())
								 .telefono(usuarioParaModificar.getTelefono())
								 .email(usuarioParaModificar.getEmail())
								 .pais(paisAsignado)
								 .ciudad(ciudadAsignada)
								 .genero(usuarioParaModificar.getGenero())
								 .fechaNacimiento(usuarioParaModificar.getFechaNacimiento())
								 .estado(usuarioParaModificar.getIdEstado())
								 .fechaCreacion(usuarioActualEntity.getFechaCreacion())
								 .usuarioCreacion(usuarioActualEntity.getUsuarioCreacion())
								 .build());
			
			if ( usuarioModificado != null && !Objects.isNull(usuarioModificado) )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	@Override
	public List<UsuarioDTO> consultarUsuarioBusqueda(String busquedaUsuario) {
		try {
			
			List<UsuarioEntity> lstUsuariosEntity = this.usuarioRepository.buscarUsuariosXCoincidencia(busquedaUsuario);
			
			
			List<UsuarioDTO> lstUsuariosDTO = new ArrayList<>();
			
			if ( lstUsuariosEntity != null && !lstUsuariosEntity.isEmpty() ) {
				
				lstUsuariosEntity.stream().forEach(x -> {
					
					RolDTO rolAsignado = this.rolService.consultarRolXId(x.getRolEntity().getIdRol());
					PaisDTO paisAsignado = this.paisService.consultarPaisXId(x.getPais().getIdPais());
					CiudadDTO ciudadAsignada = this.ciudadService.consultarCiudadXId(x.getCiudad().getIdCiudad());
					
					lstUsuariosDTO.add(UsuarioDTO.builder()
												 .idUsuario(x.getIdUsuario())
												 .numeroDocumentoUsuario(x.getNumeroDocumento())
												 .nombreUsuario(x.getNombre())
												 .passUsuario(x.getPass())
												 .rolAsignado(rolAsignado)
												 .primerNombre(x.getPrimerNombre())
												 .segundoNombre(x.getSegundoNombre())
												 .primerApellido(x.getPrimerApellido())
												 .segundoApellido(x.getSegundoApellido())
												 .direccion(x.getDireccion())
												 .telefono(x.getTelefono())
												 .email(x.getEmail())
												 .paisAsignado(paisAsignado)
												 .ciudadAsignada(ciudadAsignada)
												 .genero(x.getGenero())
												 .fechaNacimiento(x.getFechaNacimiento())
												 .idEstado(x.getEstado())
												 .fechaCreacion(x.getFechaCreacion())
												 .build());
					
				});
				
			}
			
			if ( lstUsuariosEntity != null && !lstUsuariosEntity.isEmpty() )
				return lstUsuariosDTO;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<PerfilDTO> obtenerPerfilesUsuario(UsuarioDTO usuarioBusqueda) {
		try {
			
			List<PerfilUsuarioEntity> lstPerfilesUsuario = this.perfilUsuarioRepository.consultarPerfilesXUsuario(usuarioBusqueda.getIdUsuario());
			
			List<PerfilDTO> lstPerfilesIdentificados = new ArrayList<>();
			
			if ( lstPerfilesUsuario != null && !lstPerfilesUsuario.isEmpty() ) {
				
				lstPerfilesUsuario.stream().forEach(x -> {
					
					PerfilDTO perfilAsignado = this.perfilService.consultarPerfilXId(x.getPerfilEntity().getIdPerfil());
					
					lstPerfilesIdentificados.add(perfilAsignado);
					
				});
				
			}
			
			if ( lstPerfilesIdentificados != null && !lstPerfilesIdentificados.isEmpty() )
				return lstPerfilesIdentificados;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Boolean actualizarPerfilesDelUsuario(Integer idUsuario, List<PerfilDTO> lstPerfilesAsignados, String usuarioCreacion) {
		try {
			
			Integer perfilesGuardados = 0;
			
			if ( lstPerfilesAsignados != null && !lstPerfilesAsignados.isEmpty() ) {
				
				for ( PerfilDTO perfil : lstPerfilesAsignados ) {
					
					if ( !this.perfilUsuarioRepository.consultarSiUsuarioYaTienePerfil(idUsuario, perfil.getIdPerfil()) ) {
						
						PerfilUsuarioEntity perfilUsuarioCreado = PerfilUsuarioEntity
								.builder()
								.usuarioEntity(this.usuarioRepository.findById(idUsuario))
								.perfilEntity(this.perfilRepository.findById(perfil.getIdPerfil()))
								.idEstado("A")
								.fechaCreacion(new Date())
								.usuarioCreacion(usuarioCreacion)
								.build();
						
						this.perfilUsuarioRepository.create(perfilUsuarioCreado);
						
						perfilesGuardados++;
						
					}
					
				}
				
			}
			
			if ( perfilesGuardados > 0 )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	@Override
	public void eliminarTodosLosPerfilesAsignadosUsuario(Integer idUsuario) {
		try {
			
			List<PerfilUsuarioEntity> lstPerfilesUsuarioActualesEntity = this.perfilUsuarioRepository.consultarPerfilesXUsuario(idUsuario);
			
			if ( lstPerfilesUsuarioActualesEntity != null && !lstPerfilesUsuarioActualesEntity.isEmpty() ) {
				
				lstPerfilesUsuarioActualesEntity.stream().forEach(x -> {
					this.perfilUsuarioRepository.delete(x.getIdPerfilUsuario());
				});
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
