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
<title>Nuevo modelo</title>
  	<body>
  		<div class="container">
  			<div class="row">
 				<div class ="col">
 				</div>
 				<div class ="col-6">
  					<h1 class="display-3">Añadir nuevo modelo</h1>
							<form method="POST" action="${pageContext.request.contextPath}/nuevoModelo.do" enctype="multipart/form-data" class ="card p-3 bg-light">
								<div class="form-row">
					  				<div class="form-group">
					   				 	<div class="form-group col-md-12">
					   				 		<label for="modelo.nombreModelo">Nombre de modelo.</label>
					      					<s:input path="modelo.nombreModelo" class="form-control" name="nombreModelo" id="nombreModelo"/>
					    				</div>
					  				</div>
					  				<div class="form-group">
					  					<div class="form-group col-md-12">
					  						<label for="modelo.descripcion">Descripcion.</label>
					      					<s:textarea path="modelo.descripcion" class="form-control" placeholder="Descripción del modelo" name="descripcion" id="descripcion"/>
					  					</div>
					  				</div>
					    			<div class="form-group col-md-12">
					    				Características para la capa de entrada del modelo:
					    			</div>	  				
					  				<div class="form-group" id="datosEntradaModelo">
					  					<div class="form-group col-md-2">
					    					<label for="modelo.modelImageHeight">Alto de imagen.</label>
					      					<s:input path="modelo.modelImageHeight" class="form-control" placeholder="Alto de imagen" name="modelImageHeight" id="modelImageHeight"/>
					    				</div>
					    				<div class="form-group col-md-2">
					    					<label for="modelo.modelImageWidth">Ancho de imagen.</label>
					      					<s:input path="modelo.modelImageWidth" class="form-control" placeholder="Ancho de imagen" name="modelImageWidth" id="modelImageWidth"/>
					    				</div>
					    				<div class="form-group col-md-2">
					    					<label for="modelo.imageChannels">Canales.</label>
					      					<s:input path="modelo.imageChannels" class="form-control" placeholder="Canales" name="imageChannels" id="imageChannels"/>
					    				</div>
					    				<div class="form-group col-md-6">
					    					<div>&nbsp;</div>
					    				</div>
					    			</div>
					    			<div class="form-group col-md-12">
					    				Características para la capa de salida del modelo:
					    			</div>
					  				<div class="form-group" id="datosSalidaModelo">
					    				<div class="form-group col-md-3">
		    								<div class="input-group mb-3">
												  <div class="input-group-prepend">
												    <label class="input-group-text" for="tipoPrediccion">Tipo de predicción:</label>
												  </div>
												  <select class="custom-select" id="tipoPrediccion" name="tipoPrediccion">
												    	<c:forEach var="tp" items="${tiposPred}" varStatus="loop">
												    		<option value=${tp.idTipoPrediccion}>${tp.idTipoPrediccion} - ${tp.nombre}
												    		</option>
												    	</c:forEach>
												  </select>
											</div>
		    							</div>
		    							<div class="form-group col-md-3">
		    								<div class="input-group mb-3">
												  <div class="input-group-prepend">
												    <label class="input-group-text" for="tipoSalida">Tipo salida:</label>
												  </div>
												  <select class="custom-select" id="tipoSalida" name="tipoSalida">
												    	<c:forEach var="ts" items="${tiposSal}" varStatus="loop">
												    		<option value=${ts.idTipoSalida}>${ts.idTipoSalida} - ${ts.nombre}
												    		</option>
												    	</c:forEach>
												  </select>
											</div>
		    							</div>
					    				<div class="form-group col-md-6">
					    					<div>&nbsp;</div>
					    				</div>
					  				</div>
					    			<div class="form-group col-md-12">
					    				Características del fichero del modelo:
					    			</div>
					  				<div class="form-group">
					  					<div class="form-group col-md-3">
		    								<div class="input-group mb-3">
												  <div class="input-group-prepend">
												    <label class="input-group-text" for="tipoAlmacenamiento">Origen del fichero:</label>
												  </div>
												  <select class="custom-select" id="tipoAlmacenamiento" name="tipoAlmacenamiento" onChange="mostrar_control();">
												    	<c:forEach var="ta" items="${tiposAlm}" varStatus="loop">
												    		<option value=${ta.idTipoAlmacenamiento}>${ta.idTipoAlmacenamiento} - ${ta.nombre}
												    		</option>
												    	</c:forEach>
												  </select>
											</div>
		    							</div>
		    							<div class="form-group col-md-2">
		    								<div class="input-group mb-3">
												  <div class="input-group-prepend">
												    <label class="input-group-text" for="tipofichero">Tipo de fichero:</label>
												  </div>
												  <select class="custom-select" id="tipoFichero" name="tipoFichero">
												    	<c:forEach var="tf" items="${tiposFic}" varStatus="loop">
												    		<option value=${tf.idTipoFichero}>${tf.idTipoFichero} - ${tf.nombreCorto}
												    		</option>
												    	</c:forEach>
												  </select>
											</div>
		    							</div>
					  					<div class="form-group col-md-3">
					  						<div id="rutaFichero" style="visibility:hidden">
					    						<label for="modelo.imageChannels">Ruta al recurso del fichero.</label>
					      						<s:input path="modelo.pathToModel" class="form-control" placeholder="ruta a fichero" name="pathToModel" id="pathToModel"/>
					    					</div>
					    				</div>
					    				<div class="form-group col-md-4">
					    					<div class="custom-file" id="uploadFichero">
		  										<input type="file" class="custom-file-input" id="ficheroModelo" name="ficheroModelo" lang="es">
		  										<label class="custom-file-label" for="customFileLang">Seleccionar Archivo</label>
											</div>
					    				</div>
					  				</div>
					  				<div class="form-group">
					  					<div class="form-group col-md-12">
											<button value="Grabar" type="submit" name="grabar"  id="grabar" class="btn btn-primary">Grabar</button>
											<button type="button" id="cancelar" name="cancelar" class="btn btn-primary" data-toggle="modal" data-target="#confirmacionModal">
		  										Cancelar
											</button>
										</div>
					    			</div>
					  			
								<!-- Modal -->
									<div class="modal fade" id="confirmacionModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
									  <div class="modal-dialog" role="document">
									    <div class="modal-content">
									      <div class="modal-header">
									        <h5 class="modal-title" id="exampleModalLabel">ATENCIÓN.</h5>
									        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
									          <span aria-hidden="true">&times;</span>
									        </button>
									      </div>
									      <div class="modal-body">
									      <h4 class="modal-title" id="exampleModalLabel">Se perderán los datos introducidos. ¿Salir a modelos?</h4>
									      </div>
									      <div class="modal-footer">
									        <button value="Cancelar" type="submit" name="cancelar" id="cancelar" class="btn btn-primary">Si</button>
									      </div>
									    </div>
									  </div>
									</div>
								<!-- fin ventana modal -->
								</div>
							</form>
						</div>
				<div class="col">
				</div>
			</div>
		</div>
	</body>
</html>