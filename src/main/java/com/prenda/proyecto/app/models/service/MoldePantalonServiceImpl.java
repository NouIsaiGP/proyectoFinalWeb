package com.prenda.proyecto.app.models.service;

import com.prenda.proyecto.app.models.dao.IMoldePantalonDao;
import com.prenda.proyecto.app.models.entity.MoldePantalon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoldePantalonServiceImpl implements IMoldePantalonService{

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private IMoldePantalonDao pantalonDao;

    @Override
    @Transactional
    public Page<MoldePantalon> findAllEnable(Pageable pageable) {
        return pantalonDao.findAllDisponibles(pageable);
    }

    @Override
    @Transactional
    public void save(MoldePantalon molde) {
        pantalonDao.save(molde);
    }

    @Override
    @Transactional
    public MoldePantalon findOne(Integer id) {
        return pantalonDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        pantalonDao.deshabilitar(id);
    }

    @Override
    @Transactional
    public Page<MoldePantalon> findByKeyWord(String keyword, Pageable pageable) {
        return pantalonDao.findByKeyword(keyword, pageable);
    }

	@Override
	public MoldePantalon findByName(String noae) {
		// TODO Auto-generated method stub
		return pantalonDao.findByNombre(noae);
	}

}
