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
                            <div class="col-md-6">
                                <form role="form" method="POST" action="uploadimgctlr.do" enctype="multipart/form-data">
                                    <div class="form-group">
                                        <label>Imagen de entrada</label>
                                        <input type="file" method="POST" accept=".jpg" name="file"/>
                                    </div>  
                                    <div class="form-group">
                                        <button type="submit" class="btn btn-primary">Subir imagen</button>
                                    </div>
                                </form>
                            </div>
                            	<!-- Bootstrap Progress bar -->
    							<div class="progress">
      								<div id="progressBar" class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%">0%</div>
    							</div>
                       	 </div>
                        <%if (latestPhotoUpload != null && !"".equals(latestPhotoUpload)) {%>
                        
                        <form role="form" method="POST" action="testCnnModel.do" enctype="multipart/form-data">
                                    <div class="row">
                            			<div class="col-md-12">
                                			<img src="<%=baseURL%>/img/<%=latestPhotoUpload%>" class="col-md-12"/>
                            			</div>
                            			<input type="hidden" value="<%=baseURL%>/img/<%=latestPhotoUpload%>" name="imagen">
                        			</div>
                                    <div class="form-group">
                                        <label>File input</label>
                                        <input type="file" method="POST" accept=".jpg" name="file"/>
                                    </div>  
                                    <div class="form-group">
                                        <button type="submit" class="btn btn-primary">Predecir imagen</button>
                                    </div>
                                    
                         </form>
                        <%}%>
                        <%if (resultado != null && !"".equals(resultado)) {%>
                                    	<%=resultado%>
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