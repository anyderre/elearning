<!DOCTYPE html>
<html lang="en"  xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<title>Editar Autobus</title>
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
                   Modificar un autobus
                </h3>
                <ol class="breadcrumb">
                    <li>
                    <i class="fa fa-dashboard"></i> <a href="/home">Home</a>
                </li>
                    <li>
                        <i class="fa fa-table"></i> <a href="/autobus/">Tabla de Autobuses</a>
                    </li>
                    <li class="active">
                        <i class="fa fa-edit"></i> Modificar Autobus
                    </li>
                </ol>
            </div>
        </div>

        <form action="/autobus/editar" method="POST">
        <#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->
            <div class="row">
                <div class="col-lg-6">
                    <div class="form-group">
                        <label for="modelo">Modelo</label>
                        <input type="text" class="form-control" placeholder="Modelo del autobus" min="2" max="35" name="modelo" id="modelo" <#if autobus.modelo??>value="${autobus.modelo}"</#if>>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="form-group">
                        <label for="cantidadDeAsientos">Cantidad de Asientos</label>
                        <input type="number" class="form-control" placeholder="Cantidad de asientos" name="cantidadDeAsientos" min="0" max="100" <#if autobus.cantidadDeAsientos??>value="${autobus.cantidadDeAsientos}"</#if> id="cantidadDeAsientos" required>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-6">
                    <div class="form-group">
                    <#--<#setting number_format="##0.00">-->
                    <#setting locale="en_US">
                        <label for="peso">Peso</label>

                        <div class="input-group">
                            <input type="number" id="peso" step="0.01" name="peso" min="0" max="35000"  <#if autobus.peso??>value="${autobus.peso?string["##0.00"]}"</#if> class="form-control">
                            <span class="input-group-addon">kg</span>
                        </div>

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
                    <div class="form-group anoFabricacion">
                        <label for="anoFabricacion">A&ntilde;o Fabricaci&oacute;n</label>
                        <select class="form-control selectpicker" data-live-search="true" data-size="5" name="anoFabricacion" id="fabricacion">
                            <option selected disabled>Elija el a&ntilde;o de fabricaci&oacute;n</option>
                        </select>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="form-group">
                        <label for="conductor">Conductor</label>
                        <input type="text" class="form-control" pattern="^(\s)*[A-Za-z]+((\s)?((\'|\-|\.)?([A-Za-z])+))*(\s)*$" placeholder="Nombre del conductor"  <#if autobus.conductor??>value="${autobus.conductor}"</#if> name="conductor" min="2" max="100" id="conductor" required>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-6">
                    <div class="form-group">
                        <label for="precio">Precio</label>
                        <input type="number" min="10" placeholder="Entre el precio" class="form-control" name="precio"  <#if autobus.precio??>value="${autobus.precio}"</#if> id="precio" required>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="form-group">
                        <label for="tieneAire">Tiene aire acondicionado</label>
                        <br>
                        <label class="radio-inline">
                            <input type="radio" name="tieneAireAcondicionado" id="noTieneAireAcondicionado" value=true checked>Si
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="tieneAireAcondicionado" id="noTieneAireAcondicionado" value=false >No
                        </label>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-6">
                    <div class="form-group">
                        <label for="raspberryPiNumeroSerial">Raspberry Pi / Numero de Serie</label>
                        <input type="text" pattern="^[A-Za-z0-9-]+$" placeholder="Entre el numero serial del raspberry" class="form-control" name="raspberryPiNumeroSerial" min="0" max="100" id="raspberryPiNumeroSerial" value="${autobus.raspberryPiNumeroSerial}" required>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="form-group">
                        <label for="matricula">Matricula</label>
                        <input type="text" class="form-control" pattern="([0-9]{2})((-[0-9]{3})|([0-9]{3}))" placeholder="Matricula del autobus" name="matricula" min="5" max="6" id="matricula" value="${autobus.matricula}" required>
                    </div>
                </div>
            </div>
            <div class="row">
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
            <input type="hidden" name="autobus" value="${autobus.id}">
        </form>
    <#if message??>
        <#if message == 'success'>
            <small class="text-success">Coordenada guardada!!!</small>
        <#else>
            <small class="text-danger">No se pudo guardar la coordenada!!!</small>
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
</script>
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/css/bootstrap-select.min.css">
<!-- Latest compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/js/bootstrap-select.min.js"></script>


<script type="text/javascript">
    // get the OPTION we want selected



    $(document).ready(function() {
        <#if autobus.ruta??>
            $('.ruta option')
                    .removeAttr('selected')
                    .filter('[value=${autobus.ruta.id}]')
                    .attr('selected', true);

        </#if>

        var date = new Date();
        for (var i = 1950; i < date.getFullYear(); i++) {
            $("#fabricacion").append($("<option>").attr(value, i).append(i));}


    $('.anoFabricacion option')
            .removeAttr('selected')
            .filter('[value=${autobus.anoFabricacion}]')
            .attr('selected', true);

    });
</script>


<!-- Bootstrap Core JavaScript -->
<script src="/js/bootstrap.min.js"></script>

</body>

</html>