<!DOCTYPE html>
<html lang="en"xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3"
      ng-app="omsaTracker">
<title>Crear Usuario</title>
<#include "header.ftl">

<body>
<div id="wrapper" ng-controller="usuarioController">
<#include "nav.ftl">
        <div id="page-wrapper">

                        <div class="container-fluid">

                            <!-- Page Heading -->
                            <div class="row">
                                <div class="col-lg-12">
                                    <h3 class="page-header">
                                       Crear un nuevo usuario
                                    </h3>
                                    <ol class="breadcrumb">
                                        <li>
                                            <i class="fa fa-dashboard"></i> <a href="/home">Home</a>
                                        </li>

                                        <li class="active">
                                            <i class="fa fa-edit"></i> Crear Usuarios
                                        </li>
                                    </ol>
                                </div>
                            </div>

                            <form role="form" name="myForm" method="post" action="#" th:action="@{/zonaAdmin/registrar}" th:object="${user}">
                                <div class="row">
                                    <div class="col-lg-6">
                                        <div class="form-group">
                                            <label for="name">Nombre</label>
                                            <input type="text" class="form-control" min=2 max=100 name="name" id="name" placeholder="Entre su nombre" required>
                                        </div>
                                    </div>
                                    <div class="col-lg-6">
                                        <div class="form-group">
                                            <label for="username">Nombre de Usuario</label>
                                            <input type="text" class="form-control" min="2" max="30" name="username" id="username" placeholder="Entre su nombre de usuario">
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-lg-6">
                                        <div class="form-group">
                                            <label for="password">Contrase&ntilde;a</label>
                                            <input type="password" ng-model="password" id="password" name="password" max="30" min="6" class="form-control" placeholder="Entre su contrase&ntilde;a" required>
                                        </div>

                                    </div>
                                    <div class="col-lg-6">
                                        <div class="form-group">
                                            <label for="confirmPassword">Confirmar Contrase&ntilde;a</label>
                                            <input type="password" ng-model="confirmPassword" class="form-control" min="2" max="100" id="confirmPassword" placeholder="Confirme su contrase&ntilde;a" required>
                                        </div>
                                            <small class="error" style="color: red" ng-show="password !== confirmPassword">Contrase&ntilde;a incompatibles</small>
                                    </div>

                                </div>
                                <div class="row">
                                    <div class="col-lg-6">
                                        <div class="form-group">
                                            <label for="roles">Rol</label>
                                                <select id="roles" class="form-control selectpicker" data-max-options="2" name="theRoles" multiple="multiple" required>
                                                    <option value="ROLE_ADMIN">Administrador</option>
                                                    <option value="ROLE_USER">Usuario</option>
                                                </select>

                                        </div>
                                    </div>
                                </div>

                    <div class="row">
                        <hr>
                        <#if error??>
                            <small class="error" style="color: red;"> ${error}</small>
                        </#if>
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
    <script src="/js/app.js"></script>
    <script src="/js/controllers/usuarioController.js"></script>
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/css/bootstrap-select.min.css">
<!-- Latest compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/js/bootstrap-select.min.js"></script>



</body>

</html>
