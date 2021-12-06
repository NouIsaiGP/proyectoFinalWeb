package com.prenda.proyecto.app.models.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prenda.proyecto.app.models.dao.IRoleDao;
import com.prenda.proyecto.app.models.dao.IUsuarioDao;
import com.prenda.proyecto.app.models.entity.Role;
import com.prenda.proyecto.app.models.entity.Usuario;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService{

	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired 
	private IRoleDao roleDao;
	
	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
        Usuario usuario = usuarioDao.findByUsername(username);
        
        if(usuario == null) {
        	logger.error("Error en el Login: no existe el usuario '" + username + "' en el sistema!");
        	throw new UsernameNotFoundException("Username: " + username + " no existe en el sistema!");
        }
        
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        
        for(Role role: usuario.getRoles()) {
        	logger.info("Role: ".concat(role.getAuthority()));
        	authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        
        if(authorities.isEmpty()) {
        	logger.error("Error en el Login: Usuario '" + username + "' no tiene roles asignados!");
        	throw new UsernameNotFoundException("Error en el Login: usuario '" + username + "' no tiene roles asignados!");
        }
        
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}
	
	@Transactional
	public Usuario findByUser(String username) {
		return usuarioDao.findByUsername(username);
	}
	
	@Transactional
	public Usuario findUser(String keyword) {
		return usuarioDao.findUserName(keyword);
	}
	
	@Transactional
	public Usuario findByEmail(String email) {
		return usuarioDao.findByEmail(email);
	}
	
	
	@Transactional
	public void save(Usuario user) {
		usuarioDao.save(user);
		
		Usuario userAux = usuarioDao.findByUsername(user.getUsername());
		List<Role> roles = new ArrayList<>();
		roles.add(new Role("ROLE_INV", userAux));
		roles.add(new Role("ROLE_USER", userAux));
		for(Role rol : roles) {
			roleDao.save(rol);
		}
		
	}
	
	@Transactional
	public void edit(Usuario user) {
		usuarioDao.save(user);
	}
	
	@Transactional
	public void saveAdmin(Usuario user) {
		usuarioDao.save(user);
		
		Usuario userAux = usuarioDao.findByUsername(user.getUsername());
		List<Role> roles = new ArrayList<>();
		roles.add(new Role("ROLE_INV", userAux));
		roles.add(new Role("ROLE_USER", userAux));
		roles.add(new Role("ROLE_ADMIN", userAux));
		for(Role rol : roles) {
			roleDao.save(rol);
		}
		
	}
	
}
