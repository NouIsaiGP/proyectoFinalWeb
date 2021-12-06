package com.prenda.proyecto.app.models.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.prenda.proyecto.app.models.dao.IAviosDao;
import com.prenda.proyecto.app.models.dao.IMoldeFaldaDao;
import com.prenda.proyecto.app.models.dao.IMoldePantalonDao;
import com.prenda.proyecto.app.models.dao.IPrendaDao;
import com.prenda.proyecto.app.models.dao.IPrendaServicioDao;
import com.prenda.proyecto.app.models.dao.IServicioDao;
import com.prenda.proyecto.app.models.dao.ITelaDao;
import com.prenda.proyecto.app.models.entity.Avio;
import com.prenda.proyecto.app.models.entity.MoldeFalda;
import com.prenda.proyecto.app.models.entity.MoldePantalon;
import com.prenda.proyecto.app.models.entity.Prenda;
import com.prenda.proyecto.app.models.entity.PrendaServicio;
import com.prenda.proyecto.app.models.entity.Servicio;
import com.prenda.proyecto.app.models.entity.Tela;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrendaServiceImpl implements IPrendaService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private IPrendaDao dao;
    
    @Autowired 
    private ITelaDao telaDao;
    
    @Autowired 
    private IServicioDao servicioDao;
    
    @Autowired 	
    private IPrendaServicioDao prendaDao;
    
    @Autowired
    private IAviosDao avioDao;
    
    @Autowired
    private IMoldeFaldaDao faldaDao;
    
    @Autowired
    private IMoldePantalonDao pantalonDao;

    @Override
    @Transactional
    public Page<Prenda> findAllEnable(Pageable pageable) {
        // TODO Auto-generated method stub
        return dao.findAllDisponibles(pageable);
    }

    @Override
    @Transactional
    public void save(Prenda prenda) {
        // TODO Auto-generated method stub
        dao.save(prenda);
    }

    @Override
    @Transactional
    public Prenda findOne(Integer id) {
        // TODO Auto-generated method stub
        return dao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        // TODO Auto-generated method stub
        dao.deshabilitar(id);
    }

    @Override
    @Transactional
    public Page<Prenda> findByKeyWord(String keyword, Pageable pageable) {
        // TODO Auto-generated method stub
        return dao.findAllByFecha(keyword, pageable);
    }

	@Override
	@Transactional
	public void save(PrendaServicio prenda) {
		prendaDao.save(prenda);
		
	}

	@Override
	@Transactional
	public PrendaServicio findOneServicio(Integer id) {
		return prendaDao.findById(id).orElse(null);
	}

	@Override
	public List<PrendaServicio> findByPrenda(Prenda prenda) {
		// TODO Auto-generated method stub
		return prendaDao.findByPrenda(prenda);
	}

	@Override
	public List<Tela> findTelaByName(String term) {
		// TODO Auto-generated method stub
		return telaDao.findByNombreLikeIgnoreCase("%"+term+"%");
	}

	@Override
	public List<Servicio> findServiciobyTipo(String term) {
		// TODO Auto-generated method stub
		return servicioDao.findByTipo(term);
	}

	@Override
	public List<Avio> findAvioByTerm(String term) {
		// TODO Auto-generated method stub
		return avioDao.findByTerm(term);
	}

	@Override
	public List<MoldePantalon> findPantalonByTerm(String term) {
		// TODO Auto-generated method stub
		return pantalonDao.findByKeyName(term);
	}

	@Override
	public List<MoldeFalda> findFaldaByTerm(String term) {
		// TODO Auto-generated method stub
		return faldaDao.findByKeyName(term);
	}

	@Override
	public List<Prenda> findPrendaByName(String name) {
		// TODO Auto-generated method stub
		return dao.findByNombre(name);
	}

}