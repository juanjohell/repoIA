<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/WEB-INF/views/cabecera.jsp" %>

<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Nuevo modelo</title>
  	<body>
  		<div class="container-fluid">
		<form method="POST" action="${pageContext.request.contextPath}/nuevoModelo.do">
			<div class="form-row">
  				<div class="form-group">
   				 	<div class="form-group col-md-12">
   				 		<label for="modelo.nombreModelo">Nombre de modelo.</label>
      					<s:input path="modelo.nombreModelo" class="form-control" name="nombreModelo" id="nombreModelo"/>
    				</div>
  				</div>
  				<div class="form-group">
  					<label for="modelo.descripcion">Descripcion.</label>
      				<s:textarea path="modelo.descripcion" class="form-control" placeholder="Descripción del modelo" name="descripcion" id="descripcion"/>
  				</div>
  				<div class="form-group">
  					<div class="form-group col-md-2">
    					<label for="modelo.modelImageHeight">Alto de imagen.</label>
      					<s:input path="modelo.modelImageHeight" class="form-control" placeholder="Alto de imagen" name="modelImageHeight" id="modelImageHeight"/>
    				</div>
    				<div class="form-group col-md-2">
    					<label for="modelo.modelImageWidth">Ancho de imagen.</label>
      					<s:input path="modelo.modelImageWidth" class="form-control" placeholder="Alto de imagen" name="modelImageWidth" id="modelImageWidth"/>
    				</div>
    				<div class="form-group col-md-1">
    					<label for="modelo.imageChannels">Canales de imagen</label>
      					<s:input path="modelo.imageChannels" class="form-control" placeholder="Alto de imagen" name="imageChannels" id="imageChannels"/>
    				</div>
    				<div class="form-group col-md-7">
    					<label for="modelo.imageChannels">Ruta al recurso del fichero h5. (Keras model)</label>
      					<s:input path="modelo.pathToModel" class="form-control" placeholder="ruta a fichero h5" name="pathToModel" id="pathToModel"/>
    				</div>
  				</div>
  			</div>
  			<button value="Grabar" type="submit" name="grabar" id="grabar" class="btn btn-primary">Grabar</button>
			<button value="Cancelar" type="submit" name="cancelar"  id="cancelar" class="btn btn-primary">Cancelar</button>
		</form>
		</div>
	</body>
</html>