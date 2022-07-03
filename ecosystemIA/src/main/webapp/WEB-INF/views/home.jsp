<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="es.ubu.ecosystemIA.controller.FileBaseController"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/WEB-INF/views/cabecera.jsp" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            String baseURL = (String) request.getAttribute(FileBaseController.PARAM_BASE_URL);
        %>
  <title><h1><h1><fmt:message key="titulo"/></h1></title>
  </head>
  <body>
  
  <div class="card" style="width: 18rem;">
  	<img class="card-img-top" src="..." alt="Card image cap">
  		<div class="card-body">
    		<h5 class="card-title">Aplicación para uso de redes convolucionales entrenadas</h5>
    		<p class="card-text">Actualmente está cargado por defecto el modelo...</p>
    		<form method="POST" action="modelos.do">
  				<div class="form-row align-items-center">
    				<h2></h2>
    				<p>Hora <c:out value="${now}"/></p>
    				<button type="submit" class="btn btn-success">COMENZAR</button>
     			<div>
  			</form>
  		</div>
	</div>
 
  </body>
</html>