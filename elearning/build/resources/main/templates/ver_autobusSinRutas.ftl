<!DOCTYPE html>
<html lang="en" ng-app="angularTable" xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<title>Ver autobus</title>
<#include "header.ftl">
<body>
<div id="wrapper">
<#include "nav.ftl">

    <div id="page-wrapper" ng-controller="emptyAutobusTableController">

        <div class="container-fluid">

            <!-- Page Heading -->
            <div class="row">
                <div class="col-lg-12">
                    <h3 class="page-header">
                        Ver Autobuses Sin Rutas
                    </h3>
                    <ol class="breadcrumb">
                        <li>
                            <i class="fa fa-dashboard"></i> <a href="/home">Home</a>
                        </li>
                        <li class="active">
                            <i class="fa fa-table"></i> Tabla de Autobuses Sin Rutas
                        </li>
                    </ol>
                </div>
            </div>
            <!-- /.row -->
            <div class="row">
                <hr>
                <div class="col-lg-12">
                    <div class="table-responsive" ng-init="getData(pageno)">
                        <table class="table table-bordered table-hover table-striped">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Matricula</th>
                                <th>Modelo</th>
                                <th>Cant. Asientos</th>
                                <th>Peso(KG)</th>
                                <th>Corredor</th>
                                <th>A&ntilde;o Fabri.</th>
                                <th>Conductor</th>
                                <th>Inicio Monitoreo</th>
                                <th>Precio</th>
                                <th>Climatizado</th>
                                <th>Cant. Pasajeros</th>
                                <th>Activo</th>
                                <#--<th>&nbsp</th>-->
                                <th>&nbsp</th>
                                <th>&nbsp</th>
                            </tr>
                            </thead>
                            <tbody>

                            <tr ng-show="autobuses.length <= 0">
                                <td colspan="15" style="text-align:center;">Leyendo Nuevos Datos!!</td>
                            </tr>
                            <tr dir-paginate="autobus in autobuses|itemsPerPage:itemsPerPage" total-items="total_count">
                                <td>{{start+$index+1}}</td>
                                <td>{{autobus.matricula}}</td>
                                <td>{{autobus.modelo}}</td>
                                <td>{{autobus.cantidadDeAsientos}}</td>
                                <td>{{autobus.peso}}</td>
                                <td style="color: red">vacio</td>
                                <td>{{autobus.anoFabricacion}}</td>
                                <td>{{autobus.conductor}}</td>
                                <td>{{autobus.fechaCreada*1000 | date:'dd/MM/yyyy'}}</td>
                                <td>{{autobus.precio}}</td>
                                <td>
                                    <span ng-bind="isTrue({{autobus.tieneAireAcondicionado}})?'Si' : 'No'">
                                    </span>
                                </td>
                                <td>{{autobus.cantidadDePasajerosActual}}</td>
                                <td>
                                    <span ng-bind="isTrue({{autobus.activo}})?'Si' : 'No'" ng-class="isTrue({{autobus.activo}})? 'label label-success' : 'label label-danger'">
                                    </span>
                                </td>
                                <#--<td><a href="">Ver Autobus</a></td>-->
                                <td>
                                    <a href="/autobus/editar/{{autobus.id}}">
                                        <p data-placement="top" data-toggle="tooltip" title="Editar">
                                            <button class="btn btn-primary btn-xs" data-title="Editar"
                                                    data-toggle="modal" data-target="#edit"><span
                                                    class="glyphicon glyphicon-pencil"></span></button>
                                        </p>
                                    </a>
                                </td>
                                <td>
                                    <a href="/autobus/eliminar/{{autobus.id}}" onclick="return confirm('Estas seguro que quieres eliminar el autobus?');">
                                        <p data-placement="top" data-toggle="tooltip" title="Eliminar">
                                            <button class="btn btn-danger btn-xs" data-title="Eliminar"
                                                    data-toggle="modal" data-target="#delete"><span
                                                    class="glyphicon glyphicon-trash"></span></button>
                                        </p>
                                    </a>
                                </td>

                            </tr>

                            </tbody>
                        </table>
                        <center>
                            <dir-pagination-controls
                                    max-size="10"
                                    direction-links="true"
                                    boundary-links="true"
                                    on-page-change="getData(newPageNumber)">
                            </dir-pagination-controls>
                        </center>
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
<script src="/js/tableControllers/emptyAutobusTableControllers.js">

    <#--<script type = "text/javascript" >-->
            <#--$('.selectpicker').selectpicker();-->
<#--</script>-->
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/css/bootstrap-select.min.css"/>
<!-- Latest compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/js/bootstrap-select.min.js"></script>

</body>

</html>