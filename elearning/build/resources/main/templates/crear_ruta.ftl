<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<title>Crear Ruta</title>
<#include "header.ftl">

<body>
    <div id="wrapper">
    <#include "nav.ftl">

        <div id="page-wrapper" >

            <div class="container-fluid">

                <!-- Page Heading -->
                <div class="row">
                    <div class="col-lg-12">
                        <h3 class="page-header">
                            Crear una nueva ruta
                        </h3>
                        <ol class="breadcrumb">
                            <li>
                                <i class="fa fa-dashboard"></i> <a href="/home">Home</a>
                            </li>

                            <li class="active">
                                <i class="fa fa-edit"></i> Crear Ruta
                            </li>
                        </ol>
                    </div>
                </div>

                <form role="form"  action="#" th:action="@{/ruta/crear}" th:object="${ruta}" method="post">
                    <div class="row">
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label for="nombreCorredor">Nombre Corredor</label>
                                <input type="text" class="form-control" placeholder="Nombre del corredor" min="2" max="100" name="nombreCorredor" id="nombreCorredor" required>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label for="distanciaTotal">Distancia Total</label>
                                <div class="input-group">
                                    <input type="number" id="distanciaTotal" step="0.0001" placeholder="Entre la distancia total" min="0.0" name="distanciaTotal" class="form-control">
                                    <span class="input-group-addon">km</span>
                                </div>
                            </div>

                        </div>
                    </div>
                    <div class="row">

                        <div class="col-lg-6">
                            <div class="form-group">
                                <label for="ciudad">Ciudad</label>
                                <input type="text" class="form-control" name="ciudad" placeholder="Entre la ciudad" min="2" max="100" id="ciudad">
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label for="direccionSubida">Sentido Ruta</label>
                                <br>
                                <label class="radio-inline">
                                    <input type="radio" name="esDireccionSubida" id="direccionSubida" value=true checked>Subida
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="esDireccionSubida" id="direccionBajada" value=false >Bajada
                                </label>
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

</body>

</html>
