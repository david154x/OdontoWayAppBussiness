package com.drr.odontoway.core.service;

import com.drr.odontoway.core.dto.ProcedimientoDTO;

public interface ProcedimientoService {
	
	Boolean crearProcedimiento(ProcedimientoDTO procedimientoParaCrear, String usuarioCreacion);

}
