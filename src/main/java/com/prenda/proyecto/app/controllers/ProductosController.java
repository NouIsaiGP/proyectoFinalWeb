package com.prenda.proyecto.app.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prenda.proyecto.app.models.dao.ILoteDao;
import com.prenda.proyecto.app.models.dao.ProductosRepository;
import com.prenda.proyecto.app.models.entity.Avio;
import com.prenda.proyecto.app.models.entity.Lote;
import com.prenda.proyecto.app.models.entity.Producto;
import com.prenda.proyecto.app.models.service.ILoteService;
import com.prenda.proyecto.app.util.paginator.PageRender;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/productos")
public class ProductosController {

	protected final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private ProductosRepository productosRepository;
	
	@Autowired
	private ILoteService loteService;

	@GetMapping(value = "/mostrar/up/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		Path pathFoto = Paths.get("uploads").resolve(filename).toAbsolutePath();
		log.info("pathFoto: " + pathFoto);
		Resource recurso = null;

		try {
			recurso = new UrlResource(pathFoto.toUri());
			if (!recurso.exists() || !recurso.isReadable()) {
				throw new RuntimeException("Error: no se pudo cargar la imagen " + pathFoto.toString());
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("aah" + ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso).toString());

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	@GetMapping(value = "/agregar")
	@Secured("ROLE_ADMIN")
	public String agregarProducto(Model model) {
		model.addAttribute("producto", new Producto());
		return "productos/agregar_producto";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/mostrar")
	public String mostrarProductos(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Producto> productos = null;
		productos = productosRepository.findAllValidos(pageRequest);
		PageRender<Producto> pageRender = new PageRender<Producto>("/productos/mostrar", productos);

		model.addAttribute("productos", productos);
		model.addAttribute("page", pageRender);
		return "productos/ver_productos";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/eliminar")
	public String eliminarProducto(@ModelAttribute Producto producto, RedirectAttributes redirectAttrs) {
		redirectAttrs.addFlashAttribute("mensaje", "Eliminado correctamente").addFlashAttribute("clase", "warning");
		productosRepository.deleteById(producto.getId());
		return "redirect:/productos/mostrar";
	}


	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/editar/{id}")
	public String actualizarProducto(@RequestParam String oldName, @ModelAttribute @Valid Producto producto, BindingResult bindingResult,
			@RequestParam("file") MultipartFile foto, RedirectAttributes redirectAttrs) {
		
		if (bindingResult.hasErrors()) {
			if (producto.getId() != null) {
				return "productos/editar_producto";
			}
			return "redirect:/productos/mostrar";
		}

		if (!foto.isEmpty()) {

			String uniqueFile = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();

			Path rootPath = Paths.get("uploads").resolve(uniqueFile);

			Path rootAbsoluthPath = rootPath.toAbsolutePath();

			try {
				Files.copy(foto.getInputStream(), rootAbsoluthPath);

				redirectAttrs.addFlashAttribute("info", "Has subido correctamente " + uniqueFile);

				producto.setEnable("0");
				producto.setFoto(uniqueFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		producto.setEnable("0");
		
		Lote lote = null;
		if( (lote = loteService.findByName(oldName)) != null ) {
			Float total = lote.getCostoInicial();
			lote.setCantidad(Math.round(producto.getExistencia()));
			lote.setNombre(producto.getNombre());
			lote.setCostoVenta(producto.getPrecio());
			if(lote.getTipo().equals("original")) {
				lote.setTipo("original");
			}else {
				lote.setTipo("compra");
			}
			lote.setCostoInicial(total);
		}
		loteService.save(lote);

		productosRepository.save(producto);
		redirectAttrs.addFlashAttribute("mensaje", "Editado correctamente").addFlashAttribute("clase", "success");
		return "redirect:/productos/mostrar";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model model) {
		model.addAttribute("producto", productosRepository.findById(id).orElse(null));
		
		return "productos/editar_producto";
	}

	@PostMapping(value = "/agregar")
	@Secured("ROLE_ADMIN")
	public String guardarProducto(@RequestParam Integer total, @ModelAttribute @Valid Producto producto, BindingResult bindingResult,
			@RequestParam("file") MultipartFile foto, RedirectAttributes redirectAttrs) {
		if (bindingResult.hasErrors()) {
			return "productos/agregar_producto";
		}
		if (productosRepository.findFirstByCodigo(producto.getCodigo()) != null) {
			redirectAttrs.addFlashAttribute("mensaje", "Ya existe un producto con ese código")
					.addFlashAttribute("clase", "warning");
			return "redirect:/productos/agregar";
		}

		if (!foto.isEmpty()) {

			String uniqueFile = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();

			Path rootPath = Paths.get("uploads").resolve(uniqueFile);

			Path rootAbsoluthPath = rootPath.toAbsolutePath();

			try {
				Files.copy(foto.getInputStream(), rootAbsoluthPath);

				redirectAttrs.addFlashAttribute("info", "Has subido correctamente " + uniqueFile);
				
				producto.setEnable("0");
				producto.setFoto(uniqueFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		total = (int) (producto.getPrecio() * producto.getExistencia());
		
		Lote lote = null;
		if( (lote = loteService.findByName(producto.getNombre())) != null ) {
			lote.setCantidad(Math.round(producto.getExistencia()));
			lote.setNombre(producto.getNombre());
			lote.setCostoVenta(producto.getPrecio());
			if(lote.getTipo().equals("original")) {
				lote.setTipo("original");
			}else {
				lote.setTipo("compra");
			}
			lote.setCostoInicial((float)total);
		}else{
			lote = new Lote();
			lote.setCantidad(Math.round(producto.getExistencia()));
			lote.setNombre(producto.getNombre());
			lote.setCostoVenta(producto.getPrecio());
			lote.setTipo("compra");
			lote.setCostoInicial((float)total);
		}
		
		loteService.save(lote);

		productosRepository.save(producto);
		redirectAttrs.addFlashAttribute("mensaje", "Agregado correctamente").addFlashAttribute("clase", "success");
		return "redirect:/productos/agregar";
	}
	
	@PostMapping(value = "/original")
	@Secured("ROLE_ADMIN")
	public String guardarProductoOriginal(@RequestParam Float total,@RequestParam Float existencias, @ModelAttribute Producto producto, BindingResult bindingResult,
			@RequestParam("file") MultipartFile foto, RedirectAttributes redirectAttrs) {
		
		producto.setExistencia(existencias);
		
		if (productosRepository.findFirstByCodigo(producto.getCodigo()) != null) {
			redirectAttrs.addFlashAttribute("mensaje", "Ya existe un producto con ese código")
					.addFlashAttribute("clase", "warning");
			return "redirect:/productos/agregar";
		}

		if (!foto.isEmpty()) {

			String uniqueFile = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();

			Path rootPath = Paths.get("uploads").resolve(uniqueFile);

			Path rootAbsoluthPath = rootPath.toAbsolutePath();

			try {
				Files.copy(foto.getInputStream(), rootAbsoluthPath);

				redirectAttrs.addFlashAttribute("info", "Has subido correctamente " + uniqueFile);
				
				producto.setEnable("0");
				producto.setFoto(uniqueFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Lote lote = null;
		if( (lote = loteService.findByName(producto.getNombre())) != null ) {
			lote.setCantidad(Math.round(producto.getExistencia()));
			lote.setNombre(producto.getNombre());
			lote.setCostoVenta(producto.getPrecio());
			lote.setTipo("original");
			lote.setCostoInicial((float)total);
		}else{
			lote = new Lote();
			lote.setCantidad(Math.round(producto.getExistencia()));
			lote.setNombre(producto.getNombre());
			lote.setCostoVenta(producto.getPrecio());
			lote.setTipo("original");
			lote.setCostoInicial((float)total);
		}
		
		loteService.save(lote);

		productosRepository.save(producto);
		redirectAttrs.addFlashAttribute("mensaje", "Agregado correctamente").addFlashAttribute("clase", "success");
		return "redirect:/inventario";
	}
}