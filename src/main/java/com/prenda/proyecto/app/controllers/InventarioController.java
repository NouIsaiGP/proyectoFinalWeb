package com.prenda.proyecto.app.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.prenda.proyecto.app.models.entity.Avio;
import com.prenda.proyecto.app.models.entity.MoldeFalda;
import com.prenda.proyecto.app.models.entity.MoldePantalon;
import com.prenda.proyecto.app.models.entity.Prenda;
import com.prenda.proyecto.app.models.entity.PrendaServicio;
import com.prenda.proyecto.app.models.entity.Producto;
import com.prenda.proyecto.app.models.entity.Servicio;
import com.prenda.proyecto.app.models.entity.Tela;
import com.prenda.proyecto.app.models.service.IAvioService;
import com.prenda.proyecto.app.models.service.IMoldeFaldaService;
import com.prenda.proyecto.app.models.service.IMoldePantalonService;
import com.prenda.proyecto.app.models.service.IPrendaService;
import com.prenda.proyecto.app.models.service.IServicioService;
import com.prenda.proyecto.app.models.service.ITelaService;
import com.prenda.proyecto.app.util.paginator.PageRender;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;

@Controller
@SessionAttributes("prenda")
public class InventarioController {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IAvioService avioService;

	@Autowired
	private ITelaService telaService;

	@Autowired
	private IMoldePantalonService pantalonService;

	@Autowired
	private IMoldeFaldaService faldaService;

	@Autowired
	private IServicioService servicioService;

	@Autowired
	private IPrendaService prendaService;

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = { "inventario", "/inventario/avios", "/inventario/telas",
			"/inventario/prendas" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "page2", defaultValue = "0") int page2,
			@RequestParam(name = "page3", defaultValue = "0") int page3,
			@RequestParam(name = "error", required = false) String error,
			Model model, Authentication authentication,
			HttpServletRequest request) {

		Pageable pageRequest = PageRequest.of(page, 3);
		Page<Avio> avios = null;

		avios = avioService.findAllEnable(pageRequest);
		PageRender<Avio> pageRender = new PageRender<Avio>("/inventario/avios", avios);

		model.addAttribute("error", error);
		model.addAttribute("avios", avios);
		model.addAttribute("avio", new Avio());
		model.addAttribute("page", pageRender);

		Pageable pageRequest2 = PageRequest.of(page2, 3);
		Page<Tela> telas = null;

		telas = telaService.findAllEnable(pageRequest2);
		PageRender<Tela> pageRender2 = new PageRender<Tela>("/inventario/telas", telas);

		model.addAttribute("telas", telas);
		model.addAttribute("tela", new Tela());
		model.addAttribute("page2", pageRender2);

		Pageable pageRequest3 = PageRequest.of(page3, 3);
		Page<Prenda> prendas = null;

		prendas = prendaService.findAllEnable(pageRequest3);
		PageRender<Prenda> pageRender3 = new PageRender<Prenda>("/inventario/prendas", prendas);

		model.addAttribute("prendas", prendas);
		model.addAttribute("prenda", new Prenda());
		model.addAttribute("page3", pageRender3);

		return "inventario/inventario";

	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/inventario/buscar")
	public String findAll(@RequestParam(defaultValue = "04") int mes, @RequestParam(defaultValue = "2021") int año,
			@RequestParam(name = "page", defaultValue = "0") int page, Model model) throws ParseException {

		String fech = año + "-0" + mes + "-";

		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Prenda> prendas = prendaService.findByKeyWord(fech, pageRequest);
		PageRender<Prenda> pageRender = new PageRender<Prenda>("/prenda/buscar?keyword=" + fech, prendas);

		model.addAttribute("prendas", prendas);
		model.addAttribute("prenda", new Prenda());
		model.addAttribute("page", pageRender);

		return "inventario/buscar";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/inventario/ver/{id}")
	public String ver(@PathVariable(value = "id") Integer id, Map<String, Object> model, RedirectAttributes flash) {

		Prenda prenda = prendaService.findOne(id);
		if (prenda == null) {
			flash.addFlashAttribute("error", "La prenda no existe en la base de datos");
			return "redirect:/inventario/inventario";
		}

		List<PrendaServicio> lista = prendaService.findByPrenda(prenda);
		List<Servicio> listaServicios = new ArrayList<Servicio>();
		List<Avio> avios = new ArrayList<Avio>();
		Tela tela = new Tela();
		

		for (PrendaServicio e : lista) {

			if (!listaServicios.contains(e.getServicio())) {
				listaServicios.add(e.getServicio());
			}
			Avio avio = e.getAvio();
			if(avio != null) {
				avio.setCantidad(e.getCantidad());
				avios.add(avio);
			}
			
				

		}
		
		
		
		tela=lista.get(0).getTela();
		int rollos = prenda.getCantidadTela();
		
		
		model.put("prenda", prenda);
		model.put("tela", tela);
		model.put("rollos", rollos);
		model.put("avios", avios);
		model.put("servicios", listaServicios);
		model.put("producto", new Producto());
		
		model.put("titulo", "Detalle de prenda: " + prenda.getNombre());
		return "inventario/ver";
	}

	@GetMapping(value = "/inventario/cargar-tela/{term}", produces = { "application/json" })
	public @ResponseBody List<Tela> cargarTelas(@PathVariable String term) {
		return prendaService.findTelaByName(term);
	}

	@GetMapping(value = "/inventario/cargar-servicio/{term}", produces = { "application/json" })
	public @ResponseBody List<Servicio> cargarServicios(@PathVariable String term) {
		return prendaService.findServiciobyTipo(term);
	}

	@GetMapping(value = "/inventario/cargar-avio/{term}", produces = { "application/json" })
	public @ResponseBody List<Avio> cargarAvioss(@PathVariable String term) {
		return prendaService.findAvioByTerm(term);
	}

	@GetMapping(value = "/inventario/cargar-pantalon/{term}", produces = { "application/json" })
	public @ResponseBody List<MoldePantalon> cargarPantalon(@PathVariable String term) {
		return prendaService.findPantalonByTerm(term);
	}

	@GetMapping(value = "/inventario/cargar-falda/{term}", produces = { "application/json" })
	public @ResponseBody List<MoldeFalda> cargarFalda(@PathVariable String term) {
		return prendaService.findFaldaByTerm(term);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "inventario/form", method = RequestMethod.GET)
	public String crearPantalon(Map<String, Object> model) {

		Prenda prenda = new Prenda();
		model.put("prenda", prenda);
		model.put("titulo", "Crear Prenda");

		return "inventario/form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "inventario/formfalda")
	public String crearFalda(Map<String, Object> model) {

		Prenda prenda = new Prenda();
		model.put("prenda", prenda);
		model.put("titulo", "Crear Prenda");

		return "inventario/formfalda";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = { "inventario/mover" }, method = RequestMethod.GET)
	public String mover(@RequestParam String keyword, Model model, RedirectAttributes flash) {
		Prenda prenda = new Prenda();
		if (keyword.equals("falda")) {
			model.addAttribute("prenda", prenda);

			return "redirect:/inventario/formfalda";
		} else if (keyword.equals("pantalon")) {
			model.addAttribute("prenda", prenda);

			return "redirect:/inventario/form";
		}
		flash.addFlashAttribute("error", "Ocurrio un problema");
		return "redirect:/molde/moldes";
	}

	/*
	 * @METODOS DE GUARDADO: se toma el ancho del pantalon o el largo de falda para
	 * conseguir el ancho que se usara en la tela luego se calcula el total de
	 * prendas por rollo
	 * 
	 * @AVIOS se busca el avio por el id, al guardar la PrendaServicio se guarda un
	 * registro por avio y este prende un trigger restando las cantidades usadas, si
	 * las cantidades no pueden ser usadas marca error
	 * 
	 * @SERVICIOS se busca el servicio por id , al guardar la PrendaServicio se
	 * gurda un registro por servicio, si falta el servicio de Corte o Costura marca
	 * error
	 * 
	 * @TELA se busca la tela por el id, al guardar la Prenda, se dispara un trigger
	 * para eliminar la cantidad de telas usadas
	 * 
	 */

	@RequestMapping(value = "/inventario/formfalda", method = RequestMethod.POST)
	public String guardarDatosFalda(@RequestParam(name = "tela_id[]", required = false) Integer idTela[],
			@RequestParam(name = "avio_id[]", required = false) Integer idAvio[],
			@RequestParam(name = "cantidadTela[]", required = false) Integer cantidadTela[],
			@RequestParam(name = "cantidadAvio[]", required = false) Integer cantidadAvio[],
			@RequestParam(name = "ser_id[]", required = false) Integer idServicios[],
			@RequestParam(name = "falda_id[]", required = false) Integer idFalda[], RedirectAttributes flash,
			@RequestParam(name = "nombre", required = false) String nombre) {
		
		List<Prenda> pr = prendaService.findPrendaByName(nombre);
		boolean existe = false;
		for(Prenda p : pr) {
			if(p.getNombre().equals(nombre)) existe = true;
		}
		if(existe) {
			flash.addAttribute("error", "Hubo un problema al crear la prenda, El nombre que uso ya existe");
			return "redirect:/inventario";
		}
		
		if(idAvio == null || idFalda == null || idServicios == null || idTela == null || nombre == "") {
			flash.addAttribute("error", "Hubo un problema al crear la prenda, uno o mas campos fueron vacios");
			return "redirect:/inventario";
		}
		
		

		MoldeFalda molde = faldaService.findOne(idFalda[0]);

		double ancho = molde.getAnchoFalda() + 5;
		double largo = molde.getLargoFalda() + 5;
		int redondeado = (int) Math.round(largo);

		Double largoUsado = ancho + 3;

		Tela tela = telaService.findOne(idTela[0]);
		
		if(tela.getCantidad() < cantidadTela[0]) {
			flash.addAttribute("error", "Hubo un problema, Las existencias de la tela que eligio no alcanza para la creacion de la prenda");
			return "redirect:/inventario";
		}

		int largoTela = Integer.parseInt(tela.getLargo()) * 100;
		int largoTelaTotal = largoTela * cantidadTela[0];

		int totalPrendas = largoTelaTotal / redondeado;

		List<Avio> listaAvios = new ArrayList<Avio>();

		String disponible = "si";
		Avio avioAux = new Avio();
		double precioAvio = 0;

		for (int i = 0; i < idAvio.length; i++) {
			avioAux = avioService.findOne(idAvio[i]);
			listaAvios.add(avioAux);
		}

		for (Avio a : listaAvios) {
			logger.info("Avio: " + a.getNombre() + " cantidad " + a.getCantidad() + " precio " + a.getCosto());
		}
		for (int i = 0; i < cantidadAvio.length; i++) {
			logger.info("cant por falda: " + i + " cantidad " + cantidadAvio[i]);
		}
		for (int i = 0; i < cantidadAvio.length; i++) {

			logger.info(i);

			int cantidadEnFalda = cantidadAvio[i] * totalPrendas;
			logger.info("cantidad en falda " + cantidadEnFalda + " cantida por avio " + cantidadAvio[i]);

			double aux6 = listaAvios.get(i).getCosto() * cantidadEnFalda;
			logger.info("costo x cantidad en falda " + aux6);
			precioAvio = precioAvio + aux6;

			logger.info("precio avio" + aux6);
			int aux = listaAvios.get(i).getCantidad() - cantidadEnFalda;
			logger.info("cant-----------: " + aux);
			if (aux < 0) {
				disponible = "no";
				break;
			}
		}

		if (disponible.equals("no")) {
			flash.addFlashAttribute("error",
					"Hubo un problema al crear la prenda, Las existencias de un producto de los avios no alcanza para la creacion de la prenda");
			return "redirect:/inventario";
		}
		logger.info("precio total avios: " + precioAvio);

		Servicio ser = new Servicio();
		List<Servicio> listaServicios = new ArrayList<Servicio>();
		boolean corte = false;
		boolean costura = false;

		for (int i = 0; i < idServicios.length; i++) {
			ser = servicioService.findOne(idServicios[i]);
			listaServicios.add(ser);
		}

		double costoTotalTela = Double.parseDouble(tela.getCosto()) * cantidadTela[0];
		double costoServicios = 0;

		for (Servicio s : listaServicios) {
			if (s.getTipo().getNombre().equals("Corte")) {
				corte = true;
			} else if (s.getTipo().getNombre().equals("Costura")) {
				costura = true;
			} else if (s.getTipo().getNombre().equals("Corte y Costura")) {
				corte = true;
				costura = true;
			}
			logger.info("servicio " + s.getDescripcion() + " " + s.getCosto());
			costoServicios += Double.parseDouble(s.getCosto());
			logger.info("suma servicios " + costoServicios);
		}

		if (corte == false || costura == false) {
			flash.addAttribute("error",
					"Hubo un problema al crear la prenda, Hace falta un servicio necesario (Corte o Costura)");
			return "redirect:/inventario";
		}

		costoServicios = costoServicios * totalPrendas;
		logger.info("costo Servicios total " + costoServicios);

		int aux5 = (int) Math.round(costoServicios);
		int aux3 = (int) Math.round(precioAvio);
		int aux4 = (int) Math.round(costoTotalTela);
		int costoTotalFalda = aux4 + aux5 + aux3;
		int precioXfalda = costoTotalFalda / totalPrendas;

		logger.info("costo total pantalon " + costoTotalFalda + " precio x pantalon " + precioXfalda);
		logger.info("ancho total " + largo);
		logger.info("total prendas " + totalPrendas);

		LocalDate today = LocalDate.now();
		String date = today.toString();

		logger.info("nombre" + nombre);
		Prenda prenda = new Prenda();
		prenda.setNombre(nombre);
		prenda.setFecha(date);
		prenda.setPrecio((double) precioXfalda);
		prenda.setCosto((double) costoTotalFalda);
		prenda.setCantidad(totalPrendas);
		prenda.setCantidadTela(cantidadTela[0]);
		prenda.setTipo("Falda");
		prenda.setEnable("0");

		PrendaServicio ps = new PrendaServicio();

		for (int i = 0; i < cantidadAvio.length; i++) {
			ps = new PrendaServicio();
			ps.setPrenda(prenda);
			ps.setFalda(molde);
			ps.setPantalon(pantalonService.findOne(7));
			ps.setCantidad(cantidadAvio[i] * totalPrendas);
			ps.setTela(tela);
			ps.setAvio(listaAvios.get(i));

			prendaService.save(ps);

		}

		for (Servicio s : listaServicios) {
			ps = new PrendaServicio();
			ps.setPrenda(prenda);
			ps.setFalda(molde);
			ps.setPantalon(pantalonService.findOne(7));
			ps.setTela(tela);
			ps.setServicio(s);
			prendaService.save(ps);
		}
		flash.addAttribute("success", "Se ha creado la prenda con exito");
		
		Tela actualizarTela = telaService.findOne(idTela[0]);
		int telasUsadas = actualizarTela.getCantidad() - cantidadTela[0];
		actualizarTela.setCantidad(telasUsadas);
		telaService.save(actualizarTela);
		
		
		for(int i = 0; i < idAvio.length;i++){	
			Avio actualizar = avioService.findOne(idAvio[i]);
			int cantidadEnFalda = cantidadAvio[i] * totalPrendas;
			int aviosUsados = actualizar.getCantidad() - cantidadEnFalda;
			actualizar.setCantidad(aviosUsados);
			avioService.save(actualizar);
		}

		return "redirect:/inventario";
	}

	@PostMapping("/inventario/form")
	public String guardarDatosPantalon(@RequestParam(name = "tela_id[]", required = false) Integer idTela[],
			@RequestParam(name = "avio_id[]", required = false) Integer idAvio[],
			@RequestParam(name = "cantidadTela[]", required = false) Integer cantidadTela[],
			@RequestParam(name = "cantidadAvio[]", required = false) Integer cantidadAvio[],
			@RequestParam(name = "ser_id[]", required = false) Integer idServicios[],
			@RequestParam(name = "pantalon_id[]", required = false) Integer idPrenda[], RedirectAttributes flash,
			@RequestParam(name = "nombre", required = false) String nombre) {
		
		List<Prenda> pr = prendaService.findPrendaByName(nombre);
		boolean existe = false;
		for(Prenda p : pr) {
			if(p.getNombre().equals(nombre)) existe = true;
		}
		if(existe) {
			flash.addAttribute("error", "Hubo un problema al crear la prenda, El nombre que uso ya existe");
			return "redirect:/inventario";
		}
		
		if(idAvio == null || idPrenda == null || idServicios == null || idTela == null || nombre == "") {
			flash.addAttribute("error", "Hubo un problema al crear la prenda, uno o mas campos fueron vacios");
			return "redirect:/inventario";
		}
		

		MoldePantalon molde = pantalonService.findOne(idPrenda[0]);

		double anchoDelantero = molde.getAnchoCadera() + molde.getAvanceTiro() + 3;
		double anchoTrasero = molde.getAnchoCadera() + molde.getAvanceTiroPT() + 3;
		double aux1 = anchoDelantero * 2;
		double aux2 = anchoTrasero * 2;
		double AnchoTotal = aux1 + aux2;
		int redondeado = (int) Math.round(AnchoTotal);

		Tela tela = telaService.findOne(idTela[0]);
		
		if(tela.getCantidad() < cantidadTela[0]) {
			flash.addAttribute("error", "Hubo un problema, Las existencias de la tela que eligio no alcanza para la creacion de la prenda");
			return "redirect:/inventario";
		}

		int largo = Integer.parseInt(tela.getLargo()) * 100;

		int largoTotal = largo * cantidadTela[0];

		int totalPrendas = largoTotal / redondeado;

		List<Avio> listaAvios = new ArrayList<Avio>();

		String disponible = "si";
		Avio avioAux = new Avio();
		double precioAvio = 0;

		for (int i = 0; i < idAvio.length; i++) {
			avioAux = avioService.findOne(idAvio[i]);
			listaAvios.add(avioAux);
		}

		for (Avio a : listaAvios) {
			logger.info("Avio: " + a.getNombre() + " cantidad " + a.getCantidad() + " precio " + a.getCosto());
		}
		for (int i = 0; i < cantidadAvio.length; i++) {
			logger.info("cant por pantalon: " + i + " cantidad " + cantidadAvio[i]);
		}
		for (int i = 0; i < cantidadAvio.length; i++) {

			logger.info(i);

			int cantidadEnPantalon = cantidadAvio[i] * totalPrendas;
			logger.info("cantidad en pantalon " + cantidadEnPantalon + " cantida por avio " + cantidadAvio[i]);

			double aux6 = listaAvios.get(i).getCosto() * cantidadEnPantalon;
			logger.info("costo x cantidad en pantalon " + aux6);
			precioAvio = precioAvio + aux6;

			logger.info("precio avio" + aux6);
			int aux = listaAvios.get(i).getCantidad() - cantidadEnPantalon;
			logger.info("cant-----------: " + aux);
			if (aux < 0) {
				disponible = "no";
				break;
			}
		}

		if (disponible.equals("no")) {
			flash.addFlashAttribute("error",
					"Hubo un problema al crear la prenda, Las existencias de un producto de los avios no alcanza para la creacion de la prenda");
			return "redirect:/inventario";
		}
		logger.info("precio total avios: " + precioAvio);

		Servicio ser = new Servicio();
		List<Servicio> listaServicios = new ArrayList<Servicio>();
		boolean corte = false;
		boolean costura = false;

		for (int i = 0; i < idServicios.length; i++) {
			ser = servicioService.findOne(idServicios[i]);
			listaServicios.add(ser);
		}

		double costoTotalTela = Double.parseDouble(tela.getCosto()) * cantidadTela[0];
		double costoServicios = 0;

		for (Servicio s : listaServicios) {
			if (s.getTipo().getNombre().equals("Corte")) {
				corte = true;
			} else if (s.getTipo().getNombre().equals("Costura")) {
				costura = true;
			} else if (s.getTipo().getNombre().equals("Corte y Costura")) {
				corte = true;
				costura = true;
			}
			logger.info("servicio " + s.getDescripcion() + " " + s.getCosto());
			costoServicios += Double.parseDouble(s.getCosto());
			logger.info("suma servicios " + costoServicios);
		}

		if (corte == false || costura == false) {
			flash.addAttribute("error",
					"Hubo un problema al crear la prenda, Hace falta un servicio necesario (Corte o Costura)");
			return "redirect:/inventario";
		}

		costoServicios = costoServicios * totalPrendas;
		logger.info("costo Servicios total " + costoServicios);

		int aux5 = (int) Math.round(costoServicios);
		int aux3 = (int) Math.round(precioAvio);
		int aux4 = (int) Math.round(costoTotalTela);
		int costoTotalPantalon = aux4 + aux5 + aux3;
		int precioXpantalon = costoTotalPantalon / totalPrendas;

		logger.info("costo total pantalon " + costoTotalPantalon + " precio x pantalon " + precioXpantalon);
		logger.info("ancho total " + AnchoTotal);
		logger.info("total prendas " + totalPrendas);

		LocalDate today = LocalDate.now();
		String date = today.toString();

		logger.info("nombre" + nombre);
		Prenda prenda = new Prenda();
		prenda.setNombre(nombre);
		prenda.setFecha(date);
		prenda.setPrecio((double) precioXpantalon);
		prenda.setCosto((double) costoTotalPantalon);
		prenda.setCantidad(totalPrendas);
		prenda.setCantidadTela(cantidadTela[0]);
		prenda.setTipo("Pantalon");
		prenda.setEnable("0");

		PrendaServicio ps = new PrendaServicio();

		for (int i = 0; i < cantidadAvio.length; i++) {
			ps = new PrendaServicio();
			ps.setPrenda(prenda);
			ps.setFalda(faldaService.findOne(7));
			ps.setPantalon(molde);
			ps.setCantidad(cantidadAvio[i] * totalPrendas);
			ps.setTela(tela);
			ps.setAvio(listaAvios.get(i));

			prendaService.save(ps);

		}

		for (Servicio s : listaServicios) {
			ps = new PrendaServicio();
			ps.setPrenda(prenda);
			ps.setFalda(faldaService.findOne(7));
			ps.setPantalon(molde);
			ps.setTela(tela);
			ps.setServicio(s);
			prendaService.save(ps);
		}
		flash.addAttribute("success", "Se ha creado la prenda con exito");
		
		Tela actualizarTela = telaService.findOne(idTela[0]);
		int telasUsadas = actualizarTela.getCantidad() - cantidadTela[0];
		actualizarTela.setCantidad(telasUsadas);
		telaService.save(actualizarTela);
		
		
		for(int i = 0; i < idAvio.length;i++){	
			Avio actualizar = avioService.findOne(idAvio[i]);
			int cantidadEnFalda = cantidadAvio[i] * totalPrendas;
			int aviosUsados = actualizar.getCantidad() - cantidadEnFalda;
			actualizar.setCantidad(aviosUsados);
			avioService.save(actualizar);
		}

		return "redirect:/inventario";
	}

}
