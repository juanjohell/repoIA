<%@page import="es.ubu.ecosystemIA.controller.FileUploadController"%>
<%@page import="es.ubu.ecosystemIA.controller.FileBaseController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            String baseURL = (String) request.getAttribute(FileBaseController.PARAM_BASE_URL);
            String latestPhotoUpload = (String) request.getAttribute(FileUploadController.PARAM_LATESTPHOTO);
            String resultado = (String) request.getAttribute(FileUploadController.PARAM_RESULTADO);
        %>
        <title>Upload Photo</title>
        <!-- BOOTSTRAP STYLES-->
        <link href="<%=baseURL%>/css/bootstrap.min.css" rel="stylesheet" />
 
    </head>
    <body>
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="panel panel-primary" style="margin-bottom: 0px;">
                    <div class="panel-heading">
                        Form Upload Photo
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-6">
                                <form role="form" method="POST" action="<%=baseURL%>/uploadimgctlr/" enctype="multipart/form-data">
                                    <div class="form-group">
                                        <label>File input</label>
                                        <input type="file" method="POST" accept=".jpg" name="file"/>
                                    </div>  
                                    <div class="form-group">
                                        <button type="submit" class="btn btn-primary">Upload</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <%if (latestPhotoUpload != null && !"".equals(latestPhotoUpload)) {%>
                        
                        <form role="form" method="POST" action="<%=baseURL%>/testCnnModel/" enctype="multipart/form-data">
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
    </body>
</html>