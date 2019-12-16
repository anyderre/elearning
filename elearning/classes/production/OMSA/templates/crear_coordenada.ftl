<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<title>Crear Coordenada</title>
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
                        Crear una nueva coordenada
                    </h3>
                    <ol class="breadcrumb">
                        <li>
                            <i class="fa fa-dashboard"></i> <a href="/home">Home</a>
                        </li>

                        <li class="active">
                            <i class="fa fa-edit"></i> Crear Coordenada
                        </li>
                    </ol>
                </div>
            </div>
            <a href="/ruta/listar/coordenadas/${ruta.id}">Ver Ruta</a>
            <hr>
            <form role="form" action="/coordenada/crear" method="POST">

                <div class="row">
                    <div class="col-lg-6">
                        <div class="form-group">
                            <label for="latitude">Latitud</label>
                            <input type="number" id="latitude" step="0.00000001" placeholder="Entre la latitud"
                                   name="latitude" class="form-control" required>
                        </div>

                    </div>
                    <div class="col-lg-6">
                        <div class="form-group">
                            <label for="longitud">Longitud</label>
                            <input type="number" class="form-control" step="0.00000001" name="longitud"
                                   placeholder="Entre la logitud" id="longitud" required>
                        </div>
                    </div>
                </div>
                <input type="hidden" name="ruta" value="${ruta.id}">

                <div class="row"></div>
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

<!-- Bootstrap Core JavaScript -->
<script src="/js/bootstrap.min.js"></script>

</body>

</html>
