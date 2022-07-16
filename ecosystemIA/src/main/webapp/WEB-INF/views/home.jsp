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
  <div class="jumbotron" class="w-75 p-3" >
  	<span class="border border-primary">
  			<h1 class="display-1">EcosystemIA</h1>
    		<h3 class="display-4">Aplicación para uso de redes convolucionales entrenadas</h3>
    		<p class="lead">Actualmente está cargado por defecto el modelo...</p>
    		<form method="POST" action="modelos.do">
  				<div class="form-row align-items-center">
    				<h2></h2>
    				<button type="submit" class="btn btn-primary btn-lg">COMENZAR</button>
     			<div>
  			</form>
  	</span>
  </div>
  </body>
</html>