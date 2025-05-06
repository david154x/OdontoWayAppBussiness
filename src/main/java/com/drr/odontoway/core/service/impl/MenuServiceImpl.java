package com.drr.odontoway.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.drr.odontoway.core.dto.MenuDTO;
import com.drr.odontoway.core.service.MenuService;
import com.drr.odontoway.entity.MenuEntity;
import com.drr.odontoway.entity.MenuPerfilEntity;
import com.drr.odontoway.entity.PerfilUsuarioEntity;
import com.drr.odontoway.repository.MenuPerfilRepository;
import com.drr.odontoway.repository.MenuRepository;
import com.drr.odontoway.repository.PerfilRepository;
import com.drr.odontoway.repository.PerfilUsuarioRepository;

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
	
	@Inject
	private PerfilRepository perfilRepository;

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
									  				  .nombreClaseView(y.getMenuEntity().getNombreClaseView())
									  				  .numeroItem(y.getMenuEntity().getNumeroItem())
									  				  .idEstado(y.getMenuEntity().getIdEstado())
									  				  .build());
							
						});
						
					}
					
				});
				
			}
			
			if ( lstMenuUsuario != null ) {
				
//				lstMenuUsuario.forEach(x -> {
//					System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(x));
//				});
				
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

	@Override
	public List<MenuDTO> consultarTodosLosMenu() {
		try {
			
			List<MenuEntity> lstMenuEntity = this.menuRepository.findAll();
			List<MenuDTO> lstMenuUsuario = new ArrayList<>();
					
			if ( lstMenuEntity != null && !lstMenuEntity.isEmpty() ) {
				
				
				lstMenuEntity.stream().forEach(x -> {
					
					lstMenuUsuario.add(MenuDTO.builder()
											  .idMenu(x.getIdMenu())
											  .nombreMenu(x.getNombreMenu())
											  .rutaUrl(x.getRutaUrl())
											  .nombreDocumento(x.getNombreDocumento())
											  .iconoMenu(x.getIconoMenu())
											  .moduloMenu(x.getModuloMenu())
											  .subMenu(x.getSubMenu())
											  .nombreClaseView(x.getNombreClaseView())
											  .numeroItem(x.getNumeroItem())
											  .idEstado(x.getIdEstado())
											  .build());
					
				});
				
			}
			
			if ( lstMenuUsuario != null )
				return lstMenuUsuario;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<MenuDTO> consultarMenuXPerfil(Integer idPerfil) {
		try {
			
			List<MenuPerfilEntity> lstMenuXPerfilActualEntity = this.menuPerfilRepository.consultarMenuPerfilXCodigoPerfil(idPerfil);
			
			List<MenuDTO> lstMenuXPerfilActualDTO = new ArrayList<>();
					
			if ( lstMenuXPerfilActualEntity != null && !lstMenuXPerfilActualEntity.isEmpty() ) {
				
				lstMenuXPerfilActualEntity.stream().forEach(x -> {
					
					MenuEntity menuAsignado = this.menuRepository.findById(x.getMenuEntity().getIdMenu());
					
					lstMenuXPerfilActualDTO.add(MenuDTO.builder()
											  .idMenu(menuAsignado.getIdMenu())
											  .nombreMenu(menuAsignado.getNombreMenu())
											  .rutaUrl(menuAsignado.getRutaUrl())
											  .nombreDocumento(menuAsignado.getNombreDocumento())
											  .iconoMenu(menuAsignado.getIconoMenu())
											  .moduloMenu(menuAsignado.getModuloMenu())
											  .subMenu(menuAsignado.getSubMenu())
											  .nombreClaseView(menuAsignado.getNombreClaseView())
											  .numeroItem(menuAsignado.getNumeroItem())
											  .idEstado(menuAsignado.getIdEstado())
											  .build());
					
				});
				
			}
			
			if ( lstMenuXPerfilActualDTO != null )
				return lstMenuXPerfilActualDTO;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public MenuDTO consultarMenuXId(Integer idMenu) {
		try {
			
			MenuEntity menuEntity = this.menuRepository.findById(idMenu);
			
			MenuDTO menuDTO = MenuDTO.builder()
									 .idMenu(menuEntity.getIdMenu())
									 .nombreMenu(menuEntity.getNombreMenu())
									 .rutaUrl(menuEntity.getRutaUrl())
									 .nombreDocumento(menuEntity.getNombreDocumento())
									 .iconoMenu(menuEntity.getIconoMenu())
									 .moduloMenu(menuEntity.getModuloMenu())
									 .subMenu(menuEntity.getSubMenu())
									 .nombreClaseView(menuEntity.getNombreClaseView())
									 .numeroItem(menuEntity.getNumeroItem())
									 .idEstado(menuEntity.getIdEstado())
									 .build();
			
			if ( menuDTO != null && !Objects.isNull(menuDTO) )
				return menuDTO;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void eliminarTodosLosMenusAsociadosAPerfil(Integer idPerfil) {
		try {
			
			List<MenuPerfilEntity> lstMenuXPerfilActualEntity = this.menuPerfilRepository.consultarMenuPerfilXCodigoPerfil(idPerfil);
			
			if ( lstMenuXPerfilActualEntity != null && !lstMenuXPerfilActualEntity.isEmpty() ) {
				
				lstMenuXPerfilActualEntity.stream().forEach(x -> {
					this.menuPerfilRepository.delete(x.getIdMenuPerfil());
				});
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean actualizarMenusXPerfil(Integer idPeril, List<MenuDTO> lstMenusAsignados, String usuarioCreacion) {
		try {
			
			Integer menusGuardados = 0;
			
			if ( lstMenusAsignados != null && !lstMenusAsignados.isEmpty() ) {
				
				for ( MenuDTO menu : lstMenusAsignados ) {
					
					MenuPerfilEntity menuPerfilCreado = this.menuPerfilRepository.create(
							MenuPerfilEntity.builder()
											.menuEntity(this.menuRepository.findById(menu.getIdMenu()))
											.perfilEntity(this.perfilRepository.findById(idPeril))
											.idEstado("A")
											.fechaCreacion(new Date())
											.usuarioCreacion(usuarioCreacion)
											.build()
							);
					
					if ( menuPerfilCreado != null && menuPerfilCreado.getIdMenuPerfil() != null )
						menusGuardados++;
					
				}
				
			}
			
			if ( menusGuardados > 0 )
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

}
