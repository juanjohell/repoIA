<%@ page language="java" contentType="text/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags/form"%>

<!-- CSS for Bootstrap -->
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto|Varela+Round">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<!-- JQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="<c:url value='/resources/css/ecosystemia.css' />"/>

<script type="text/javascript">
function ocultar (id_element) {
	document.getElementById(id_element).style.visibility = 'hidden';
}
function mostrar (id_element) {
	document.getElementById(id_element).style.visibility = 'visible';
}

</script>

<div class="container">
  <div class="jumbotron_cabecera bg-info text-white">
    <h1>EcosystemIA</h1>
    <img class="rounded float-start" src="resources/img/portada.png" alt="portada" >
    <p>Gesti�n y uso de modelos de redes neuronales para visi�n artificial.</p>
  </div>
</div>