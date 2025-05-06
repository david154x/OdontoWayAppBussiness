package com.drr.odontoway.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.drr.odontoway.core.dto.CiudadDTO;
import com.drr.odontoway.core.dto.PaisDTO;
import com.drr.odontoway.core.service.CiudadService;
import com.drr.odontoway.core.service.PaisService;
import com.drr.odontoway.entity.CiudadEntity;
import com.drr.odontoway.entity.PaisEntity;
import com.drr.odontoway.repository.CiudadRepository;
import com.drr.odontoway.repository.PaisRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CiudadServiceImpl implements CiudadService {
	
	@Inject
	private PaisService paisService;
	
	@Inject
	private CiudadRepository ciudadRepository;
	
	@Inject
	private PaisRepository paisRepository;
	
	@Override
	public Boolean consultarSiExisteCiudadXPais(Integer idPais, String nombreCiudad) {
		try {
			
			CiudadEntity ciudadEncontrada = this.ciudadRepository.consultarSiExisteCiudadXPais(idPais, nombreCiudad);
			
			if ( ciudadEncontrada != null && !Objects.isNull(ciudadEncontrada) )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean crearCiudad(CiudadDTO ciudadParaCrear, String usuarioCreacion) {
		try {
			
			PaisEntity paisAsociado = this.paisRepository.findById(ciudadParaCrear.getPaisAsignado().getIdPais());
			
			CiudadEntity ciudadCreada = this.ciudadRepository.create(CiudadEntity.builder()
																				 .paisEntity(paisAsociado)
																				 .nombreCiudad(ciudadParaCrear.getNombreCiudad().toUpperCase())
																				 .nombreCorto(ciudadParaCrear.getNombreCorto().toUpperCase())
																				 .idEstado("A")
																				 .fechaCreacion(new Date())
																				 .usuarioCreacion(usuarioCreacion)
																				 .build());
			
			if ( ciudadCreada != null && ciudadCreada.getIdCiudad() != null )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	@Override
	public List<CiudadDTO> consultarTodasLasCiudades() {
		try {
			
			List<CiudadEntity> lstCiudadesEntity = this.ciudadRepository.findAll();

			List<CiudadDTO> lstCiudadesDTO = new ArrayList<>();
			
			if ( lstCiudadesEntity != null && !lstCiudadesEntity.isEmpty() ) {
				
				lstCiudadesEntity.stream().forEach(x -> {
					
					lstCiudadesDTO.add(CiudadDTO.builder()
												.idCiudad(x.getIdCiudad())
												.paisAsignado(this.paisService.consultarPaisXId(x.getPaisEntity().getIdPais()))
												.nombreCiudad(x.getNombreCiudad())
												.nombreCorto(x.getNombreCorto())
												.idEstado(x.getIdEstado() != null ?
														   (x.getIdEstado().equals("A") ? "Activo" : "Inactivo") : null)
												.fechaCreacion(x.getFechaCreacion())
												.build());
					
				});
				
			}
			
			if ( lstCiudadesDTO != null && !lstCiudadesDTO.isEmpty() )
				return 	lstCiudadesDTO;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<CiudadDTO> consultarCiudadXFiltros(CiudadDTO consultaFiltroCiudad) {
		
		List<CiudadEntity> consultarCiudadXFiltros = this.ciudadRepository.consultarCiudadXFiltros(
				consultaFiltroCiudad.getPaisAsignado().getIdPais(), consultaFiltroCiudad.getNombreCiudad(),
				consultaFiltroCiudad.getIdEstado(), consultaFiltroCiudad.getLstFechasRango());
		
		List<CiudadDTO> lstResultadosEncontrados = new ArrayList<>();
		
		if ( consultarCiudadXFiltros != null && !consultarCiudadXFiltros.isEmpty() ) {
			
			consultarCiudadXFiltros.stream().forEach(x -> {
				
				lstResultadosEncontrados.add(CiudadDTO.builder()
						    						  .idCiudad(x.getIdCiudad())
						    						  .paisAsignado(this.paisService.consultarPaisXId(x.getPaisEntity().getIdPais()))
						    						  .nombreCiudad(x.getNombreCiudad())
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
	public void activarODesactivarCiudad(CiudadDTO ciudadSeleccionada) {
		try {
			
			CiudadEntity ciudadParaModificar = this.ciudadRepository.findById(ciudadSeleccionada.getIdCiudad());
			
			if ( ciudadParaModificar != null && !Objects.isNull(ciudadParaModificar) ) {
				
				if ( ciudadParaModificar.getIdEstado().equals("A") ) {
					ciudadParaModificar.setIdEstado("I");
				} else {
					ciudadParaModificar.setIdEstado("A");
				}
				
				ciudadParaModificar = this.ciudadRepository.update(ciudadParaModificar);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean modificarCiudad(CiudadDTO ciudadParaModificar) {
		try {
			
			CiudadEntity ciudadActual = this.ciudadRepository.findById(ciudadParaModificar.getIdCiudad());
			
			if ( ciudadParaModificar.getPaisAsignado().getIdPais() != null )
				ciudadActual.setPaisEntity(this.paisRepository.findById(ciudadParaModificar.getPaisAsignado().getIdPais()));
			
			if ( ciudadParaModificar.getNombreCiudad() != null && !ciudadParaModificar.getNombreCiudad().isEmpty() )
				ciudadActual.setNombreCiudad(ciudadParaModificar.getNombreCiudad().toUpperCase());
			
			if ( ciudadParaModificar.getNombreCorto() != null && !ciudadParaModificar.getNombreCorto().isEmpty() ) {
				
				if ( !ciudadActual.getNombreCorto().equals(ciudadParaModificar.getNombreCorto().toUpperCase()) )
					ciudadActual.setNombreCorto(ciudadParaModificar.getNombreCorto().toUpperCase());
				
			}
			
			CiudadEntity ciudadConNuevosCambios = this.ciudadRepository.update(ciudadActual);
			
			if ( ciudadConNuevosCambios != null && !Objects.isNull(ciudadConNuevosCambios) )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	@Override
	public List<CiudadDTO> consultarCiudadesXPais(Integer idPais) {
		try {
			
			List<CiudadDTO> lstCiudadesXPaisDTO = new ArrayList<>();
			
			List<CiudadEntity> lstCiudadesXPaisEntity = this.ciudadRepository.consultarCiudadesXPais(idPais);
			
			if ( lstCiudadesXPaisEntity != null && !lstCiudadesXPaisEntity.isEmpty() ) {
				
				lstCiudadesXPaisEntity.stream().forEach(x -> {
					
					lstCiudadesXPaisDTO.add(CiudadDTO.builder()
													 .idCiudad(x.getIdCiudad())
													 .paisAsignado(this.paisService.consultarPaisXId(x.getPaisEntity().getIdPais()))
													 .nombreCiudad(x.getNombreCiudad())
													 .nombreCorto(x.getNombreCorto())
													 .idEstado(x.getIdEstado() != null ?
															   (x.getIdEstado().equals("A") ? "Activo" : "Inactivo") : null)
													 .fechaCreacion(x.getFechaCreacion())
													 .build());
					
				});
				
			}
			
			if ( lstCiudadesXPaisDTO != null && !lstCiudadesXPaisDTO.isEmpty() )
				return lstCiudadesXPaisDTO;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public CiudadDTO consultarCiudadXId(Integer idCiudad) {
		try {
			
			CiudadEntity ciudadEncontrada = this.ciudadRepository.findById(idCiudad);
			PaisDTO paisAsignado = this.paisService.consultarPaisXId(ciudadEncontrada.getPaisEntity().getIdPais());
			
			CiudadDTO ciudadAsignada = CiudadDTO.builder()
												.idCiudad(ciudadEncontrada.getIdCiudad())
												.paisAsignado(paisAsignado)
												.nombreCiudad(ciudadEncontrada.getNombreCiudad())
												.nombreCorto(ciudadEncontrada.getNombreCorto())
												.idEstado(ciudadEncontrada.getIdEstado())
												.fechaCreacion(ciudadEncontrada.getFechaCreacion())
												.build();
			
			if ( ciudadAsignada != null && !Objects.isNull(ciudadAsignada) )
				return ciudadAsignada;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
