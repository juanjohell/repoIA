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
  	${modelo.nombreModelo}
  		<div class="container-fluid">
		<form:form method="POST" action="${pageContext.request.contextPath}/editarModelo.do">
			<div class="form-row">
  				<div class="form-group">
  				
   				 	<div class="form-group col-md-10">
   				 		<label for="modelo.nombreModelo">Nombre de modelo.</label>
      					<s:input path="modelo.nombreModelo" class="form-control" id="nombreModelo"/>
    				</div>
  				</div>
  				<div class="form-group">
  					<label for="descripcion">Descripcion.</label>
      				<s:textarea path="descripcion" class="form-control" placeholder="Descripción del modelo" id="descripcion"/>
      				
  				</div>
  				<div class="form-group">
  					<div class="form-group col-md-4">
    					<label for="modelImageHeight">Alto de imagen.</label>
      					<s:input path="modelImageHeight" class="form-control" placeholder="Alto de imagen" id="modelImageHeight"/>
    				</div>
    				<div class="form-group col-md-4">
    					<label for="modeloMVC.modelImageWidth">Ancho de imagen.</label>
      					<s:input path="modelImageWidth" class="form-control" placeholder="Alto de imagen" id="modelImageWidth"/>
    				</div>
    				<div class="form-group col-md-4">
    					<label for="imageChannels">Canales de imagen</label>
      					<s:input path="imageChannels" class="form-control" placeholder="Alto de imagen" id="imageChannels"/>
    				</div>
  				</div>
  			</div>
  			<form:button value="Grabar" type="submit" name="grabar" id="grabar" class="btn btn-primary"/>
			<form:button value="Cancelar" type="submit" name="cancelar"  id="cancelar" class="btn btn-primary"/>
  			
		</form:form>
		</div>
	</body>
</html>