<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/WEB-INF/views/cabecera.jsp" %>

<html>
  <head>
  <meta charset="ISO-8859-1">
  <title>EcosystemIA</title>
  </head>
  <body>
  <h1><fmt:message key="titulo_modelos"/></h1>
  <h3>Modelos disponibles</h3>
    <c:forEach items="${model.modelos}" var="modelo">
      <c:out value="${modelo.nombreModelo}"/> <i>$<c:out value="${modelo.descripcion}"/></i><br><br>
    </c:forEach>
    <form method="POST" action="cargarModelo.do">
    	<button type="submit" class="btn btn-success">COMENZAR</button>
    </form>
  </body>
</html>