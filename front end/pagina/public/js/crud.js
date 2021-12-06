/**
 * @author amaury morales
 */

$().ready(function () {

    /**
     * Se encarga de generar un arreglo de los valores
     * introducidos en el formulario para que luego este
     * arreglo se transforme en un json con la funcion
     * JSON.stringify()
     * @param {*} form - formulario a transformar en objeto json
     */
    JQ_formularioJSON = function (form) {
        var arregloDesordenado = $(form).serializeArray();
        var arregloPares = {};

        $.map(arregloDesordenado, function (n, i) {
            arregloPares[n['name']] = n['value'];
        });

        var json = arregloPares;
        console.log("Objeto del forms");
        console.log(json);
        return json;
    }

    /**
     * Llama a la funcion que hace la peticion GET al REST API
     */
    JQ_GET = function () {
        console.log("GET");
        getEmpleados();
    }

    /**
     * Llama a la funcion que hace la peticion POST al REST API
     * @param {*} form - formulario empleado
     */
    JQ_POST = function (form) {
        console.log("POST");
        var json = JQ_formularioJSON(form);

        postEmpleado(json);

        return false;
    }

    /**
     * Llama a la funcion que hace la peticion PUT al REST AP
     * @param {*} form - formulario empleado
     */
    JQ_PUT = function (form) {
        console.log("PUT");
        var json = JQ_formularioJSON(form);
        var empleadoId = document.getElementById("inputId").value;

        putEmpleado(json, empleadoId);

        return false;
    }

    /**
     * Llama a la funcion que hace la peticion DELETE al REST API
     * @param {*} id - id del empleado a eliminar
     */
    JQ_DELETE = function (id) {
        console.log("DELETE");
        console.log(id);
        deleteEmpleado(id);
    }

    getEmpleados();
})

/**
 *  Arreglo en el cual se encuentran todos los numeros de id
 *  correspondientes a los registros seleccionados en la interfaz
 *  grafica de la tabla
 */
seleccionados = [];

/**
 * Cierra la ventana flotante del formulario
 */
function cerrar_ventanaFlotante() {
    var ventana = document.getElementById("ventanaAgregarRegistro");
    ventana.style.display = "none";
}

/**
 * Muestra el formulario relacionado con modificar un nuevo
 * registro y llena los campos del registro seleccionado a modificar
 * para facilitar el uso
 */
function mostrar_nuevoRegistro() {
    formulario = document.getElementById("formulario");
    formulario.setAttribute("onsubmit", "return JQ_POST(this)");

    inputId = document.getElementById("inputId");
    inputNombre = document.getElementById("inputNombre");
    inputCorreo = document.getElementById("inputCorreo");
    inputDireccion = document.getElementById("inputDireccion");
    inputTelefono = document.getElementById("inputTelefono");

    inputId.value = "";
    inputNombre.value = "";
    inputCorreo.value = "";
    inputDireccion.value = "";
    inputTelefono.value = "";

    var ventana = document.getElementById("ventanaAgregarRegistro");
    var titulo = document.getElementById("formularioTitulo");
    ventana.style.display = "block";
    titulo.innerHTML = "Agregar Nuevo Registro";
}

/**
 * Muestra el formulario relacionado con modificar un registro existente
 * y llena los campos del registro seleccionado a modificar
 * para facilitar el uso
 * @param {*} elemento - boton correspondiente a modificar registro
 */
function mostrar_modificarRegistro(elemento) {
    formulario = document.getElementById("formulario");
    formulario.setAttribute("onsubmit", "return JQ_PUT(this)");

    var indice = elemento.parentNode.parentNode.rowIndex;
    var fila = document.getElementById("tablaEmpleados").rows[indice].cells;

    id = fila[1].innerHTML;
    nombre = fila[2].innerHTML;
    correo = fila[3].innerHTML;
    direccion = fila[4].innerHTML;
    telefono = fila[5].innerHTML;

    inputId = document.getElementById("inputId");
    inputNombre = document.getElementById("inputNombre");
    inputCorreo = document.getElementById("inputCorreo");
    inputDireccion = document.getElementById("inputDireccion");
    inputTelefono = document.getElementById("inputTelefono");

    inputId.value = id;
    inputNombre.value = nombre;
    inputCorreo.value = correo;
    inputDireccion.value = direccion;
    inputTelefono.value = telefono;

    var ventana = document.getElementById("ventanaAgregarRegistro");
    var titulo = document.getElementById("formularioTitulo");
    ventana.style.display = "block";
    titulo.innerHTML = "Modificar Registro";
}

/**
 * Agrega un registro a la tabla a partir de un objeto JSON
 * con las llaves y valores correspondientes
 * @param {*} datos - objeto JSON procesado por tabla_rellenar
 */
function tabla_agregarRegistro(datos) {
    var tabla = document.getElementById("tablaEmpleados").getElementsByTagName("tbody")[0];
    var nuevoRegistro = tabla.insertRow(tabla.length);

    botonSeleccionar = '<input type="checkbox" id="seleccionarRegistro" onclick="tabla_seleccionarElemento()" </input>';
    botonModificar = '<button type="button" class="btn btn-warning" onclick="mostrar_modificarRegistro(this)"><i class="icon-edit"></i></button>';
    botonEliminar = '<button type="button" class="btn btn-danger" onclick="tabla_eliminarElemento(this)"><i class="icon-trash"></i></button>';

    seleccion = nuevoRegistro.insertCell(0);
    seleccion.innerHTML = botonSeleccionar;
    id = nuevoRegistro.insertCell(1);
    id.innerHTML = datos.id;
    nombre = nuevoRegistro.insertCell(2);
    nombre.innerHTML = datos.name;
    correo = nuevoRegistro.insertCell(3);
    correo.innerHTML = datos.email;
    direccion = nuevoRegistro.insertCell(4);
    direccion.innerHTML = datos.address;
    telefono = nuevoRegistro.insertCell(5);
    telefono.innerHTML = datos.phone;
    acciones = nuevoRegistro.insertCell(6);
    acciones.innerHTML = botonModificar + botonEliminar;
}

/**
 * Rellena la tabla utilizando el JSON recibido por la peticion
 * GET del REST API, agarrando las llaves y los valores del JSON
 * e introduciendolos uno por uno a la funcion tabla_agregarRegistro
 * @param {*} getJSON - JSON recibido por la peticion GET
 */
function tabla_rellenar(getJSON) {
    for (i = 0; i < Object.keys(getJSON).length; i++) {
        tabla_agregarRegistro(getJSON[i]);
    }
}

/**
 * Selecciona todos los checkbox y agrega sus id de registros
 * correspondientes al arreglo "seleccionados"
 * @param {*} orig  - el checkbox que se encuentra en la cabecera de la tabla
 */
function tabla_seleccionarTodos(orig) {
    seleccionados = [];
    checkboxes = document.querySelectorAll("input[type=checkbox]");
    numeroRegistros = checkboxes.length;
    for (i = 1; i < numeroRegistros; i++) {
        checkboxes[i].checked = orig.checked;
        if (checkboxes[i].checked) {
            var fila = document.getElementById("tablaEmpleados").rows[i].cells;
            id = fila[1].innerHTML;
            seleccionados.push(id);
        }
    }

    console.log(seleccionados);
}

/**
 * Selecciona el checkbox y agrega el id del registro correspondiente
 * al arreglo "seleccionados"
 */
function tabla_seleccionarElemento() {
    seleccionados = [];
    checkboxes = document.querySelectorAll("input[type=checkbox]");
    numeroRegistros = checkboxes.length;

    for (i = 1; i < numeroRegistros; i++) {
        if (checkboxes[i].checked) {
            var fila = document.getElementById("tablaEmpleados").rows[i].cells;
            id = fila[1].innerHTML;
            seleccionados.push(id);
        }
    }

    console.log(seleccionados);
}

/**
 * Llama a la funcion JQ_DELETE suministrando el id del registro obtenido
 * @param {*} elemento - el boton de eliminar registro correspondiente
 */
function tabla_eliminarElemento(elemento) {
    var indice = elemento.parentNode.parentNode.rowIndex;
    var fila = document.getElementById("tablaEmpleados").rows[indice].cells;
    id = fila[1].innerHTML;
    JQ_DELETE(id);
}

/**
 * Hace un fetch request al URL correspondiente al GET del REST API
 */
function tabla_eliminarSeleccionados() {
    for (i = 0; i < seleccionados.length; i++) {
        JQ_DELETE(seleccionados[i]);
    }
}

//Peticiones asincronas

/**
 * Hace un fetch request al URL correspondiente al GET del REST API
 */
async function getEmpleados() {
    try {
        let res = await fetch("http://localhost:8080/employees"),
            json = await res.json();
        if (!res.ok) throw { status: res.status, statusText: res.statusText };
        console.log(json);
        tabla_rellenar(json);
    } catch (err) {
        let message = err.statusText || "Ocurrió un error";
    }
}

/**
 * Hace un fetch request al URL correspondiente al POST del REST API
 * y al recibir una respuesta, recarga la pagina
 * @param {*} jsonEmpleado - el JSON Object correspondiente al nuevo registro
 */
async function postEmpleado(jsonEmpleado) {

    console.log("Objeto en el post");
    console.log(jsonEmpleado);
    console.log(JSON.stringify(jsonEmpleado));

    const response = await fetch('http://localhost:8080/employees', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonEmpleado)
    });
    const content = await response.json();
    console.log(content);
    location.reload();
}

/**
 * Hace un fetch request al URL correspondiente al PUT del REST API
 * y al recibir una respuesta, recarga la pagina
 * @param {*} jsonEmpleado - el JSON Object correspondiente al registro modificado
 * @param {*} empleadoId  - el numero de id correspondiente al registro modificar
 */
async function putEmpleado(jsonEmpleado, empleadoId) {
    console.log("Objeto en el put");
    console.log(jsonEmpleado);
    console.log(JSON.stringify(jsonEmpleado));

    const response = await fetch('http://localhost:8080/employees/' + empleadoId, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonEmpleado)
    });
    const content = await response.json();
    console.log(content);
    location.reload();
}

/**
 * Hace un fetch request al URL correspondiente al DELETE del REST API
 * y al recibir una respuesta, recarga la pagina
 * @param {*} empleadoId - el numero de id correspondiente al registro a eliminar
 */
async function deleteEmpleado(empleadoId) {
    const response = await fetch('http://localhost:8080/employees/' + empleadoId, {
        method: 'DELETE',
    });
    const content = await response.json();
    console.log(content);
    location.reload();
}