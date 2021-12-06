package com.prenda.proyecto.app.controllers.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prenda.proyecto.app.models.dao.ProductosRepository;
import com.prenda.proyecto.app.models.dao.ProductosVendidosRepository;
import com.prenda.proyecto.app.models.dao.VentasRepository;
import com.prenda.proyecto.app.models.entity.Lote;
import com.prenda.proyecto.app.models.entity.Producto;
import com.prenda.proyecto.app.models.entity.ProductoParaVender;
import com.prenda.proyecto.app.models.entity.ProductoVendido;
import com.prenda.proyecto.app.models.entity.Usuario;
import com.prenda.proyecto.app.models.entity.Venta;
import com.prenda.proyecto.app.models.service.ILoteService;
import com.prenda.proyecto.app.models.service.JpaUserDetailsService;
import com.prenda.proyecto.app.models.service.rest.ProductoService;
import com.prenda.proyecto.app.models.service.rest.UsuarioService;
import com.prenda.proyecto.app.util.paginator.PageRender;

@RestController
@RequestMapping("/client")
public class ProductoControllerRest {

	@Autowired
	private ProductoService service;

	@Autowired
	JpaUserDetailsService userService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ProductosRepository productosRepository;

	@Autowired
	private VentasRepository ventasRepository;

	@Autowired
	private ProductosVendidosRepository productosVendidosRepository;

	@Autowired
	private ILoteService loteService;

	private Logger logger = LoggerFactory.getLogger(ProductoControllerRest.class);

	@GetMapping("/listar")
	public List<Map<String, Object>> listar(Model model) {
		return service.listar();
	}

	@GetMapping("/listar/{keyword}")
	public List<Producto> listarKeyWord(@PathVariable(value = "keyword") String keyword, Model model) {
		return productosRepository.findByKeyWord(keyword);
	}

	@RequestMapping(value = "/login/{username}", method = RequestMethod.GET)
	public @ResponseBody Usuario findUsername(@PathVariable String username, Model model) {
		return userService.findByUser(username);
	}

	@RequestMapping(value = { "/tel/{username}" }, method = RequestMethod.GET)
	public String UsuarioJson(@PathVariable(name = "username") String username, Model model) {
		Usuario user = userService.findByUser(username);
		/*if(user.getCcv() != null && !user.getCcv().equals("")) {
			String ccvDecrypt = decrypt("hola", user.getCcv());
			String tarjetaDecrypt = decrypt("hola", user.getTarjeta());
			user.setTarjeta(tarjetaDecrypt);
			user.setCcv(ccvDecrypt);
		}*/
		List<Usuario> usuario = new ArrayList<>();
		usuario.add(user);
		model.addAttribute("usuario", usuario);
		return "/tel";
	}

	@RequestMapping(value = {
			"/usuario/configurar/{username}/{telefono}/{cad}/{calle}/{ccv}/{colonia}/{postal}/{tarjeta}" }, method = RequestMethod.GET)
	public Usuario configurarUsuario(@PathVariable(name = "username") String username,
			@PathVariable(name = "telefono") String telefono, @PathVariable(name = "cad") String cad,
			@PathVariable(name = "calle") String calle, @PathVariable(name = "ccv") String ccv,
			@PathVariable(name = "colonia") String colonia, @PathVariable(name = "postal") String postal,
			@PathVariable(name = "tarjeta") String tarjeta,

			Model model, SessionStatus status) {
		Usuario usuarioExistente = userService.findByUser(username);
		System.out.println("tar " + tarjeta + " ccv " + ccv);
		tarjeta = tarjeta.replaceAll("911", "/");
		ccv = ccv.replaceAll("911", "/");
		System.out.println("tar " + tarjeta + " ccv " + ccv);
		System.out.println(decrypt("hola", tarjeta));
		System.out.println(decrypt("hola", ccv));

		//String ccvEncrypted = encrypt("hola", ccv);
		//String TarjetaEncrypted = encrypt("hola", tarjeta);

		usuarioExistente.setTarjeta(tarjeta);
		usuarioExistente.setCcv(ccv);

		usuarioExistente.setTelefono(telefono);
		usuarioExistente.setCad(cad);
		usuarioExistente.setCalle(calle);
		usuarioExistente.setColonia(colonia);
		usuarioExistente.setPostal(postal);

		userService.edit(usuarioExistente);

		return usuarioExistente;
	}

	@GetMapping(value = "/comprar/{username}/{id}")
	public List<Producto> comprar(@PathVariable(name = "username") String username,
			@PathVariable(name = "id") Integer id) {

		List<Producto> productos = new ArrayList<Producto>();
		Producto productoAux = new Producto();

		productoAux = productosRepository.findById(id).orElse(null);
		productos.add(productoAux);

		Usuario user = userService.findByUser(username);
		List<ProductoParaVender> carrito = new ArrayList<ProductoParaVender>();
		Integer total = 0;
		for (int i = 0; i < productos.size(); i++) {
			Float cantidad = (float) 1;
			ProductoParaVender pro = new ProductoParaVender(productos.get(i).getNombre(), productos.get(i).getCodigo(),
					productos.get(i).getPrecio(), productos.get(i).getExistencia(), productos.get(i).getId(), cantidad);
			total = total + cantidad.intValue() * productos.get(i).getPrecio().intValue();
			carrito.add(pro);
		}

		Venta v = ventasRepository.save(new Venta(user, total, "COMPROBANDO"));
		ProductoVendido productoVendido = null;
		// Recorrer el carrito
		for (ProductoParaVender productoParaVender : carrito) {
			// Obtener el producto fresco desde la base de datos
			Producto p = productosRepository.findById(productoParaVender.getId()).orElse(null);
			System.out.println(p.toString());
			if (p == null)
				continue; // Si es nulo o no existe, ignoramos el siguiente código con continue

			// Le restamos existencia
			p.restarExistencia(productoParaVender.getCantidad());

			// Lo guardamos con la existencia ya restada
			productosRepository.save(p);

			// Creamos un nuevo producto que será el que se guarda junto con la venta
			productoVendido = new ProductoVendido(productoParaVender.getCantidad(), productoParaVender.getPrecio(),
					productoParaVender.getNombre(), productoParaVender.getCodigo(), v);
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
		return productos;
	}

	@RequestMapping(value = "/compras/{username}", method = RequestMethod.GET)
	public List<Venta> compras(@PathVariable(name = "username") String username, Model model) {
		Usuario user = userService.findByUser(username);
		List<Venta> ventas = productosVendidosRepository.findAllValidos(user.getId());

		return ventas;
	}
	
	@RequestMapping(value = "/registrar/{username}/{pass}/{email}", method = RequestMethod.GET)
	public Usuario registrar(@PathVariable(name = "username") String username,@PathVariable(name = "pass") String password, @PathVariable(name = "email") String email) {
		Usuario existe = userService.findByEmail(email);
		Usuario existe2 = userService.findByUser(username);
		
		if(existe != null || existe2 != null) {
			existe = new Usuario();
			existe.setUsername("Existe");
			return existe;
		}
		
		String pass = "";
		for (int i = 0; i < 2; i++) {
			pass = passwordEncoder.encode(password);
		}
		
		
		Usuario user = new Usuario();
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(pass);
		user.setEnabled(true);
		userService.save(user);
		Usuario good = userService.findByEmail(email);
		return good;
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
