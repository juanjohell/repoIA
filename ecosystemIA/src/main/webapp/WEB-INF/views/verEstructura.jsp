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
  	<div class="container">
	  <div class="jumbotron" class="w-75 p-3" >
	  	<span class="border border-primary">
	    		<h3 class="display-4">Aplicación para uso de redes convolucionales entrenadas</h3>
	    		<div class="container-fluid">
	    			<h2>${modelo.nombreModelo}</h2><p>
	    			<form method="POST" action="${pageContext.request.contextPath}/verEstructura.do">
  						<div class="d-flex p-2">
  							${estructura}
  						</div>
  						<button value="cancelar" type="submit" name="cancelar"  id="cancelar" class="btn btn-primary">Volver</button>
					</form>
  					
				</div>
	  	</span>
	  </div>
	 </div>
  </body>
</html>