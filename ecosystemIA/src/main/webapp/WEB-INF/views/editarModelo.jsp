<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/WEB-INF/views/cabecera.jsp" %>

<html lang="es">
<script type="text/javascript">
function mostrar_control() {
	var index = document.getElementById('tipoAlmacenamiento').selectedIndex;
	//BASE DE DATOS
	if (index == '0') 
		{
		document.getElementById('uploadFichero').style.visibility = 'visible';
		document.getElementById('rutaFichero').style.visibility = 'hidden';
		}
	// URL O RECURSO SISTEMA FICHEROS SERVIDOR
	else{
		document.getElementById('uploadFichero').style.visibility = 'hidden';
		document.getElementById('rutaFichero').style.visibility = 'visible';
		}
}

</script>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Editar modelo</title>
  	<body>
  		<div class="container">
 			<div class="row">
 				<div class ="col-2">
 				</div>
 				<div class ="col-10">
 				
  					<h1 class="display-3">Editar ${modelo.nombreModelo}</h1>
					<form method="POST" action="${pageContext.request.contextPath}/editarModelo.do">
						<div class="form-row">
  							<div class="form-group">
  								<div class="form-group col-md-2">
   				 					<label for="modelo.idModelo">Id de modelo.</label>
      								<s:input path="modelo.idModelo" class="form-control" name="idModelo" id="idModelo" readonly="true"/>
    							</div>
   				 				<div class="form-group col-md-10">
   				 					<label for="modelo.nombreModelo">Nombre de modelo.</label>
      								<s:input path="modelo.nombreModelo" class="form-control" name="nombreModelo" id="nombreModelo" required="true"/>
      								<div class="invalid-tooltip">
      										Debe indicar un nombre para el modelo.
    								</div>
    							</div>
  							</div>
  							<div class="form-group">
  								<div class="form-group col-md-12">
  									<label for="modelo.descripcion">Descripcion.</label>
      								<s:textarea path="modelo.descripcion" class="form-control" placeholder="Descripción del modelo" name="descripcion" id="descripcion"/>
  								</div>
  							</div>
  							<div class="form-group">
  								<div class="form-group col-md-2">
    								<label for="modelo.modelImageHeight">Alto de imagen.</label>
      								<s:input path="modelo.modelImageHeight" class="form-control" placeholder="Alto de imagen" name="modelImageHeight" id="modelImageHeight" required="true"/>
    								<div class="invalid-tooltip">
      										Debe indicar alto de imagen.
    								</div>
    							</div>
    							<div class="form-group col-md-2">
    								<label for="modelo.modelImageWidth">Ancho de imagen.</label>
      								<s:input path="modelo.modelImageWidth" class="form-control" placeholder="Alto de imagen" name="modelImageWidth" id="modelImageWidth" required="true"/>
    								<div class="invalid-tooltip">
      										Debe indicar un ancho de imagen.
    								</div>
    							</div>
    							<div class="form-group col-md-1">
    								<label for="modelo.imageChannels">Canales.</label>
      								<s:input path="modelo.imageChannels" class="form-control" placeholder="Alto de imagen" name="imageChannels" id="imageChannels" required="true"/>
    								<div class="invalid-tooltip">
      										Debe indicar número de canales.
    								</div>
    							</div>
    							<div class="form-group col-md-3">
    								<div class="input-group mb-3">
										  <div class="input-group-prepend">
										    <label class="input-group-text" for="tipoAlmacenamiento">Origen del fichero:</label>
										  </div>
										  <select class="custom-select" id="tipoAlmacenamiento" name="tipoAlmacenamiento" onChange="mostrar_control();">
										  
										    	<c:forEach var="ta" items="${tiposAlm}" varStatus="loop">
										    		<c:choose>
										    			<c:when test="${ta.idTipoAlmacenamiento == modelo.tipoAlmacenamiento}">
										    				<option selected="true" value=${ta.idTipoAlmacenamiento}>${ta.idTipoAlmacenamiento} - ${ta.nombre}
										    				</option>
										    			</c:when>
										    			<c:otherwise>
										    				<option value=${ta.idTipoAlmacenamiento}>${ta.idTipoAlmacenamiento} - ${ta.nombre}
										    				</option>
										    			</c:otherwise>
										    		</c:choose>
										    	</c:forEach>
										  </select>
									</div>
    							</div>
    							<div class="form-group col-md-2">
    								<div class="input-group mb-2">
										  <div class="input-group-prepend">
										    <label class="input-group-text" for="tipofichero">Tipo de fichero:</label>
										  </div>
										  <select class="custom-select" id="tipoFichero" name="tipoFichero">
										    	<c:forEach var="tf" items="${tiposFic}" varStatus="loop">
										    		<c:choose>
										    			<c:when test="${tf.idTipoFichero == modelo.tipoFichero}">
										    				<option selected="true" value=${tf.idTipoFichero}>${tf.idTipoFichero} - ${tf.nombreCorto}
										    				</option>
										    			</c:when>
										    			<c:otherwise>
										    				<option value=${tf.idTipoFichero}>${tf.idTipoFichero} - ${tf.nombreCorto}
										    				</option>
										    			</c:otherwise>
										    		</c:choose>
										    	</c:forEach>
										  </select>
									</div>
    							</div>
    							<div class="form-group col-md-2">
    								<div class="input-group mb-2">
										  <div class="input-group-prepend">
										    <label class="input-group-text" for="tipoPrediccion">Tipo de fichero:</label>
										  </div>
										  <select class="custom-select" id="tipoPrediccion" name="tipoPrediccion">
										    	<c:forEach var="tp" items="${tiposPred}" varStatus="loop">
										    		<c:choose>
										    			<c:when test="${tp.idTipoPrediccion == modelo.tipoPrediccion}">
										    				<option selected="true" value=${tf.idTipoPrediccion}>${tp.idTipoPrediccion} - ${tp.nombre}
										    				</option>
										    			</c:when>
										    			<c:otherwise>
										    				<option value=${tp.idTipoPrediccion}>${tp.idTipoPrediccion} - ${tp.nombre}
										    				</option>
										    			</c:otherwise>
										    		</c:choose>
										    	</c:forEach>
										  </select>
									</div>
    							</div>
  							</div>
  							<div class="form-group">
  								<div class="form-group col-md-2">
    								<div class="input-group mb-2">
										  <div class="input-group-prepend">
										    <label class="input-group-text" for="tipoSalida">Tipo de salida:</label>
										  </div>
										  <select class="custom-select" id="tipoSalida" name="tipoSalida">
										    	<c:forEach var="ts" items="${tiposSal}" varStatus="loop">
										    		<c:choose>
										    			<c:when test="${ts.idTipoSalida == modelo.tipoSalida}">
										    				<option selected="true" value=${ts.idTipoSalida}>${ts.idTipoSalida} - ${ts.nombre}
										    				</option>
										    			</c:when>
										    			<c:otherwise>
										    				<option value=${ts.idTipoSalida}>${ts.idTipoSalida} - ${ts.nombre}
										    				</option>
										    			</c:otherwise>
										    		</c:choose>
										    	</c:forEach>
										  </select>
									</div>
    							</div>
  							</div>
  							<div class="form-group">
  								<div class="form-group col-md-6">
  									<div id="rutaFichero" style="visibility:hidden">
  										<label for="modelo.imageChannels">Ruta al recurso del fichero.</label>
      									<s:input path="modelo.pathToModel" class="form-control" placeholder="ruta a fichero h5" name="pathToModel" id="pathToModel"/>
    								</div>
  								</div>
  								<div class="form-group col-md-6">
  									<div class="custom-file" id="uploadFichero">
  										No se puede editar el fichero almacenado en base de datos. Cree nuevo modelo.
  									</div>
  								</div>
  							</div>
  							<div class="form-group">
  								<div class="form-group col-md-12">
  									<button type="button" id="grabar" name="grabar" class="btn btn-primary" data-toggle="modal" data-target="#confirmacionModal">
  										Grabar cambios
									</button>
									<button value="Cancelar" type="submit" name="cancelar"  id="cancelar" class="btn btn-primary">Volver</button>
  								</div>
  							</div>
  						</div>
  						
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
				<div class ="col-2">
 				</div>
			</div> <!-- row -->
		</div>
	</body>
</html>