<%@page import="es.ubu.ecosystemIA.controller.FileUploadController"%>
<%@page import="es.ubu.ecosystemIA.controller.FileBaseController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/WEB-INF/views/cabecera.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            String baseURL = (String) request.getAttribute(FileBaseController.PARAM_BASE_URL);
            String latestPhotoUpload = (String) request.getAttribute(FileUploadController.PARAM_LATESTPHOTO);
            String resultado = (String) request.getAttribute(FileUploadController.PARAM_RESULTADO);
        %>
        <title>Subir Imagen de entrada para probar el modelo</title>
        <!-- BOOTSTRAP STYLES-->
        <link href="css/bootstrap.min.css" rel="stylesheet" />
 
    </head>
    <body>
    <div class="form-row align-items-center">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="panel panel-primary" style="margin-bottom: 0px;">
                    <div class="panel-heading">
                        ${modelo.nombreModelo}
                        ${modelo.descripcion}
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-12">
                                <form role="form" method="POST" action="uploadimgctlr.do" enctype="multipart/form-data">
                                    <div class="form-group">
                                    	<div class="mb-3">
  											<label for="formFile" class="form-label">Seleccione imagen a probar.</label>
  											<input class="form-control" type="file" id="formFile" method="POST" accept=".jpg" name="file">
										</div>
                                    </div>  
                                    <div class="form-group">
                                        <button type="submit" class="btn btn-primary">Subir imagen</button>
                                    </div>
                                </form>
                            </div>
                            	
                       	 </div>
                        <%if (latestPhotoUpload != null && !"".equals(latestPhotoUpload)) {%>	
							<form role="form" method="POST" action="testCnnModel.do" enctype="multipart/form-data">
                        	<div class="row">
                            	<div class="col-md-12">
                                	<img src="<%=baseURL%>/img/<%=latestPhotoUpload%>" class="col-md-12"/>
                            	</div>
                            </div>
                            <div class="row">
                            	<div class="col-md-12">
                            		<input type="hidden" value="<%=baseURL%>/img/<%=latestPhotoUpload%>" name="imagen">
                        		</div>
                        	</div>
                        	<div class="row">
                            	<div class="col-md-12">  
                                    	<div class="form-group">
                                        	<button type="submit" class="btn btn-primary">Predecir imagen</button>
                                    	</div>
                                 </div>
                             </div>
                             </form>
                         	 <%if (resultado != null && !"".equals(resultado)) {%>
                         	<div class="row">
                            	<div class="col-md-12">
                            		<div class="alert alert-success">
  										<strong>Salida del modelo: </strong> <%=resultado%>
									</div>
                        		</div>
                        	</div>
                        	<%}%>
                         	<!-- Bootstrap Progress bar -->
                        	<div class="row">
                            	<div class="col-md-12">
    								<div class="progress">
  										<div class="progress-bar progress-bar-striped active" role="progressbar"
  												aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width:80%">
    												procesando imagen...
  										</div>
									</div>
								</div>
							</div>		
                        <%}%>
                        
                        
                    </div>
                </div>
            </div>            
        </div>
        <!-- Alert -->
    		<div id="alertMsg" style="color: red;font-size: 18px;"></div>
		</div>       
    </body>
</html>