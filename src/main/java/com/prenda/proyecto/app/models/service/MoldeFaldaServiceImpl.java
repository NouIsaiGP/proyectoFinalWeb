package com.prenda.proyecto.app.models.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prenda.proyecto.app.models.dao.IMoldeFaldaDao;
import com.prenda.proyecto.app.models.entity.MoldeFalda;

@Service
public class MoldeFaldaServiceImpl implements IMoldeFaldaService {
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private IMoldeFaldaDao moldeDao;

	@Override
	@Transactional
	public Page<MoldeFalda> findAllEnable(Pageable pageable) {
		return moldeDao.findAllDisponibles(pageable);
	}

	@Override
	@Transactional
	public void save(MoldeFalda molde) {
		moldeDao.save(molde);
	}

	@Override
	@Transactional
	public MoldeFalda findOne(Integer id) {
		return moldeDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		moldeDao.deshabilitar(id);
	}

	@Override
	@Transactional
	public Page<MoldeFalda> findByKeyWord(String keyword, Pageable pageable) {
		return moldeDao.findByKeyword(keyword, pageable);
	}

	@Override
	public MoldeFalda findByName(String nombre) {
		// TODO Auto-generated method stub
		return moldeDao.findByNombre(nombre);
	}

}
