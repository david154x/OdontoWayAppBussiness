package com.drr.odontoway.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.drr.odontoway.core.dto.PaisDTO;
import com.drr.odontoway.core.service.PaisService;
import com.drr.odontoway.entity.PaisEntity;
import com.drr.odontoway.repository.PaisRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PaisServiceImpl implements PaisService {
	
	@Inject
	private PaisRepository paisRepository;

	@Override
	public Boolean consultarSiPaisExisteXNombre(String nombrePais) {
		try {
			
			PaisEntity paisBuscado = this.paisRepository.consultarPaisXNombre(nombrePais);
			
			if ( paisBuscado != null && !Objects.isNull(paisBuscado) )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean consultarSiPaisExisteXNombreCorto(String nombreCorto) {
		try {
			
			PaisEntity paisBuscado = this.paisRepository.consultarPaisXNombreCorto(nombreCorto);
			
			if ( paisBuscado != null && !Objects.isNull(paisBuscado) )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean crearPais(PaisDTO paisDTO, String nombreUsuario) throws Exception {
		try {
			
			PaisEntity paisParaGuardar = PaisEntity.builder()
												   .nombrePais(paisDTO.getNombrePais().toUpperCase())
												   .nombreCorto(paisDTO.getNombreCorto().toUpperCase())
												   .idEstado("A")
												   .fechaCreacion(new Date())
												   .usuarioCreacion(nombreUsuario)
												   .build();
			
			PaisEntity paisCreado = this.paisRepository.create(paisParaGuardar);
			
			if ( paisCreado != null && !Objects.isNull(paisCreado) && paisCreado.getIdPais() != null )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			throw e;
		}
		return Boolean.FALSE;
	}

	@Override
	public List<PaisDTO> consultarTodosLosPaises() {
		try {
			
			List<PaisEntity> lstTodosLosPaisesEntity = this.paisRepository.findAll();
			
			List<PaisDTO> lstTodosLosPaisesDTO = new ArrayList<>();
			
			if ( lstTodosLosPaisesEntity != null && !lstTodosLosPaisesEntity.isEmpty() ) {
				
				lstTodosLosPaisesEntity.stream().forEach(x -> {
				
					lstTodosLosPaisesDTO.add(PaisDTO.builder()
											  .idPais(x.getIdPais())
											  .nombrePais(x.getNombrePais())
											  .nombreCorto(x.getNombreCorto())
											  .idEstado(x.getIdEstado() != null ?
													   (x.getIdEstado().equals("A") ? "Activo" : "Inactivo") : null)
											  .fechaCreacion(x.getFechaCreacion())
											  .build());
				
				});
				
			}
			
			if ( lstTodosLosPaisesDTO != null && !lstTodosLosPaisesDTO.isEmpty() )
				return lstTodosLosPaisesDTO;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<PaisDTO> consultarPaisXFiltros(PaisDTO consultaXFiltro) {
		
		List<PaisEntity> consultarPaisXFiltros = this.paisRepository.consultarPaisXFiltros(consultaXFiltro.getNombrePais(),
				consultaXFiltro.getIdEstado(), consultaXFiltro.getLstFechasRango());
		
		List<PaisDTO> lstResultadosEncontrados = new ArrayList<>();
		
		if ( consultarPaisXFiltros != null && !consultarPaisXFiltros.isEmpty() ) {
			
			consultarPaisXFiltros.stream().forEach(x -> {
				
				lstResultadosEncontrados.add(PaisDTO.builder()
					    						    .idPais(x.getIdPais())
					    						    .nombrePais(x.getNombrePais())
					    						    .nombreCorto(x.getNombreCorto())
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
	public void activarODesactivarPais(PaisDTO paisSeleccionado) {
		try {
			
			PaisEntity paisParaModificar = this.paisRepository.findById(paisSeleccionado.getIdPais());
			
			if ( paisParaModificar != null && !Objects.isNull(paisParaModificar) ) {
				
				if ( paisParaModificar.getIdEstado().equals("A") ) {
					paisParaModificar.setIdEstado("I");
				} else {
					paisParaModificar.setIdEstado("A");
				}
				
				paisParaModificar = this.paisRepository.update(paisParaModificar);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean actualizarPais(PaisDTO paisNuevosValores) {
		try {
			
			PaisEntity paisParaModificar = this.paisRepository.findById(paisNuevosValores.getIdPais());
			
			if ( paisParaModificar != null && !Objects.isNull(paisParaModificar) ) {
				
				if ( paisNuevosValores.getNombrePais() != null && !paisNuevosValores.getNombrePais().isEmpty() ) {
					
					if ( !paisNuevosValores.getNombrePais().equals(paisParaModificar.getNombrePais()) )
						paisParaModificar.setNombrePais(paisNuevosValores.getNombrePais().toUpperCase());	
					
				}
				
				if ( paisNuevosValores.getNombreCorto() != null && !paisNuevosValores.getNombreCorto().isEmpty() ) {
					
					if ( !paisNuevosValores.getNombreCorto().equals(paisParaModificar.getNombreCorto()) )
						paisParaModificar.setNombreCorto(paisNuevosValores.getNombreCorto().toUpperCase());
					
				}
				
				paisParaModificar = this.paisRepository.update(paisParaModificar);
				
				if ( paisParaModificar.getNombrePais().equals(paisNuevosValores.getNombrePais()) )
					return Boolean.TRUE;
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.TRUE;
	}

	@Override
	public PaisDTO consultarPaisXId(Integer idPais) {
		try {
			
			PaisEntity paisEncontradoEntity = this.paisRepository.findById(idPais);
			
			if ( paisEncontradoEntity != null && !Objects.isNull(paisEncontradoEntity) ) {
				
				PaisDTO paisEncontradoDTO = PaisDTO.builder()
												   .idPais(paisEncontradoEntity.getIdPais())
												   .nombrePais(paisEncontradoEntity.getNombrePais())
												   .nombreCorto(paisEncontradoEntity.getNombreCorto())
												   .idEstado(paisEncontradoEntity.getIdEstado())
												   .fechaCreacion(paisEncontradoEntity.getFechaCreacion())
												   .build();
				
				return paisEncontradoDTO;
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
