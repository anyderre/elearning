<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<title>Eiditar Coordenada</title>
<#include "header.ftl">
<body>
<div id="wrapper">
<#include "nav.ftl">
    <div id="page-wrapper">

        <div class="container-fluid">

            <!-- Page Heading -->
            <div class="row">
                <div class="col-lg-12">
                    <h3 class="page-header">
                        Modificar una coordenada
                    </h3>
                    <ol class="breadcrumb">
                        <li>
                            <i class="fa fa-dashboard"></i> <a href="/home">Home</a>
                        </li>
                        <li>
                            <i class="fa fa-table"></i> <a href="/ruta/">Tabla de Corredores</a>
                        </li>
                        <li>
                            <i class="fa fa-table"></i> <a href="/ruta/listar/coordenadas/${ruta.id}">Tabla de Coordenadas</a>
                        </li>
                        <li class="active">
                            <i class="fa fa-edit"></i> Modificar Coordenada
                        </li>
                    </ol>
                </div>
            </div>
            <a href="/ruta/listar/coordenadas/${ruta.id}">Ver Ruta</a>

            <form role="form" action="/coordenada/editar"  method="POST">

                <div class="row">
                    <div class="col-lg-6">
                        <div class="form-group">
                            <label for="latitude">Latitud</label>
                            <input type="number" id="latitude" step="0.00000001" name="latitude" value="${latitude}" class="form-control" required>
                        </div>

                    </div>
                    <div class="col-lg-6">
                        <div class="form-group">
                            <label for="longitud">Longitud</label>
                            <input type="number" class="form-control" value="${longitud}" step="0.00000001" name="longitud" id="longitud" required>
                        </div>
                    </div>
                </div>
                <input type="hidden" name="ruta" value="${ruta.id}">
                <input type="hidden" name="coordenada" value="${id_coordenada}">
        </div>
        <div class="row">
            <hr>
            <div class="col-lg-offset-6 col-lg-6">
                <div class="col-lg-6">
                    <div class="form-group">
                        <button type="reset" class="btn btn-success form-control">Limpiar</button>
                    </div>

                </div>
                <div class="col-lg-6">
                    <div class="form-group">
                        <button type="submit" class="btn btn-success form-control">Guardar</button>
                    </div>

                </div>

            </div>
        <#if message??>${message}</#if>
        </div>

        </form>

    </div>
    <!-- /.container-fluid -->

</div>
<!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->

<!-- jQuery -->
<script src="/js/jquery.js">


</script>

<!-- Bootstrap Core JavaScript -->
<script src="/js/bootstrap.min.js"></script>

<script type = "text/javascript" >
    $('.selectpicker').selectpicker();
</script>
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/css/bootstrap-select.min.css">
<!-- Latest compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/js/bootstrap-select.min.js"></script>

</body>

</html>
