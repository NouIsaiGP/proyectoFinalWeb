package com.prenda.proyecto.app.models.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prenda.proyecto.app.models.dao.ITelaDao;
import com.prenda.proyecto.app.models.entity.Tela;

@Service
public class TelaServiceImpl implements ITelaService{
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private ITelaDao telaDao;

	@Override
	@Transactional
	public Page<Tela> findAllEnable(Pageable pageable) {
		return telaDao.findAllDisponibles(pageable);
	}

	@Override
	@Transactional
	public void save(Tela tela) {
		telaDao.save(tela);
	}

	@Override
	@Transactional
	public Tela findOne(Integer id) {
		return telaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		telaDao.deshabilitarTela(id);
	}

	@Override
	@Transactional
	public Page<Tela> findByKeyWord(String keyword, Pageable pageable) {
		return telaDao.findByKeyword(keyword, pageable);
	}

	@Override
	@Transactional
	public List<Tela> findByKeyWord(String keyword) {
		return telaDao.findByKeyword2(keyword);
	}

}
