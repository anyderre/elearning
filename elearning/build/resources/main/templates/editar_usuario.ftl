<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<title>Editar Usuario</title>
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
                        Usuarios
                    </h3>
                    <ol class="breadcrumb">
                        <li>
                            <i class="fa fa-dashboard"></i> <a href="/home">Home</a>
                        </li>
                        <li>
                            <i class="fa fa-table"></i> <a href="/zonaAdmin/usuarios">Tabla de Usuarios</a>
                        </li>
                        <li class="active">
                            <i class="fa fa-edit"></i> Modificar Usuario
                        </li>
                    </ol>
                </div>
            </div>

            <form role="form" action="/zonaAdmin/editar" method="post">
                <h2>Agregar Nuevo Usuario</h2>
                <hr>
                <div class="row">
                    <div class="col-lg-6">
                        <div class="form-group">
                            <label for="name">Nombre</label>
                            <input type="text" class="form-control" min="2" max="100" name="name" id="name"
                                   placeholder="Entre su nombre" value="${user.name}" required>
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <div class="form-group">
                            <label for="username">Nombre de Usuario</label>
                            <input type="text" class="form-control" min="2" max="30" name="username" id="username"
                                   placeholder="Entre su nombre de usuario" value="${user.username}" required>
                        </div>
                    </div>
                </div>
                <input type="hidden" name ="id" value="${user.id}">
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
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/css/bootstrap-select.min.css">
<!-- Latest compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/js/bootstrap-select.min.js"></script>

</body>

</html>
