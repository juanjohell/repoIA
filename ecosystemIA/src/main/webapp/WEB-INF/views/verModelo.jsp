<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/WEB-INF/views/cabecera.jsp" %>

<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Editar modelo</title>
  	<body>
  		<div class="container">
  			<div class="row">
 				<div class ="col">
 				</div>
 				<div class ="col-6">
  					<h1 class="display-3">Datos del modelo ${modelo.nombreModelo}</h1>
						<form method="POST" action="${pageContext.request.contextPath}/verModelo.do">
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
				  				<div class="form-group">
				  					<label for="modelo.descripcion">Descripcion.</label>
				      				<s:textarea path="modelo.descripcion" class="form-control" placeholder="Descripción del modelo" name="descripcion" id="descripcion" readonly="true"/>
				  				</div>
				  				<div class="form-group">
				  					<div class="form-group col-md-4">
				    					<label for="modelo.modelImageHeight">Alto de imagen.</label>
				      					<s:input path="modelo.modelImageHeight" class="form-control" placeholder="Alto de imagen" name="modelImageHeight" id="modelImageHeight" readonly="true"/>
				    				</div>
				    				<div class="form-group col-md-4">
				    					<label for="modelo.modelImageWidth">Ancho de imagen.</label>
				      					<s:input path="modelo.modelImageWidth" class="form-control" placeholder="Alto de imagen" name="modelImageWidth" id="modelImageWidth" readonly="true"/>
				    				</div>
				    				<div class="form-group col-md-4">
				    					<label for="modelo.imageChannels">Canales de imagen</label>
				      					<s:input path="modelo.imageChannels" class="form-control" placeholder="Alto de imagen" name="imageChannels" id="imageChannels" readonly="true"/>
				    				</div>
				  				</div>
				  				<div class="form-group">
				  					<div class="form-group col-md-4">
				  						<label for="tipoAlmacenamiento.idTipoAlmacenamiento">Fichero almacenado en</label>
				      					<s:input path="tipoAlmacenamiento.nombre" class="form-control" placeholder="Tipo de almacenamiento" name="tipoAlmacenamiento" id="tipoAlmacenamiento" readonly="true"/>
				  					</div>
				  					<div class="form-group col-md-1">
				  						<label for="tipoFichero.idTipoFichero">Tipo de fichero</label>
				      					<s:input path="tipoFichero.nombreCorto" class="form-control" placeholder="Tipo de fichero" name="tipoFichero" id="tipoFichero" readonly="true"/>
				  					</div>
				  					<div class="form-group col-md-3">
				  						<label for="tipoPrediccion.idTipoPrediccion">Tipo de predicción</label>
				      					<s:input path="tipoPrediccion.nombre" class="form-control" placeholder="Tipo de prediccion" name="tipoPrediccion" id="tipoPrediccion" readonly="true"/>
				  					</div>
				  					<div class="form-group col-md-4">
				  						<label for="tipoSalida.idTipoSalida">Tipo de salida del modelo</label>
				      					<s:input path="tipoSalida.nombre" class="form-control" placeholder="Tipo de salida" name="tipoSalida" id="tipoSalida" readonly="true"/>
				  					</div>
				  				</div>
				  				<div class="form-group">
				  					<div class="form-group col-md-12">
				  						<label for="modelo.pathToModel">Ruta al fichero</label>
				      					<s:input path="modelo.pathToModel" class="form-control" placeholder="Ruta al fichero" name="pathToModel" id="pathToModel" readonly="true"/>
				  					</div>
				  				</div>
				  			</div>
				  			<a href="<c:url value='verEstructura.do?idModelo=${modelo.idModelo}' />" class="ver" data-toggle="modal"><i class="material-icons" data-toggle="tooltip" title="Ver">remove_red_eye</i></a>
							<button value="cancelar" type="submit" name="cancelar"  id="cancelar" class="btn btn-primary">Volver</button>
						</form>
					</div>
				<div class="col">
				</div>
			</div>
		</div>
	</body>
</html>