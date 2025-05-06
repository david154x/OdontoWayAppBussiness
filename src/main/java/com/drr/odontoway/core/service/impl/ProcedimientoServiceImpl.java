package com.drr.odontoway.core.service.impl;

import java.util.Date;

import com.drr.odontoway.core.dto.ProcedimientoDTO;
import com.drr.odontoway.core.service.ProcedimientoService;
import com.drr.odontoway.entity.ProcedimientoEntity;
import com.drr.odontoway.repository.ProcedimientoRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProcedimientoServiceImpl implements ProcedimientoService {
	
	@Inject
	private ProcedimientoRepository procedimientoRepository;

	@Override
	public Boolean crearProcedimiento(ProcedimientoDTO procedimientoParaCrear, String usuarioCreacion) {
		try {
			
			ProcedimientoEntity procedimientoEntity = this.procedimientoRepository.create(
					ProcedimientoEntity.builder()
									   .tipoProcedimiento(procedimientoParaCrear.getTipoProcedimiento())
									   .nombreProcedimiento(procedimientoParaCrear.getNombreProcedimiento().toUpperCase())
									   .descripcionProcedimiento(procedimientoParaCrear.getDescripcionProcedimiento())
									   .cantidad(procedimientoParaCrear.getCantidad())
									   .tiempoDuracion(procedimientoParaCrear.getTiempoDuracion())
									   .valorCosto(procedimientoParaCrear.getValorCosto())
									   .idEstado("A")
									   .fechaCreacion(new Date())
									   .usuarioCreacion(usuarioCreacion)
									   .build());
			
			if ( procedimientoEntity.getIdProcedimiento() != null )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

}
