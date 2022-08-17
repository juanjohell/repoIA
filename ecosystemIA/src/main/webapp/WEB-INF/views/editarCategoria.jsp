<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/WEB-INF/views/cabecera.jsp" %>

<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Editar categoria</title>
  	<body>
  		<div class="container">
 			<div class="row">
 				<div class ="col">
 				</div>
 				<div class ="col-6">
  					<h1 class="display-3">Editar ${modelo.nombreModelo}</h1>
					<form method="POST" action="${pageContext.request.contextPath}/editarCategoria.do">
						<div class="form-row">
  							<div class="form-group">
  								<div class="form-group col-md-2">
   				 					<label for="modelo.idModelo">Id de modelo.</label>
      								<s:input path="modelo.idModelo" class="form-control" name="idModelo" id="idModelo" readonly="true"/>
    							</div>
   				 				<div class="form-group col-md-10">
   				 					<label for="modelo.nombreModelo">Nombre de modelo.</label>
      								<s:input path="modelo.nombreModelo" class="form-control" name="nombreModelo" id="nombreModelo" readonly="true"/>
    							</div>
  							</div>
  								<div class="form-group col-md-2">
   				 					<label for="categoria.idOrden">Orden de la categoria.</label>
      								<s:input path="categoria.idOrden" class="form-control" name="idOrden" id="idOrden"/>
    							</div>
   				 				<div class="form-group col-md-10">
   				 					<label for="categoria.nombreCategoria">Clase o categoría.</label>
      								<s:input path="categoria.nombreCategoria" class="form-control" name="nombreCategoria" id="nombreCategoria" required="true"/>
    							</div>
  						</div>
  						<button type="button" id="grabar" name="grabar" class="btn btn-primary" data-toggle="modal" data-target="#confirmacionModal">
  								Grabar cambios
						</button>
						<button value="Cancelar" type="submit" name="cancelar"  id="cancelar" class="btn btn-primary">Volver</button>
						<!-- Modal -->
							<div class="modal fade" id="confirmacionModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
							  <div class="modal-dialog" role="document">
							    <div class="modal-content">
							      <div class="modal-header">
							        <h5 class="modal-title" id="exampleModalLabel">Se van a guardar los cambios introducidos. ¿Proceder?</h5>
							        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
							          <span aria-hidden="true">&times;</span>
							        </button>
							      </div>
							      <div class="modal-body">
							      </div>
							      <div class="modal-footer">
							        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
							        <button value="Grabar" type="submit" name="grabar" id="grabar" class="btn btn-primary">Grabar cambios</button>
							      </div>
							    </div>
							  </div>
							</div>
						<!-- fin ventana modal -->
					</form>
				</div>
				<div class="col">
				</div>
			</div>
		</div>
	</body>
</html>