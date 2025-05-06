package com.drr.odontoway.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.drr.odontoway.core.dto.PerfilDTO;
import com.drr.odontoway.core.service.PerfilService;
import com.drr.odontoway.entity.PerfilEntity;
import com.drr.odontoway.repository.PerfilRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PerfilServiceImpl implements PerfilService {
	
	@Inject
	private PerfilRepository perfilRepository;

	@Override
	public Boolean consultarSiPerfilXNombreExiste(String nombrePerfil) {
		try {
			
			PerfilEntity perfilEncontrado = this.perfilRepository.consultarPerfilXNombre(nombrePerfil);
			
			if ( perfilEncontrado != null && !Objects.isNull(perfilEncontrado) )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean crearPerfil(PerfilDTO perfilParaCrear, String usuarioCreacion) {
		try {
			
			PerfilEntity perfilCreado = this.perfilRepository.create(
					PerfilEntity.builder()
								.nombrePerfil(perfilParaCrear.getNombrePerfil().toUpperCase())
								.descripcionPerfil(perfilParaCrear.getDescripcionPerfil())
								.idEstado("A")
								.fechaCreacion(new Date())
								.usuarioCreacion(usuarioCreacion)
								.build());
			
			if ( perfilCreado != null && !Objects.isNull(perfilCreado) )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	@Override
	public List<PerfilDTO> consultarTodosLosPerfiles() {
		try {
			
			List<PerfilEntity> lstPerfilesActualesEntity = this.perfilRepository.findAll();
			
			List<PerfilDTO> lstPerfilesActualsDTO = new ArrayList<>();
			
			if ( lstPerfilesActualesEntity != null && !lstPerfilesActualesEntity.isEmpty() ) {
				
				lstPerfilesActualesEntity.stream().forEach(x -> {
					
					lstPerfilesActualsDTO.add(PerfilDTO.builder()
													   .idPerfil(x.getIdPerfil())
													   .nombrePerfil(x.getNombrePerfil())
													   .descripcionPerfil(x.getDescripcionPerfil())
													   .idEstado(x.getIdEstado() != null ?
						    						    		  (x.getIdEstado().equals("A") ? "Activo" : "Inactivo") : null)
													   .fechaCreacion(x.getFechaCreacion())
													   .build());
					
				});
				
				
			}
			
			if ( lstPerfilesActualsDTO != null && !lstPerfilesActualsDTO.isEmpty() )
				return lstPerfilesActualsDTO;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<PerfilDTO> consultarPerfilXFiltros(PerfilDTO consultaXFiltro) {
		try {
			
			List<PerfilEntity> consultarRolXFiltros = this.perfilRepository.consultarPerfilXFiltros(consultaXFiltro.getNombrePerfil(),
					consultaXFiltro.getDescripcionPerfil(), consultaXFiltro.getIdEstado(), consultaXFiltro.getLstFechasRango());
			
			List<PerfilDTO> lstResultadosEncontrados = new ArrayList<>();
			
			if ( consultarRolXFiltros != null && !consultarRolXFiltros.isEmpty() ) {
				
				consultarRolXFiltros.stream().forEach(x -> {
					
					lstResultadosEncontrados.add(PerfilDTO.builder()
						    						      .idPerfil(x.getIdPerfil())
						    						      .nombrePerfil(x.getNombrePerfil())
						    						      .descripcionPerfil(x.getDescripcionPerfil())
						    						      .idEstado(x.getIdEstado() != null ?
						    						    		  (x.getIdEstado().equals("A") ? "Activo" : "Inactivo") : null)
						    						      .fechaCreacion(x.getFechaCreacion())
						    						      .build());
					
				});
				
			}
			
			if ( lstResultadosEncontrados != null && !lstResultadosEncontrados.isEmpty() )
				return lstResultadosEncontrados;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void activarODesactivarPerfil(PerfilDTO perfilSeleccionado) {
		try {
			
			PerfilEntity perfilParaModificar = this.perfilRepository.findById(perfilSeleccionado.getIdPerfil());
			
			if ( perfilParaModificar != null && !Objects.isNull(perfilParaModificar) ) {
				
				if ( perfilParaModificar.getIdEstado().equals("A") ) {
					perfilParaModificar.setIdEstado("I");
				} else {
					perfilParaModificar.setIdEstado("A");
				}
				
				perfilParaModificar = this.perfilRepository.update(perfilParaModificar);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean actualizarPerfil(PerfilDTO perfilParaModificar) {
		Boolean huboCambios = Boolean.FALSE;
		try {
			
			PerfilEntity perfilAModificar = this.perfilRepository.findById(perfilParaModificar.getIdPerfil());
			
			if ( perfilAModificar.getNombrePerfil() != null && !perfilParaModificar.getNombrePerfil().isEmpty() ) {
				perfilAModificar.setNombrePerfil(perfilParaModificar.getNombrePerfil().toUpperCase());
				huboCambios = Boolean.TRUE;
			}
			
			if ( perfilAModificar.getDescripcionPerfil() != null && !perfilParaModificar.getDescripcionPerfil().isEmpty() ) {
				perfilAModificar.setDescripcionPerfil(perfilParaModificar.getDescripcionPerfil());
				huboCambios = Boolean.TRUE;
			}
			
			this.perfilRepository.update(perfilAModificar);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return huboCambios;
	}

	@Override
	public PerfilDTO consultarPerfilXId(Integer idPerfil) {
		try {
			
			PerfilEntity perfilEncontradoEntity = this.perfilRepository.findById(idPerfil);
			
			PerfilDTO perfilEncontradoDTO = PerfilDTO.builder()
												     .idPerfil(perfilEncontradoEntity.getIdPerfil())
												     .nombrePerfil(perfilEncontradoEntity.getNombrePerfil())
												     .descripcionPerfil(perfilEncontradoEntity.getDescripcionPerfil())
												     .idEstado(perfilEncontradoEntity.getIdEstado() != null ?
												    	  (perfilEncontradoEntity.getIdEstado().equals("A") ? "Activo" : "Inactivo") : null)
												     .fechaCreacion(perfilEncontradoEntity.getFechaCreacion())
												     .build();
			
			if ( perfilEncontradoDTO != null && !Objects.isNull(perfilEncontradoDTO) )
				return perfilEncontradoDTO;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<PerfilDTO> consultarPerfilBusqueda(String busquedaPerfil) {
try {
			
			List<PerfilEntity> lstPerfilEntity = this.perfilRepository.buscarPerfilesXCoincidencia(busquedaPerfil);
			
			
			List<PerfilDTO> lstPerfilDTO = new ArrayList<>();
			
			if ( lstPerfilEntity != null && !lstPerfilEntity.isEmpty() ) {
				
				lstPerfilEntity.stream().forEach(x -> {
					
					lstPerfilDTO.add(PerfilDTO.builder()
											  .idPerfil(x.getIdPerfil())
											  .nombrePerfil(x.getNombrePerfil())
											  .descripcionPerfil(x.getDescripcionPerfil())
											  .idEstado(x.getIdEstado())
											  .fechaCreacion(x.getFechaCreacion())
											  .build());
					
				});
				
			}
			
			if ( lstPerfilDTO != null && !lstPerfilDTO.isEmpty() )
				return lstPerfilDTO;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
