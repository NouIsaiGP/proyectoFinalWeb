package com.prenda.proyecto.app.controllers;

import com.prenda.proyecto.app.models.entity.Avio;
import com.prenda.proyecto.app.models.entity.MoldeFalda;
import com.prenda.proyecto.app.models.entity.MoldePantalon;
import com.prenda.proyecto.app.models.entity.Prenda;
import com.prenda.proyecto.app.models.entity.PrendaServicio;
import com.prenda.proyecto.app.models.entity.Servicio;
import com.prenda.proyecto.app.models.entity.Tela;
import com.prenda.proyecto.app.models.service.IMoldeFaldaService;
import com.prenda.proyecto.app.models.service.IMoldePantalonService;
import com.prenda.proyecto.app.util.paginator.PageRender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("molde")
public class MoldeController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private IMoldeFaldaService faldaService;

    @Autowired
    private IMoldePantalonService pantalonService;

    @RequestMapping(value = { "molde/mover" }, method = RequestMethod.GET)
    public String mover(@RequestParam String keyword, Model model, RedirectAttributes flash) {
        if (keyword.equals("falda")) {

            return "redirect:/molde/moldefalda";
        } else if (keyword.equals("pantalon")) {

            return "redirect:/molde/moldepantalon";
        }
        flash.addFlashAttribute("error", "Ocurrio un problema");
        return "redirect:/molde/moldes";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "molde/moldepantalon")
    public String crearPantalon(Map<String, Object> model) {

        MoldePantalon pantalon = new MoldePantalon();
        model.put("pantalon", pantalon);
        model.put("titulo", "Crear Molde");

        return "molde/moldepantalon";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "molde/moldepantalon/{id}")
    public String editarPantalon(@PathVariable(value = "id") Integer id, Map<String, Object> model,
            RedirectAttributes flash) {
        MoldePantalon pantalon = null;

        if (id > 0) {
            pantalon = pantalonService.findOne(id);
            if (pantalon == null) {
                flash.addFlashAttribute("error", "El ID del Molde no existe en la BBDD!");
                return "redirect:/molde/moldes";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del molde no puede ser cero!");
            return "redirect:/molde/moldes";
        }

        model.put("pantalon", pantalon);
        model.put("titulo", "Editar Molde");
        return "molde/moldepantalon";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "molde/moldepantalonpt", method = RequestMethod.GET)
    public String crearPantalonPT(@Valid MoldePantalon pantalon,
            Map<String, Object> model) {
        model.put("pantalon", pantalon);

        return "molde/moldepantalonpt";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "molde/moldepantalonpt", method = RequestMethod.POST)
    public String guardarPantalon(MoldePantalon pantalon, BindingResult result, Model model,
            RedirectAttributes flash, SessionStatus status) {
    	
    	if(pantalon == null) {
    		flash.addFlashAttribute("error", "Hubo un problema");//
    		return "redirect:/molde/moldes";
    	}
    	
    	if(pantalon.getAnchoCadera() < 21 || pantalon.getAnchoCadera() > 38) {
    		flash.addFlashAttribute("error", "Hubo un problema, Los rangos del ancho de cadera son 21-38");
    		return "redirect:/molde/moldepantalon";
    	}else if(pantalon.getAvanceTiro() < 5 || pantalon.getAvanceTiro() > 9) {
    		flash.addFlashAttribute("error", "Hubo un problema, Los rangos del avance del tiro son 5-9");
    		return "redirect:/molde/moldepantalon";
    	}else if(pantalon.getAvanceTiroPT() < 3 || pantalon.getAvanceTiroPT() > 7) {
    		flash.addFlashAttribute("error", "Hubo un problema, Los rangos del avence por el tiro en la parte trasera son 3-7");
    		return "redirect:/molde/moldepantalon";
    	}else if(pantalon.getContornoCinturaPT() < 21 || pantalon.getContornoCinturaPT() > 35) {
    		flash.addFlashAttribute("error", "Hubo un problema, Los rangos de contorno de la cintura de la parte trasera son 21-35");
    		return "redirect:/molde/moldepantalon";
    	}else if(pantalon.getContornoRodilla() < 10 || pantalon.getContornoRodilla() > 18) {
    		flash.addFlashAttribute("error", "Hubo un problema, Los rangos de contorno de la rodilla don 10-18");
    		return "redirect:/molde/moldepantalon";
    	}else if(pantalon.getContornoTobillo() < 9.5 || pantalon.getContornoTobillo() > 16.5) {
    		flash.addFlashAttribute("error", "Hubo un problema, Los rangos de contorno de tobillos son 9.5-16.5");
    		return "redirect:/molde/moldepantalon";
    	}else if(pantalon.getCuartoCintura() < 17 || pantalon.getCuartoCintura() > 31) {
    		flash.addFlashAttribute("error", "Hubo un problema, Los rangos de un cuarto de cintura son 17-31");
    		return "redirect:/molde/moldepantalon";
    	}else if(pantalon.getEntradaCintura() < 3 || pantalon.getEntradaCintura() > 5.4) {
    		flash.addFlashAttribute("error", "Hubo un problema, Los rangos de entrada desde la cintura son 3-5.4");
    		return "redirect:/molde/moldepantalon";
    	}else if(pantalon.getLargoPantalon() < 104 || pantalon.getLargoPantalon() >112) {
    		flash.addFlashAttribute("error", "Hubo un problema, Los rangos del largo de pantalon son 104-112");
    		return "redirect:/molde/moldepantalon";
    	}else if(pantalon.getLargoPinza() < 7.5 || pantalon.getLargoPinza() > 8) {
    		flash.addFlashAttribute("error", "Hubo un problema, Los rangos del largo de pinza son 7.5-8");
    		return "redirect:/molde/moldepantalon";
    	}else if(pantalon.getLargoRodilla() < 57 || pantalon.getLargoRodilla() > 65) {
    		flash.addFlashAttribute("error", "Hubo un problema, Los rangos del largo de rodillos son 57-65");
    		return "redirect:/molde/moldepantalon";
    	}else if(pantalon.getLargoTiro() < 22 || pantalon.getLargoTiro() > 29) {
    		flash.addFlashAttribute("error", "Hubo un problema, Los rangos del largo de pinza son 22-29");
    		return "redirect:/molde/moldepantalon";
    	}else if(pantalon.getSaleCostadoPT() < 2.5 || pantalon.getSaleCostadoPT() > 4.5) {
    		logger.info("sale " + pantalon.getSaleCostadoPT());
    		flash.addFlashAttribute("error", "Hubo un problema, Los rangos del sale de costado son 2.5-4.5");
    		return "redirect:/molde/moldepantalon";
    	}
    	

        String mensajeFlash = (pantalon.getId() != null) ? "Molde editado con éxito!" : "Molde creado con éxito!";

        pantalon.setEnable("0");
        pantalonService.save(pantalon);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);

        return "redirect:/molde/moldes";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "molde/moldes/eliminarp/{id}")
    public String eliminarPantalon(@PathVariable(value = "id") Integer id, RedirectAttributes flash) {

        if (id > 0) {
            MoldePantalon pantalon = pantalonService.findOne(id);

            pantalonService.delete(id);
            flash.addFlashAttribute("success", "Molde eliminado con éxito!");

        }
        return "redirect:/molde/moldes";
    }

    @RequestMapping(value = { "molde/moldes", "/molde/moldes/falda",
            "/molde/moldes/pantalon", "moldes" }, method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "page2", defaultValue = "0") int page2, Model model, Authentication authentication,
            HttpServletRequest request) {

        Pageable pageRequest = PageRequest.of(page, 2);
        Page<MoldeFalda> faldas = null;

        faldas = faldaService.findAllEnable(pageRequest);
        PageRender<MoldeFalda> pageRender = new PageRender<MoldeFalda>("/molde/moldes/falda", faldas);

        model.addAttribute("faldas", faldas);
        model.addAttribute("falda", new MoldeFalda());
        model.addAttribute("page", pageRender);

        Pageable pageRequest2 = PageRequest.of(page2, 2);
        Page<MoldePantalon> pantalones = null;

        pantalones = pantalonService.findAllEnable(pageRequest2);
        PageRender<MoldePantalon> pageRender2 = new PageRender<MoldePantalon>("/molde/moldes/pantalon", pantalones);

        model.addAttribute("pantalones", pantalones);
        model.addAttribute("pantalon", new MoldePantalon());
        model.addAttribute("page2", pageRender2);

        return "molde/moldes";

    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "molde/moldefalda")
    public String crearFalda(Map<String, Object> model) {

        MoldeFalda falda = new MoldeFalda();
        model.put("falda", falda);
        model.put("titulo", "Crear Falda");

        return "molde/moldefalda";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "molde/moldefalda/{id}")
    public String editarFalda(@PathVariable(value = "id") Integer id, Map<String, Object> model,
            RedirectAttributes flash) {
        MoldeFalda falda = null;

        if (id > 0) {
            falda = faldaService.findOne(id);
            if (falda == null) {
                flash.addFlashAttribute("error", "El ID del Molde no existe en la BBDD!");
                return "redirect:/molde/moldes";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del molde no puede ser cero!");
            return "redirect:/molde/moldes";
        }

        model.put("falda", falda);
        model.put("titulo", "Editar Falda");
        return "molde/moldefalda";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "molde/moldefalda", method = RequestMethod.POST)
    public String guardarFalda(MoldeFalda falda, BindingResult result, Model model, RedirectAttributes flash,
            SessionStatus status) {
    	
    	
    	
    	
    	if (falda.getLargoFalda() < 45.0 || falda.getLargoFalda() > 65.0) {
    		flash.addFlashAttribute("error", "Hubo un problema, Los rangos del largo de falda son 45-65");//
    		return "redirect:/molde/moldefalda";
    		
        } else if (falda.getCuartoCadera() < 15 || falda.getCuartoCadera() > 26.5) {
        	flash.addFlashAttribute("error", "Hubo un problema, Los rangos del cuarto de cadera son 21.7-32.3");
    		return "redirect:/molde/moldefalda";
    		
        } else if (falda.getCuartoCintura() < 21.7 || falda.getCuartoCintura() > 32.5) {
        	flash.addFlashAttribute("error", "Hubo un problema, Los rangos de cuarto de cintura son 26.5-15");//
    		return "redirect:/molde/moldefalda";
    		
        } else if (falda.getAumentoPinza() < 2 || falda.getAumentoPinza() > 5) {
        	flash.addFlashAttribute("error", "Hubo un problema, Los rangos del aumento de pinza son 2-5");//
    		return "redirect:/molde/moldefalda";
    		
        } else if (falda.getLargoCadera() < 18 || falda.getLargoCadera() > 25) {
        	flash.addFlashAttribute("error", "Hubo un problema, Los rangos del largo de falda son 18-25");//
    		return "redirect:/molde/moldefalda";
    		
        } else if(falda.getNombre().equals("")) {
        	flash.addFlashAttribute("error", "Hubo un problema, debe de ingresar el nombre");//
    		return "redirect:/molde/moldefalda";
        }

        String mensajeFlash = (falda.getId() != null) ? "Molde editado con éxito!" : "Molde creado con éxito!";

        falda.setEnable("0");
        falda.setAnchoFalda(falda.getCuartoCadera());
        falda.setCurvaCintura(1.0);
        falda.setCurvaCostado(0.5);

        faldaService.save(falda);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);

        return "redirect:/molde/moldes";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "molde/moldes/eliminarf/{id}")
    public String eliminarFalda(@PathVariable(value = "id") Integer id, RedirectAttributes flash) {

        if (id > 0) {
            MoldeFalda falda = faldaService.findOne(id);

            faldaService.delete(id);
            flash.addFlashAttribute("success", "Molde eliminado con éxito!");

        }
        return "redirect:/molde/moldes";
    }

    @GetMapping({ "/molde/buscar", "/molde/buscar/falda", "/molde/buscar/pantalon"  })
    public String findAll(@RequestParam int page2, @RequestParam String keyword, @RequestParam int page,
            Model model) {

        Pageable pageRequest = PageRequest.of(page, 2);
        Page<MoldeFalda> faldas = null;

        faldas = faldaService.findAllEnable(pageRequest);
        PageRender<MoldeFalda> pageRender = new PageRender<MoldeFalda>("/molde/moldes/falda?keyword=" + keyword, faldas);

        model.addAttribute("faldas", faldas);
        model.addAttribute("falda", new MoldeFalda());
        model.addAttribute("page", pageRender);

        Pageable pageRequest2 = PageRequest.of(page2, 2);
        Page<MoldePantalon> pantalones = null;

        pantalones = pantalonService.findAllEnable(pageRequest2);
        PageRender<MoldePantalon> pageRender2 = new PageRender<MoldePantalon>("/molde/moldes/pantalon?keyword=" + keyword, pantalones);

        model.addAttribute("pantalones", pantalones);
        model.addAttribute("pantalon", new MoldePantalon());
        model.addAttribute("page2", pageRender2);

        return "/molde/buscar";
    }
    
    @Secured("ROLE_ADMIN")
	@GetMapping(value = "/molde/ver/{id}")
	public String ver(@PathVariable(value = "id") Integer id, Map<String, Object> model, RedirectAttributes flash) {

		MoldePantalon molde = pantalonService.findOne(id);
		model.put("molde", molde);
		
		model.put("titulo", "Detalle de prenda: " + molde.getNombre());
		return "molde/ver";
	}
    
    @Secured("ROLE_ADMIN")
	@GetMapping(value = "/molde/verfalda/{id}")
	public String verFalda(@PathVariable(value = "id") Integer id, Map<String, Object> model, RedirectAttributes flash) {

		MoldeFalda molde = faldaService.findOne(id);
		model.put("molde", molde);
		
		model.put("titulo", "Detalle de prenda: " + molde.getNombre());
		return "molde/verfalda";
	}

}
