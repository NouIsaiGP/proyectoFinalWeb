package com.prenda.proyecto.app.models.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prenda.proyecto.app.models.dao.IAviosDao;
import com.prenda.proyecto.app.models.entity.Avio;

@Service
public class AvioServiceImpl implements IAvioService{
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private IAviosDao avioDao;

	@Override
	@Transactional
	public List<Avio> findAll() {
		return (List<Avio>) avioDao.findAll();
	}

	@Override
	@Transactional
	public void save(Avio avio) {
		avioDao.save(avio);
	}

	@Override
	@Transactional
	public Avio findOne(Integer id) {
		
		return avioDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		avioDao.deshabilitarAvio(id);
	}

	@Override
	public List<Avio> findByNombre(String nombre) {
		return avioDao.findByNombre(nombre);
	}

	@Override
	public List<Avio> findByTipo(String tipo) {
		// TODO Auto-generated method stub
		return avioDao.findByTipo(tipo);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Avio> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return avioDao.findAll(pageable);
	}

	@Override
	public Page<Avio> findAllEnable(Pageable pageable) {
		return avioDao.findAllValidos(pageable);
	}

	@Override
	public Page<Avio> findAllByTipo(String tipo, Pageable pageable) {
		return avioDao.findAllByTipo(tipo, pageable);
	}

}
