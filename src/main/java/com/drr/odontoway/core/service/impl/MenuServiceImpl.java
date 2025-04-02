package com.drr.odontoway.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.drr.odontoway.core.dto.MenuDTO;
import com.drr.odontoway.core.service.MenuService;
import com.drr.odontoway.entity.MenuEntity;
import com.drr.odontoway.entity.MenuPerfilEntity;
import com.drr.odontoway.entity.PerfilUsuarioEntity;
import com.drr.odontoway.repository.MenuPerfilRepository;
import com.drr.odontoway.repository.MenuRepository;
import com.drr.odontoway.repository.PerfilUsuarioRepository;
import com.google.gson.GsonBuilder;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MenuServiceImpl implements MenuService {
	
	@Inject
	private PerfilUsuarioRepository perfilUsuarioRepository;
	
	@Inject
	private MenuPerfilRepository menuPerfilRepository;
	
	@Inject
	private MenuRepository menuRepository;

	@Override
	public List<MenuDTO> consultarMenuXUsuario(Integer idUsuario) {
		try {
			
			List<MenuDTO> lstMenuUsuario = new ArrayList<>();
			
			List<PerfilUsuarioEntity> lstPerfilesUsuario = this.perfilUsuarioRepository.consultarPerfilesXUsuario(idUsuario);
			
			if ( lstPerfilesUsuario != null && !lstPerfilesUsuario.isEmpty() ) {
				
				lstPerfilesUsuario.stream().forEach(x -> {
					
					List<MenuPerfilEntity> lstMenuPerfilAsignado = this.menuPerfilRepository.consultarMenuPerfilXCodigoPerfil(x.getPerfilEntity().getIdPerfil());
					
					if ( lstMenuPerfilAsignado != null && !lstMenuPerfilAsignado.isEmpty() ) {
						
						lstMenuPerfilAsignado.stream().forEach(y -> {
							
							lstMenuUsuario.add(MenuDTO.builder()
									  				  .idMenu(y.getMenuEntity().getIdMenu())
									  				  .nombreMenu(y.getMenuEntity().getNombreMenu())
									  				  .rutaUrl(y.getMenuEntity().getRutaUrl())
									  				  .nombreDocumento(y.getMenuEntity().getNombreDocumento())
									  				  .iconoMenu(y.getMenuEntity().getIconoMenu())
									  				  .moduloMenu(y.getMenuEntity().getModuloMenu())
									  				  .subMenu(y.getMenuEntity().getSubMenu())
									  				  .idEstado(y.getMenuEntity().getIdEstado())
									  				  .build());
							
						});
						
					}
					
				});
				
			}
			
			if ( lstMenuUsuario != null ) {
				
				lstMenuUsuario.forEach(x -> {
					
					System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(x));
					
				});
				
				return lstMenuUsuario;
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String ubicacionMenu(Integer idMenu) {
	    try {
	        StringBuilder rutaUbicacion = new StringBuilder();
	        MenuEntity menuActual = this.menuRepository.findById(idMenu);
	        
	        while (menuActual != null) {
	            rutaUbicacion.insert(0, menuActual.getNombreMenu() + " > ");

	            if (menuActual.getSubMenu() != null) {
	                menuActual = this.menuRepository.findById(menuActual.getSubMenu());
	            } else if (menuActual.getModuloMenu() != null) {
	                menuActual = this.menuRepository.findById(menuActual.getModuloMenu());
	            } else {
	                break;
	            }
	        }

	        return "Estas en: " + (rutaUbicacion.length() > 0 ? rutaUbicacion.substring(0, rutaUbicacion.length() - 3) : "Inicio");
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

}
