<!DOCTYPE html>
<html lang="en"xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<title>Crear Parada</title>
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
                            Crear una nueva parada
                        </h3>
                        <ol class="breadcrumb">
                            <li>
                                <i class="fa fa-dashboard"></i> <a href="/home">Home</a>
                            </li>

                            <li class="active">
                                <i class="fa fa-edit"></i> Crear Parada
                            </li>
                        </ol>
                    </div>
                </div>
                <a href="/ruta/listar/paradas/${ruta.id}">Ver Paradas</a>
                <hr>
                <form role="form" action="/parada/crear" th:object="${parada}" method="POST">
                    <div class="row">
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label for="latitude">Latitud</label>
                                <input type="number" id="latitude" placeholder="Entre la latitud" step="0.00000001" name="latitude" class="form-control" required>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label for="longitud">Longitud</label>
                                <input type="number" class="form-control" step="0.00000001" placeholder="Entre la longitud" name="longitud" id="longitud" required>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label for="nombre">Nombre</label>
                                <input type="text" class="form-control" min="2" placeholder="Entre el nombre de la parada" max="100" name="nombre" id="nombre" required>
                            </div>
                        </div>

                        <div class="col-lg-6">
                            <div class="form-group ruta">
                                <label for="ruta">Ruta</label>
                                <select class="form-control selectpicker" data-live-search="true" data-size="5" name="ruta" id="ruta" required>
                                    <option selected disabled>Elija una ruta</option>
                                        <#if rutas??>
                                            <#list rutas as ruta>
                                                <option value="${ruta.id}">${ruta.nombreCorredor} | <#if ruta.esDireccionSubida>Subida <#else> Bajada </#if></option>
                                            </#list>
                                        </#if>
                                </select>
                            </div>
                        </div>

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
                    </div>
                <#if message??>
                    <#if message=="success">
                        <small class="text-success">Parada guardada!!!</small>
                    <#else>
                        <small class="text-danger">No se pudo guardar la parada!!!</small>
                    </#if>

                    </#if>
                </form>
            </div>

        </div>
        <!-- /.container-fluid -->

    </div>
    <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="/js/jquery.js">
    </script>

<script type = "text/javascript" >

    $(document).ready(function() {
        $('.ruta option')
                .removeAttr('selected')
                .filter('[value=${ruta.id}]')
                .attr('selected', true);
        })
</script>
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/css/bootstrap-select.min.css">
<!-- Latest compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/js/bootstrap-select.min.js"></script>


<!-- Bootstrap Core JavaScript -->
    <script src="/js/bootstrap.min.js"></script>

</body>

</html>
