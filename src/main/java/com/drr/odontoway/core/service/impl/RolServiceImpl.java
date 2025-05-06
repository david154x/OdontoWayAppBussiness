package com.drr.odontoway.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.drr.odontoway.core.dto.RolDTO;
import com.drr.odontoway.core.service.RolService;
import com.drr.odontoway.entity.RolEntity;
import com.drr.odontoway.repository.RolRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RolServiceImpl implements RolService {
	
	@Inject
	private RolRepository rolRepository;

	@Override
	public Boolean consultarSiRolExisteXNombre(String nombreRol) {
		try {
			
			RolEntity rolBuscado = this.rolRepository.consultarRolXNombreRol(nombreRol);
			
			if ( rolBuscado != null && !Objects.isNull(rolBuscado) )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean crearNuevoRol(String nombreRol, String descripcionRol, String usuarioCreacion) {
		try {
			RolEntity nuevoRolCreado = RolEntity.builder()
												.nombre(nombreRol)
												.descripcion(descripcionRol)
												.idEstado("A")
												.fechaCreacion(new Date())
												.usuarioCreacion(usuarioCreacion)
												.build();
					
			nuevoRolCreado = this.rolRepository.create(nuevoRolCreado);
			
			if ( nuevoRolCreado.getIdRol() != null )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}
	
	@Override
	public List<RolDTO> consultarTodosLosRoles() {
		try {
			
			List<RolEntity> lstTodosLosRolesEntity = this.rolRepository.findAll();
			
			List<RolDTO> lstTodosLosRolesDTO = new ArrayList<>();
			
			if ( lstTodosLosRolesEntity != null && !lstTodosLosRolesEntity.isEmpty() ) {
				
				lstTodosLosRolesEntity.stream().forEach(x -> {
				
				lstTodosLosRolesDTO.add(RolDTO.builder()
											  .idRol(x.getIdRol())
											  .nombreRol(x.getNombre())
											  .descripcionRol(x.getDescripcion())
											  .idEstado(x.getIdEstado() != null ?
													  (x.getIdEstado().equals("A") ? "Activo" : "Inactivo") : null)
											  .fechaCreacion(x.getFechaCreacion())
											  .build());
				
				});
				
			}
			
			if ( lstTodosLosRolesDTO != null && !lstTodosLosRolesDTO.isEmpty() )
				return lstTodosLosRolesDTO;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<RolDTO> consultarRolXFiltros(RolDTO consultaXFiltro) {
		
		List<RolEntity> consultarRolXFiltros = this.rolRepository.consultarRolXFiltros(consultaXFiltro.getNombreRol(),
				consultaXFiltro.getDescripcionRol(), consultaXFiltro.getIdEstado(), consultaXFiltro.getLstFechasRango());
		
		List<RolDTO> lstResultadosEncontrados = new ArrayList<>();
		
		if ( consultarRolXFiltros != null && !consultarRolXFiltros.isEmpty() ) {
			
			consultarRolXFiltros.stream().forEach(x -> {
				
				lstResultadosEncontrados.add(RolDTO.builder()
					    						   .idRol(x.getIdRol())
					    						   .nombreRol(x.getNombre())
					    						   .descripcionRol(x.getDescripcion())
					    						   .idEstado(x.getIdEstado() != null ?
					    								   (x.getIdEstado().equals("A") ? "Activo" : "Inactivo") : null)
					    						   .fechaCreacion(x.getFechaCreacion())
					    						   .build());
				
			});
			
		}
		
		if ( lstResultadosEncontrados != null && !lstResultadosEncontrados.isEmpty() )
			return lstResultadosEncontrados;
		
		return null;
	}

	@Override
	public void modificarEstadoRol(RolDTO rolParaModificar) {
		try {
			
			RolEntity rolAModificar = this.rolRepository.findById(rolParaModificar.getIdRol());
			
			if ( rolAModificar != null && !Objects.isNull(rolAModificar) ) {
				
				if ( rolParaModificar.getIdEstado().equals("Activo") )
					rolAModificar.setIdEstado("A");
				
				if ( rolParaModificar.getIdEstado().equals("Inactivo") )
					rolAModificar.setIdEstado("I");
				
				rolAModificar = this.rolRepository.update(rolAModificar);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean actualizarRol(RolDTO rolParaModificar) {
		try {
			
			RolEntity rolAModificar = this.rolRepository.findById(rolParaModificar.getIdRol());
			
			if ( rolParaModificar.getNombreRol() != null && !rolParaModificar.getNombreRol().isEmpty() )
				rolAModificar.setNombre(rolParaModificar.getNombreRol());
			
			if ( rolParaModificar.getDescripcionRol() != null && !rolParaModificar.getDescripcionRol().isEmpty() )
				rolAModificar.setDescripcion(rolParaModificar.getDescripcionRol());
			
			rolAModificar = this.rolRepository.update(rolAModificar);
			
			if ( rolAModificar.getNombre().equals(rolParaModificar.getNombreRol()) )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	@Override
	public RolDTO consultarRolXId(Integer idRol) {
		try {
			
			RolEntity rolEncontrado = this.rolRepository.findById(idRol);
			
			RolDTO rolAsignado = RolDTO.builder()
									   .idRol(rolEncontrado.getIdRol())
									   .nombreRol(rolEncontrado.getNombre())
									   .descripcionRol(rolEncontrado.getDescripcion())
									   .idEstado(rolEncontrado.getIdEstado())
									   .fechaCreacion(rolEncontrado.getFechaCreacion())
									   .build();
			
			if ( rolAsignado != null && !Objects.isNull(rolAsignado) )
				return rolAsignado;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
