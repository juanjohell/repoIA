<%@page import="es.ubu.ecosystemIA.controller.FileUploadController"%>
<%@page import="es.ubu.ecosystemIA.controller.FileBaseController"%>
<%@page import="es.ubu.ecosystemIA.controller.EcosystemIAController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
    
<%@ include file="/WEB-INF/views/cabecera.jsp" %>
<!DOCTYPE html>
<html>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 	 <% String msg_error_ficheros = (String) request.getAttribute(FileUploadController.PARAM_ERROR); %>
 	 <% String msg_error_aplicacion = (String) request.getAttribute(EcosystemIAController.PARAM_ERROR); %>
<title>Insert title here</title>
</head>
<body>
<div class="alert alert-danger" role="alert">
  Oops! Se ha producido un error. <br>
  <%=msg_error_ficheros%>
</div>
<form method="POST" action="home.do">
	  <div class="form-row align-items-center">
	    	<h2></h2>
	    	<button type="submit" class="btn btn-primary btn-lg">Inicio</button>
	  <div>
</form>
</body>
</html>