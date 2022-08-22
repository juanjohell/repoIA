<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/WEB-INF/views/cabecera.jsp" %>

<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Listado de categorias del modelo</title>

  <body>
    <div class="container">
        <div class="table-wrapper">
            <div class="table-title">
                <div class="row">
                    <div class="col-sm-6">
						<h2>Categorías del modelo ${modeloMVC.modelo.nombreModelo}</h2>
					</div>
					<div class="col-sm-6">
						<a href="#addEmployeeModal" class="btn btn-success" data-toggle="modal"><i class="material-icons">&#xE147;</i> <span>Añadir categoria</span></a>
					</div>
                </div>
            </div>
            <table class="table table-striped table-hover">
                <thead>
                    <tr>
                    	<th>Id</th>
                        <th>Orden</th>
                        <th>Nombre</th>
                    </tr>
                </thead>
                <tbody>
                    <!--  Nombre de modelo mvc en controller para esta página, nombre tabla en POJO -->
	  					<tr><td colspan="4"></td></tr>
	    				<c:forEach var="m" items="${modeloMVC.listadoCategorias}" varStatus="loop">
		      				<tr>
		      					<td>${m.idCategoria}</td>
			      				<td>${m.idOrden}</td>
			      				<td>${m.nombreCategoria}</td>
			      				<td>
		                            <a href="<c:url value='editarCategoria.do?idModelo=${modeloMVC.listadoCategorias[loop.index].idModelo}&idOrden=${modeloMVC.listadoCategorias[loop.index].idOrden}' />" class="editar" data-toggle="modal"><i class="material-icons" data-toggle="tooltip" title="Editar">&#xE254;</i></a>
		                            <a href="<c:url value='editarCategoria.do?idModelo=${modeloMVC.listadoCategorias[loop.index].idModelo}&idOrden=${modeloMVC.listadoCategorias[loop.index].idOrden}'  />" class="borrar" data-toggle="modal"><i class="material-icons" data-toggle="tooltip" title="Borrar">&#xE872;</i></a>
		                        </td>
		      				</tr>
	    				</c:forEach>
	    				<tr><td colspan="4">
	    				<form method="POST" action="${pageContext.request.contextPath}/verCategorias.do">
	    					<button value="Cancelar" type="submit" name="cancelar"  id="cancelar" class="btn btn-primary">Volver</button>
	    				</form>
	    				</td></tr>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>