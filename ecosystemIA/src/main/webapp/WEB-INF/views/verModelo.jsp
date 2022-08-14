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
						<form method="POST" action="${pageContext.request.contextPath}/editarModelo.do">
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
				  			</div>
							<button value="cancelar" type="submit" name="cancelar"  id="cancelar" class="btn btn-primary">Volver</button>
						</form>
					</div>
				<div class="col">
				</div>
			</div>
		</div>
	</body>
</html>