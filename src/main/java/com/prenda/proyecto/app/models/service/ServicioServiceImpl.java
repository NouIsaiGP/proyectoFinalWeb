package com.prenda.proyecto.app.models.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.prenda.proyecto.app.models.dao.IServicioDao;
import com.prenda.proyecto.app.models.entity.Servicio;

@Service
public class ServicioServiceImpl implements IServicioService{
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private IServicioDao servicioDao;

	@Override
	@Transactional
	public Page<Servicio> findAllEnable(Pageable pageable) {
		return servicioDao.findAllDisponibles(pageable);
	}

	@Override
	@Transactional
	public void save(Servicio servicio) {
		servicioDao.save(servicio);
	}

	@Override
	@Transactional(readOnly = false)
	public Servicio findOne(Integer id) {
		return servicioDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		servicioDao.deshabilitarServicio(id);
	}

	@Override
	@Transactional
	public Page<Servicio> findByKeyWord(String keyword, Pageable pageable) {
		return servicioDao.findByKeyword(keyword, pageable);
	}

	@Override
	public List<Servicio> findByKeyWord(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void update(Servicio servicio) {
		servicioDao.update(servicio.getId(), servicio.getDescripcion(), servicio.getTelefono(),
				servicio.getContacto(), servicio.getCosto(), servicio.getTipo().getId());
	}

}
