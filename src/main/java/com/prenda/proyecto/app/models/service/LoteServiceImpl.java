package com.prenda.proyecto.app.models.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prenda.proyecto.app.models.dao.IAviosDao;
import com.prenda.proyecto.app.models.dao.ILoteDao;
import com.prenda.proyecto.app.models.entity.Lote;

@Service
public class LoteServiceImpl implements ILoteService{
	
protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private ILoteDao dao;

	@Override
	@Transactional
	public Page<Lote> findAll(Pageable pageable) {
		return dao.findAll(pageable);
	}

	@Override
	@Transactional
	public void save(Lote lote) {
		dao.save(lote);
	}

	@Override
	@Transactional
	public Lote findOne(Integer id) {
		return dao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Page<Lote> findAllByTipo(String tipo, Pageable pageable) {
		return dao.findAllByTipo(tipo, pageable);
	}

	@Override
	public Lote findByName(String name) {
		return dao.findByNombre(name);
	}

	
}
