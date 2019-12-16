<!DOCTYPE html>
<html lang="en" ng-app="angularTable" xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<title>Ver Usuarios</title>
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
                        Ver usuarios
                    </h3>
                    <ol class="breadcrumb">
                        <li>
                            <i class="fa fa-dashboard"></i> <a href="/home">Home</a>
                        </li>

                        <li class="active">
                            <i class="fa fa-table"></i> Tabla de Usuarios
                        </li>
                    </ol>
                </div>
            </div>
            <!-- /.row -->


            <div class="row">
                <div class="col-lg-12">
                    <div class="table-responsive" ng-controller="usuarioTableController">
                        <table class="table table-bordered table-hover table-striped" ng-init="getData(pageno)">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Nombre</th>
                                <th>Nombre de Usuario</th>
                                <th>Roles</th>
                                <th>&nbsp</th>
                                <th>&nbsp</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-show="usuarios.length <=0">
                                <td colspan="7" style="text-align:center;">Leyendo Nuevos Datos!!</td>
                            </tr>
                            <tr dir-paginate="usuario in usuarios|itemsPerPage:itemsPerPage" total-items="${size}">
                                <td>{{start+$index+1}}</td>
                                <td>{{usuario.name}}</td>
                                <td>{{usuario.username}}</td>
                                <td> <span ng-repeat="x in usuario.roles">{{x.rol}}/</span></td>
                                <#--<td>{{usuario.roles..rol/usuario.roles.1.rol}}</td>-->
                                <td>
                                    <a href="/zonaAdmin/editar/{{usuario.id}}">
                                        <p data-placement="top" data-toggle="tooltip" title="Editar">
                                            <button class="btn btn-primary btn-xs" data-title="Editar" data-toggle="modal"
                                                    data-target="#edit"><span class="glyphicon glyphicon-pencil"></span>
                                            </button>
                                        </p>
                                    </a>
                                </td>
                                <td>
                                    <a href="/zonaAdmin/eliminar/usuario/{{usuario.id}}" onclick="return confirm('Estas seguro que quieres eliminar este usuario ?');">
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
<script src="/js/jquery.js">
</script>

<!-- Bootstrap Core JavaScript -->
<script src="/js/bootstrap.min.js"></script>
<script src="/js/dirPagination.js"></script>
<script src="/js/appTable.js"></script>
<script src="/js/tableControllers/usuarioTableController.js"></script>

</body>

</html>
