<!DOCTYPE html>
<html lang="en" ng-app="angularTable" xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<title>Ver Ruta</title>
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
                    Lista de Corredores
                </h3>
                <ol class="breadcrumb">
                    <li>
                        <i class="fa fa-dashboard"></i> <a href="/home">Home</a>
                    </li>
                    <li class="active">
                        <i class="fa fa-table"></i> Tabla de Corredores
                    </li>
                </ol>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <h2> Rutas</h2>
                <div class="table-responsive" ng-controller="rutaTableControllers" ng-init="getData(pageno)" >
                    <table class="table table-bordered table-hover table-striped">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Nombre Corredor</th>
                                <th>Distancia Total (KM)</th>
                                <th>Ciudad</th>
                                <th>Direcci&oacute;n</th>
                                <th>&nbsp</th>
                                <th>&nbsp</th>
                                <th>&nbsp</th>
                                <th>&nbsp</th>
                                <th>&nbsp</th>
                                <th>&nbsp</th>
                            </tr>
                        </thead>
                        <tbody>
                        <tr ng-show="rutas.length <= 0"><td colspan="11" style="text-align:center;">Leyendo Nuevas Rutas!!</td></tr>
                        <tr dir-paginate="r in rutas | itemsPerPage:itemsPerPage" total-items="${size}">
                            <td>{{start+$index+1}}</td>
                            <td>{{r.nombreCorredor}}</td>
                            <td>{{r.distanciaTotal}}</td>
                            <td>{{r.ciudad}}</td>
                            <td ng-bind ="isTrue({{r.esDireccionSubida}})? 'Subida' : 'Bajada'"></td>
                            <td><a href="/ruta/listar/paradas/{{r.id}}">Ver Paradas</a></td>
                            <td><a href="/ruta/listar/coordenadas/{{r.id}}">Ver Coordenadas</a></td>
                            <td><a href="/parada/crear/{{r.id}}">Agregar Parada</a></td>
                            <td><a href="/coordenada/crear/{{r.id}}">Agregar Coordenada</a></td>
                            <td>
                                <a href="/ruta/editar/{{r.id}}">
                                    <p data-placement="top" data-toggle="tooltip" title="Editar"><button class="btn btn-primary btn-xs" data-title="Editar" data-toggle="modal" data-target="#edit"><span class="glyphicon glyphicon-pencil"></span></button></p>
                                </a>
                            </td>
                            <td>
                                <a href="/ruta/eliminar/{{r.id}}" onclick="return confirm('Estas seguro que quieres esta ruta?');">
                                    <p data-placement="top" data-toggle="tooltip" title="Eliminar"><button class="btn btn-danger btn-xs" data-title="Eliminar" data-toggle="modal" data-target="#delete"><span class="glyphicon glyphicon-trash"></span></button></p>
                                </a>
                            </td>

                        </tr>
                        </tbody>
                    </table>
                    <center><dir-pagination-controls
                            max-size="10"
                            direction-links="true"
                            boundary-links="true"
                            on-page-change="getData(newPageNumber)" >
                    </dir-pagination-controls></center>

                </div>
            </div>
        </div>
        <!-- /.row -->

    </div>
    <!-- /.container-fluid -->

</div>
<!-- /#page-wrapper -->
</div>
<!-- /#wrapper -->

<!-- jQuery -->
<script src="/js/jquery.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="/js/bootstrap.min.js"></script>

<script src="/js/dirPagination.js"></script>
<script src="/js/appTable.js"></script>
<script src="/js/tableControllers/rutaTableControllers.js"></script>

</body>

</html>