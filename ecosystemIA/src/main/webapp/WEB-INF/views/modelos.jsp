<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/WEB-INF/views/cabecera.jsp" %>

<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Listado de modelos de redes neuronales disponibles</title>

  <body>
    <div class="container">
        <div class="table-wrapper">
            <div class="table-title">
                <div class="row">
                    <div class="col-sm-6">
						<h2>Modelos disponibles.</h2>
					</div>
					<div class="col-sm-6">
						<a href="nuevoModelo.do" class="btn btn-success"><i class="material-icons">&#xE147;</i> <span>Nuevo modelo</span></a>
					</div>
                </div>
            </div>
            <table class="table table-striped table-hover">
                <thead>
                    <tr>
                    	<th>Id</th>
                        <th>Nombre</th>
                        <th>Descripción</th>
						<th>Img Width</th>
                        <th>Img Height</th>
                        <th>Channels</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <!--  Nombre de modelo mvc en controller para esta página, nombre tabla en POJO -->
	  					<tr><td colspan="7"></td></tr>
	    				<c:forEach var="m" items="${modeloMVC.listadoModelos}" varStatus="loop">
		      				<tr>
		      					<td>${m.idModelo}</td>
			      				<td>${m.nombreModelo}</td>
			      				<td>${m.descripcion}</td>
			      				<td>${m.modelImageWidth}</td>
			      				<td>${m.modelImageHeight}</td>
			      				<td>${m.imageChannels}</td>
			      				<td>
		                            <a href="<c:url value='editarModelo.do?idModelo=${modeloMVC.listadoModelos[loop.index].idModelo}' />" class="editar" data-toggle="modal"><i class="material-icons" data-toggle="tooltip" title="Editar">&#xE254;</i></a>
		                            <a href="<c:url value='eliminarModelo.do?idModelo=${modeloMVC.listadoModelos[loop.index].idModelo}' />" class="borrar" data-toggle="modal"><i class="material-icons" data-toggle="tooltip" title="Borrar">&#xE872;</i></a>
		                        	<a href="<c:url value='verModelo.do?idModelo=${modeloMVC.listadoModelos[loop.index].idModelo}' />" class="ver" data-toggle="modal"><i class="material-icons" data-toggle="tooltip" title="Ver">remove_red_eye</i></a>
		                        	<a href="<c:url value='verCategorias.do?idModelo=${modeloMVC.listadoModelos[loop.index].idModelo}' />" class="categorias" data-toggle="modal"><i class="material-icons" data-toggle="tooltip" title="Categorías">dataset</i></a>
		                        	<a href="<c:url value='cargarModelo.do?idModelo=${modeloMVC.listadoModelos[loop.index].idModelo}' />" class="predecir" data-toggle="modal"><i class="material-icons" data-toggle="tooltip" title="Cargar" onclick="mostrar('progress');">burst_mode</i></a>
		                        </td>
		      				</tr>
	    				</c:forEach>
                </tbody>
            </table>
			<div class="progress" id="progress" style="visibility:hidden">
    			<div class="progress-bar progress-bar-striped active" role="progressbar"
  							aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width:80%">
    						cargando modelo neuronal...
  				</div>
			</div>
        </div>
    </div>
</body>
</html>