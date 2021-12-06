package com.prenda.proyecto.app.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.prenda.proyecto.app.models.dao.ProductosRepository;
import com.prenda.proyecto.app.models.dao.ProductosVendidosRepository;
import com.prenda.proyecto.app.models.dao.VentasRepository;
import com.prenda.proyecto.app.models.entity.Avio;
import com.prenda.proyecto.app.models.entity.Lote;
import com.prenda.proyecto.app.models.entity.Prenda;
import com.prenda.proyecto.app.models.entity.Producto;
import com.prenda.proyecto.app.models.entity.ProductoParaVender;
import com.prenda.proyecto.app.models.entity.ProductoVendido;
import com.prenda.proyecto.app.models.entity.Tela;
import com.prenda.proyecto.app.models.entity.Usuario;
import com.prenda.proyecto.app.models.entity.Venta;
import com.prenda.proyecto.app.models.service.ILoteService;
import com.prenda.proyecto.app.models.service.JpaUserDetailsService;
import com.prenda.proyecto.app.util.paginator.PageRender;

@Controller
public class AccionesUsuarioController {

	@Autowired
	private JpaUserDetailsService userservice;

	@Autowired
	private ProductosRepository productoDao;

	@Autowired
	private VentasRepository ventasRepository;

	@Autowired
	private ProductosVendidosRepository productosVendidosRepository;

	@Autowired
	private ILoteService loteService;

	protected final Log log = LogFactory.getLog(this.getClass());

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "venta/comprobar/{id}")
	public String comprobar(@PathVariable(value = "id") Integer id, RedirectAttributes flash) {

		if (id > 0) {
			Venta venta = ventasRepository.findById(id).orElse(null);
			venta.setEstado("COMPROBANDO");
			ventasRepository.save(venta);
			flash.addFlashAttribute("success", "Cambio realizado con éxito!");

		}
		return "redirect:/ventas/ventas";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "venta/enviado/{id}")
	public String enviado(@PathVariable(value = "id") Integer id, RedirectAttributes flash) {

		if (id > 0) {
			Venta venta = ventasRepository.findById(id).orElse(null);
			venta.setEstado("ENVIANDO");
			ventasRepository.save(venta);
			flash.addFlashAttribute("success", "Cambio realizado con éxito!");

		}
		return "redirect:/ventas/ventas";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "venta/recibido/{id}")
	public String recibido(@PathVariable(value = "id") Integer id, RedirectAttributes flash) {

		if (id > 0) {
			Venta venta = ventasRepository.findById(id).orElse(null);
			venta.setEstado("RECIBIDO");
			ventasRepository.save(venta);
			flash.addFlashAttribute("success", "Cambio realizado con éxito!");

		}
		return "redirect:/ventas/ventas";
	}

	@RequestMapping(value = { "/usuario/compras", "compras", "/compras" }, method = RequestMethod.GET)
	public String compras(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario user = userservice.findByUser(auth.getName());

		Pageable pageRequest = PageRequest.of(page, 6);
		Page<ProductoVendido> ventas = null;
		ventas = productosVendidosRepository.findAllValidos(user.getId(), pageRequest);
		PageRender<ProductoVendido> pageRender = new PageRender<ProductoVendido>("/usuario/compras", ventas);

		model.addAttribute("ventas", ventas);
		model.addAttribute("venta", new ProductoVendido());
		model.addAttribute("page", pageRender);

		model.addAttribute("usuario", user);

		return "usuario/compras";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "ventas/ventas")
	public String ventas(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 6);
		Page<Venta> ventas = null;
		ventas = productosVendidosRepository.findAllVenta(pageRequest);
		PageRender<Venta> pageRender = new PageRender<Venta>("/ventas/ventas", ventas);
		for (Venta v : ventas.toList()) {
			log.info("Ventas " + v.toString());
		}

		model.addAttribute("ventas", ventas);
		model.addAttribute("venta", new Venta());
		model.addAttribute("page", pageRender);

		return "ventas/ventas";
	}

	@RequestMapping(value = "/ventas/buscar", method = RequestMethod.GET)
	public String buscar(@RequestParam String keyword, @RequestParam(name = "page", defaultValue = "0") int page,
			Model model) {

		Pageable pageRequest = PageRequest.of(page, 6);
		Page<Venta> ventas = productosVendidosRepository.findByKeywordVenta(keyword, pageRequest);
		PageRender<Venta> pageRender = new PageRender<Venta>("/ventas/buscar?keyword=" + keyword, ventas);

		model.addAttribute("keyword", keyword);
		model.addAttribute("ventas", ventas);
		model.addAttribute("venta", new ProductoVendido());
		model.addAttribute("page", pageRender);

		return "ventas/buscar";
	}

	@RequestMapping(value = { "/usuario/configurar", "configurar", "/configurar" }, method = RequestMethod.GET)
	public String ConfigurarForm(Map<String, Object> model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			log.info(
					"Utilizando forma estática SecurityContextHolder.getContext().getAuthentication(): Usuario autenticado: "
							.concat(auth.getName()));
		}

		Usuario usuario = userservice.findByUser(auth.getName());
		if(usuario.getCcv() != null && !usuario.getCcv().equals("")) {
			String ccvDecrypt = decrypt("hola", usuario.getCcv());
			String tarjetaDecrypt = decrypt("hola", usuario.getTarjeta());
			usuario.setTarjeta(tarjetaDecrypt);
			usuario.setCcv(ccvDecrypt);
		}

		model.put("usuario", usuario);
		log.info("get " + usuario.getCcv());

		return "usuario/configurar";
	}

	@PostMapping(value = "/usuario/configurar")
	public String configurarUsuario(Usuario usuario, BindingResult result, Model model, RedirectAttributes flash,
			SessionStatus status) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioExistente = userservice.findByUser(auth.getName());
		log.info("post " + usuario.getCcv());
		log.info("new user " + usuarioExistente.getCcv());

		if (usuario.getCalle().equals("") || usuario.getColonia().equals("") || usuario.getPostal().equals("")) {
			flash.addFlashAttribute("error", "Hubo un problema, Debe Agregar una direccion de envio");
			flash.addFlashAttribute("usuario", usuario);
			return "redirect:/usuario/configurar";
		} else if (usuario.getPostal().length() != 5) {
			flash.addFlashAttribute("error", "Hubo un problema, Ingrese un codigo postal valido");
			flash.addFlashAttribute("usuario", usuario);
			return "redirect:/usuario/configurar";
		} else if (usuario.getTelefono().length() != 10 || usuario.getTelefono().equals("")
				|| Pattern.matches("[a-zA-Z]+", usuario.getTelefono()) == true) {
			flash.addFlashAttribute("error", "Hubo un problema, Ingrese un Telefono valido");
			flash.addFlashAttribute("usuario", usuario);
			return "redirect:/usuario/configurar";
		} else if (usuario.getCad().equals("") || usuario.getCcv().equals("") || usuario.getTarjeta().equals("")) {
			flash.addFlashAttribute("error", "Hubo un problema, Debe llenar los datos de la tarjeta");
			flash.addFlashAttribute("usuario", usuario);
			return "redirect:/usuario/configurar";
		} else if (usuario.getTarjeta().length() != 16 || Pattern.matches("[a-zA-Z]+", usuario.getCcv()) == true) {
			flash.addFlashAttribute("error", "Hubo un problema, Debe llenar los datos de la tarjeta");
			flash.addFlashAttribute("usuario", usuario);
			return "redirect:/usuario/configurar";
		} else if (Pattern.matches("\\d{1,2}-\\d{4}", usuario.getCad()) == false) {
			flash.addFlashAttribute("error", "Hubo un problema, El patron del vencimiento de tarjeta debe ser mm-yyyy");
			flash.addFlashAttribute("usuario", usuario);
			return "redirect:/usuario/configurar";
		}
        String ccvEncrypted = encrypt("hola", usuario.getCcv());
        String TarjetaEncrypted = encrypt("hola", usuario.getTarjeta());

		usuarioExistente.setTelefono(usuario.getTelefono());
		usuarioExistente.setCad(usuario.getCad());
		usuarioExistente.setCalle(usuario.getCalle());
		usuarioExistente.setCcv(ccvEncrypted);
		usuarioExistente.setColonia(usuario.getColonia());
		usuarioExistente.setPostal(usuario.getPostal());
		usuarioExistente.setTarjeta(TarjetaEncrypted);

		userservice.edit(usuarioExistente);

		return "redirect:/index";
	}

	@RequestMapping(value = { "/usuario/carrito", "carrito", "/carrito" }, method = RequestMethod.GET)
	public String Carrito(Model model, HttpServletRequest request) {

		model.addAttribute("producto", new Producto());
		float total = 0;
		model.addAttribute("total", total);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			log.info(
					"Utilizando forma estática SecurityContextHolder.getContext().getAuthentication(): Usuario autenticado: "
							.concat(auth.getName()));
		}

		return "usuario/carrito";
	}

	@PostMapping(value = "/usuario/comprar")
	public String comprar(@RequestParam(name = "id[]", required = false) Integer idProducto[],
			@RequestParam(name = "cantidadProducto[]", required = false) Integer cantidadProducto[],
			RedirectAttributes flash, HttpServletRequest request) {

		List<Producto> productos = new ArrayList<Producto>();
		Producto productoAux = new Producto();

		for (int i = 0; i < idProducto.length; i++) {
			productoAux = productoDao.findById(idProducto[i]).orElse(null);
			productos.add(productoAux);
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Usuario user = userservice.findByUser(auth.getName());

		if (user.getCad() == null || user.getCad().equals("") || user.getCcv() == null || user.getCcv().equals("")
				|| user.getTarjeta() == null || user.getTarjeta().equals("")) {
			flash.addFlashAttribute("error",
					"Hubo un problema, Debe de registrar una forma de pago para poder realizar una compra");
			return "redirect:/index";
		} else if (user.getCalle() == null || user.getColonia() == null || user.getPostal() == null) {
			flash.addFlashAttribute("error",
					"Hubo un problema, Debe de registrar una direccion de envio para poder realizar una compra");
			return "redirect:/index";
		}

		for (Producto p : productos) {
			if (p.sinExistencia()) {
				flash.addFlashAttribute("error",
						"Hubo un problema, :( Lo sentimos un producto que eligio ya no cuenta con existencias");
				return "redirect:/index";
			}
		}

		List<ProductoParaVender> carrito = new ArrayList<ProductoParaVender>();

		Integer total = 0;

		for (int i = 0; i < productos.size(); i++) {

			Float cantidad = (float) cantidadProducto[i];
			ProductoParaVender pro = new ProductoParaVender(productos.get(i).getNombre(), productos.get(i).getCodigo(),
					productos.get(i).getPrecio(), productos.get(i).getExistencia(), productos.get(i).getId(), cantidad);
			total = total + cantidad.intValue() * productos.get(i).getPrecio().intValue();

			carrito.add(pro);
		}

		Venta v = ventasRepository.save(new Venta(user, total, "COMPROBANDO"));

		// Recorrer el carrito
		for (ProductoParaVender productoParaVender : carrito) {
			// Obtener el producto fresco desde la base de datos
			Producto p = productoDao.findById(productoParaVender.getId()).orElse(null);
			System.out.println(p.toString());
			if (p == null)
				continue; // Si es nulo o no existe, ignoramos el siguiente código con continue

			// Le restamos existencia
			p.restarExistencia(productoParaVender.getCantidad());

			// Lo guardamos con la existencia ya restada
			productoDao.save(p);

			// Creamos un nuevo producto que será el que se guarda junto con la venta
			ProductoVendido productoVendido = new ProductoVendido(productoParaVender.getCantidad(),
					productoParaVender.getPrecio(), productoParaVender.getNombre(), productoParaVender.getCodigo(), v);
			// Y lo guardamos

			Lote lote = loteService.findByName(productoVendido.getNombre());
			if (lote.getVentas() == null) {
				lote.setVentas(0);
				lote.setGanancias((float) 0);
			}
			int cant = lote.getVentas() + Math.round(productoVendido.getCantidad());
			int cant2 = lote.getCantidad() - Math.round(productoVendido.getCantidad());
			float ganancias = lote.getGanancias() + productoVendido.getPrecio() * productoVendido.getCantidad();
			lote.setCantidad(cant2);
			lote.setVentas(cant);
			lote.setGanancias(ganancias);
			loteService.save(lote);
			productosVendidosRepository.save(productoVendido);
		}

		return "redirect:/index";
	}

	@GetMapping(value = "/usuario/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		return productoDao.findByKeyWord(term);
	}

	private String encrypt(final String secret, final String data) {

		byte[] decodedKey = Base64.getDecoder().decode(secret);

		try {
			Cipher cipher = Cipher.getInstance("AES");
			// rebuild key using SecretKeySpec
			SecretKey originalKey = new SecretKeySpec(Arrays.copyOf(decodedKey, 16), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, originalKey);
			byte[] cipherText = cipher.doFinal(data.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(cipherText);
		} catch (Exception e) {
			throw new RuntimeException("Error occured while encrypting data", e);
		}

	}

	private String decrypt(final String secret, final String encryptedString) {

		byte[] decodedKey = Base64.getDecoder().decode(secret);

		try {
			Cipher cipher = Cipher.getInstance("AES");
			// rebuild key using SecretKeySpec
			SecretKey originalKey = new SecretKeySpec(Arrays.copyOf(decodedKey, 16), "AES");
			cipher.init(Cipher.DECRYPT_MODE, originalKey);
			byte[] cipherText = cipher.doFinal(Base64.getDecoder().decode(encryptedString));
			return new String(cipherText);
		} catch (Exception e) {
			throw new RuntimeException("Error occured while decrypting data", e);
		}
	}

}
