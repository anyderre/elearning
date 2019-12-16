<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"  xmlns:th="http://www.thymeleaf.org" ng-app="omsaTracker">

<title>Iniciar Sesion</title>
<#include "header.ftl">

<body >

    <div id="wrapper">

        <!-- Navigation -->
        <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <#--<a class="navbar-brand" href="index.ftl">OMSA</a>-->
                <a class="navbar-brand" href="/"><img src="/images/logo.png" alt="logo">
                </a>
            </div>
            <!-- /.navbar-collapse -->
        </nav>

        <div class="row" style="margin-top: 140px" ng-controller="usuarioController">

            <div class="col-lg-offset-2 col-lg-7">
                <div class="panel panel-green">
                    <div class="panel-heading" style="text-align: center">
                        <h2 class="panel-title" style="font-weight: bold; font-size: 20px">Iniciar Sesi&oacute;n</h2>
                    </div>
                    <div class="panel-body" style="background-image: url(../images/OMSA.jpg); background-color: rgba(255,255,255,0.6);background-blend-mode: lighten;  ">

                        <form role="form" action="/login" method="post" class="form-signin name="myForm">
                            <#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->
                            <div class="row">
                                <div class="col-lg-offset-3 col-lg-6">
                                    <div class="form-group">
                                        <label for="username">Usuario</label>
                                        <input type="text" class="form-control" ng-minlength="4" ng-maxlength=30 ng-model="username" name="username" placeholder="Entre su nombre de usuario" id="username" required>
                                        <span style="color:red" ng-show="myForm.username.$dirty && myForm.username.$invalid">
                                              <small ng-show="myForm.username.$error.required">Nombre de usuario requerido.</small>
                                        </span>
                                        <small style="color:red" ng-show="myForm.username.$dirty && myForm.username.$error.minlength">Nombre de usuario corto.</small>
                                        <small style="color:red" ng-show="myForm.username.$dirty && myForm.username.$error.maxlength">Nombre de usuario demasiado largo.</small>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-offset-3 col-lg-6">
                                    <div class="form-group">
                                        <label for="cantidadDeAsientos">Contrase&ntilde;a</label>
                                        <input type="password" class="form-control" ng-model="password" name="password" placeholder="Entre su contrase&ntilde;a" id="password" ng-minlength="6" ng-maxlength=30 required>
                                        <span style="color:red" ng-show="myForm.password.$dirty && myForm.password.$invalid">
                                              <small ng-show="myForm.password.$error.required">Contrase&ntilde;a requerida.</small>
                                        </span>
                                        <small style="color:red" ng-show="myForm.password.$dirty && mysForm.password.$error.minlength">contrase&ntilde;a corto</small>
                                        <small style="color:red" ng-show="myForm.password.$dirty && mysForm.password.$error.maxlength">contrase&ntilde;a demasiado largo</small>
                                    </div>

                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-offset-3 col-lg-3">
                                    <div class="form-group">
                                        <button type="Submit" class="btn btn-success form-control" ng-disabled="myForm.username.$dirty && myForm.username.$invalid || myForm.password.$dirty && myForm.password.$invalid">Entrar</button>
                                    </div>

                                </div>
                            </div>

                        </form>
                        <div class="row">
                            <div class="col-lg-offset-3 col-lg-3">
                                <#if error??>
                                    <p>${error}</p>
                                </#if>
                                <#if msg??>
                                    <p>${msg}</p>
                                </#if>
                            </div>
                        </div>

                    </div>
                </div>
            </div>

        </div>
    </div>
    <!-- /.container-fluid -->



    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="/js/jquery.js">


    </script>

    <!-- Bootstrap Core JavaScript -->
    <script src="/js/bootstrap.min.js"></script>

    <script src="/js/app.js"></script>
    <script src="/js/controllers/usuarioController.js"></script>

</body>

</html>
