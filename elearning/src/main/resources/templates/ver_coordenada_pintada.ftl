<!DOCTYPE html>
<html lang="en" ng-app="angularTable" xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<title>Ver Coordenadas</title>
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
                        Lista de coordenadas del corredor <#if nombreCorredor??> ${nombreCorredor}</#if>
                    </h3>
                    <ol class="breadcrumb">
                        <li>
                            <i class="fa fa-dashboard"></i> <a href="/home">Home</a>
                        </li>
                        <li>
                            <i class="fa fa-table"></i> <a href="/ruta/">Tabla de Corredores</a>
                        </li>
                        <li class="active">
                            <i class="fa fa-table"></i> Tabla de Cooredenadas
                        </li>
                    </ol>


                </div>
            </div>
            <!-- /.row -->

            <div class="row">
                <div class="col-lg-12">

                    <div class="table-responsive" ng-controller="coordenadaTableController">
                        <table class="table table-bordered table-hover table-striped"  <#if id_ruta??>  ng-init="getData(pageno,${id_ruta})"</#if>>

                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Latitud</th>
                                <th>Longitud</th>
                                <#--<th>id</th>-->
                                <th>&nbsp</th>
                                <th>&nbsp</th>

                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-show="coordenadas.length <= 0"><td colspan="5" style="text-align:center;">Leyendo Nuevos Datos!!</td></tr>
                            <tr dir-paginate="coordenada in coordenadas|itemsPerPage:itemsPerPage" total-items="${size}">
                                <td>{{start+$index+1}}</td>
                                <td>{{coordenada.latitude}}</td>
                                <td>{{coordenada.longitud}}</td>
                                <#--<td>{{coordenada.id}}</td>-->
                                <td> <a href="/coordenada/editar/${id_ruta}/{{coordenada.id}}">
                                    <p data-placement="top" data-toggle="tooltip" title="Editar"><button class="btn btn-primary btn-xs" data-title="Editar" data-toggle="modal" data-target="#edit"><span class="glyphicon glyphicon-pencil"></span></button></p>
                                </a></td>
                                <td>
                                    <a href="/coordenada/eliminar/${id_ruta}/{{coordenada.id}}" onclick="return confirm('Estas seguro que quieres eliminar esta coordenada?');">
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
                                on-page-change="getData(newPageNumber, ${id_ruta})" >
                        </dir-pagination-controls></center>
                    </div>
                </div>
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel-body" style="height: 100%; width:100%;">
                        <div id="map" style="width: 100%; height: 600px;"></div>
                    </div>
                </div>

            </div>
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


<script>
    // This example creates a 2-pixel-wide red polyline showing the path of William
    // Kingsford Smith's first trans-Pacific flight between Oakland, CA, and
    // Brisbane, Australia.

    function initMap() {
        var map = new google.maps.Map(document.getElementById('map'), {
            zoom: 13,
            center: {
                lat: 19.48863754,
                lng: -70.71721584
            },
            mapTypeId: 'terrain'
        });


        $.getJSON( "/api/ruta/${id_ruta}", function( data ) {
            var coor = [];
            $.each(data["coordenadas"], function (k, v) {
                console.log(data)
                var obj ={lat:v.latitude, lng:v.longitud}
                coor.push(obj)

            });
            var flightPath = new google.maps.Polyline({
                path: coor,
                geodesic: true,
                strokeColor: '#088A08',
                strokeOpacity: 1.0,
                strokeWeight: 5
            });

            flightPath.setMap(map);

        });



    }
</script>
<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyABtO1OiHaJnWDo29kaUUOm06HBU6GjAUA&callback=initMap">
</script>

<script src="/js/dirPagination.js"></script>
<script src="/js/appTable.js"></script>
<script src="/js/tableControllers/coordenadaTableController.js"></script>



</body>

</html>
