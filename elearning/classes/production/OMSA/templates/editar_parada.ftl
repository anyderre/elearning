<!DOCTYPE html>
<html lang="en"  xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<title>Editar Parada</title>
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
                        Modificar una parada
                    </h3>
                    <ol class="breadcrumb">
                        <li>
                            <i class="fa fa-dashboard"></i> <a href="/home">Home</a>
                        </li>
                        <li>
                            <i class="fa fa-table"></i> <a href="/ruta/">Tabla de Corredores</a>
                        </li>
                        <li>
                            <i class="fa fa-table"></i> <a href="/ruta/listar/paradas/${parada.ruta.id}">Tabla de Coordenadas</a>
                        </li>
                        <li class="active">
                            <i class="fa fa-edit"></i> Modificar Parada
                        </li>
                    </ol>
                </div>
            </div>
            <form role="form" action="/parada/editar" method="POST">
                <div class="row">
                    <div class="col-lg-6">
                        <div class="form-group">
                            <label for="nombre">Nombre</label>
                            <input type="text" class="form-control" min="2" max="100" name="nombre" id="nombre" value="${parada.nombre}" required>
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
                    <div class="col-lg-6">
                        <div class="form-group">
                            <label for="latitude">Latitud</label>
                            <input type="number" id="latitude" step="0.00000001" name="latitude" class="form-control" value="${parada.coordenada.latitude?c}" required>
                        </div>

                    </div>
                    <div class="col-lg-6">
                        <div class="form-group">
                            <label for="longitud">Longitud</label>
                            <input type="number" class="form-control" step="0.00000001" name="longitud" id="longitud" value="${parada.coordenada.longitud?c}" required>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-lg-6">
                        <div class="form-group">
                            <label for="paradaAnterior">Parada anterior</label>
                            <select class="form-control  selectpicker" data-live-search="true" data-size="5" name="paradaAnterior" id="paradaAnterior">
                                <option selected disabled>Elija una parada anterior</option>
                                    <#if paradas??>
                                        <#list paradas as p>
                                            <option value="${p.id}">${p.nombre}</option>
                                        </#list>

                                    </#if>
                            </select>
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <div class="form-group">
                            <label for="paradaSiguiente">Parada Siguiente</label>
                            <select class="form-control selectpicker" data-live-search="true" data-size="5" name="paradaSiguiente" id="paradaSiguiente">
                                <option selected disabled>Elija una parada siguiente</option>
                                    <#if paradas??>
                                        <#list paradas as p>
                                            <option value="${p.id}">${p.nombre}</option>
                                        </#list>

                                    </#if>
                            </select>
                        </div>
                    </div>
                </div>
                <input type="hidden" name="id_parada" value="${parada.id}">


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

        </form>
    <#if message??>
        <#if message == 'error'>
            <small class="text-danger">No se pudo modificar la parada!!!</small>
        </#if>
    </#if>
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
$('.selectpicker').selectpicker();

$(document).ready(function() {
    $('.ruta option')
            .removeAttr('selected')
            .filter('[value=${parada.ruta.id}]')
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