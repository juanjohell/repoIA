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
  <div class="jumbotron_cabecera bg-info text-white" height="10%">
  	<div class = "container">
  		<div class = "row">
  			<div class = "col-md-2 col-sm-12">
  				<img class="img-responsive" src="resources/img/portada.png" alt="portada"/>
  			</div>
  			<div class = "col-md-10">
    			<h1>EcosystemIA</h1><br/>
    			<h3>Gesti�n y uso de modelos de redes neuronales para visi�n artificial.</h3>
    		</div>
    	</div>
     </div>
   </div>
</div>
