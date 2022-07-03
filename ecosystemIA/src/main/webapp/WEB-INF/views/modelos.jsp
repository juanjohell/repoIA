<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/WEB-INF/views/cabecera.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
  <meta charset="ISO-8859-1">
  <title>EcosystemIA</title>
  </head>
  <body>
      <div class="form-row align-items-center">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="panel panel-primary" style="margin-bottom: 0px;">
                    <div class="panel-heading">
                       <h1><fmt:message key="titulo_modelos"/></h1>
                    </div>
  					<div class="panel-body">
                        <div class="row">
                            <div class="col-md-6">
  								<table border="0"> 
	  								<!--  Nombre de modelo mvc en controller para esta página, nombre tabla en POJO -->
	  								<tr><td colspan="2"></td></tr>
	    							<c:forEach var="m" items="${modeloMVC.listadoModelos}" varStatus="loop">
	      							<tr><td>
	      									${m.nombreModelo}</td><td> ${m.descripcion}</td></tr>
	    							</c:forEach>
  								</table>
    							<form method="POST" action="cargarModelo.do">
    									<button type="submit" class="btn btn-success">COMENZAR</button>
    							</form>
    						</div>
                		</div>
            		</div>            
        		</div>
        	</div>            
        </div>
       </div>
  	</body>
</html>