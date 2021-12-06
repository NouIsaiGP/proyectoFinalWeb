/**
 * 
 */

var formulario = document.getElementById('formulario'),
	contacto = formulario.contacto,
	telefono = formulario.telefono,
	costo = formulario.costo,
	descripcion = formulario.descripcion,
	error = document.getElementById('error');

function validarContacto(e) {
	if (contacto.value == '' || contacto.value == null) {
		error.style.display = 'block';
		error.innerHTML += '<li>Por favor ingrese el contacto</li>';
		e.preventDefault();
	} else {
		error.style.display = 'none';
	}
}

function validarTelefono(e) {
	var tel = parseInt(telefono.value)
	if (telefono.value == '' || telefono.value == null) {
		error.style.display = 'block';
		error.innerHTML += '<li>Por favor ingrese el telefono</li>';
		e.preventDefault();
	} else if (!Number.isInteger(tel) || telefono.value.length < 9) {
		error.style.display = 'block';
		error.innerHTML += '<li>Por favor ingrese un telefono valido</li>';
		e.preventDefault();
	} else {
		error.style.display = 'none';
	}
}

function validarCosto(e) {
	if (costo.value <= 0 || costo.value == null) {
		error.style.display = 'block';
		error.innerHTML += '<li>Por favor ingrese un costo valido</li>';
		e.preventDefault();
	} else {
		error.style.display = 'none';
	}
}

function validarDescripcion(e) {
	if (descripcion.value == '' || descripcion.value == null) {
		error.style.display = 'block';
		error.innerHTML += '<li>Por favor ingrese la descripcion</li>';
		e.preventDefault();
	} else {
		error.style.display = 'none';
	}
}



formulario.addEventListener('submit', validarFormulario);

function validarFormulario(e) {
	validarContacto(e);
	validarCosto(e);
	validarDescripcion(e);
	validarTelefono(e);

}