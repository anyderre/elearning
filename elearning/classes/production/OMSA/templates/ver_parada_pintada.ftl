<!DOCTYPE html>
<html lang="en" ng-app="angularTable" xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<title>Ver Paradas</title>
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
                        Lista de paradas del corredor <#if nombreCorredor??> ${nombreCorredor}</#if>
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

                    <div class="table-responsive" ng-controller="paradaTableController">
                        <table class="table table-bordered table-hover table-striped"  <#if id_ruta??>
                               ng-init="getData(pageno,${id_ruta})"</#if>>

                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Nombre</th>
                                <th>Latitud</th>
                                <th>Longitud</th>
                                <th></th>

                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-show="paradas.length <= 0">
                                <td colspan="5" style="text-align:center;">Leyendo Nuevos Datos!!</td>
                            </tr>
                            <tr dir-paginate="parada in paradas|itemsPerPage:itemsPerPage" total-items="${size}">
                                <td>{{start+$index+1}}</td>
                                <td>{{parada.nombre}}</td>
                                <td>{{parada.coordenada.latitude}}</td>
                                <td>{{parada.coordenada.longitud}}</td>
                                <td><a href="/parada/editar/${id_ruta}/{{parada.id}}">
                                    <p data-placement="top" data-toggle="tooltip" title="Editar">
                                        <button class="btn btn-primary btn-xs" data-title="Editar" data-toggle="modal"
                                                data-target="#edit"><span class="glyphicon glyphicon-pencil"></span>
                                        </button>
                                    </p>
                                </a></td>

                            </tr>
                            </tbody>
                        </table>
                        <center>
                            <dir-pagination-controls
                                    max-size="10"
                                    direction-links="true"
                                    boundary-links="true"
                                    on-page-change="getData(newPageNumber, ${id_ruta})">
                            </dir-pagination-controls>
                        </center>
                    </div>
                </div>

            </div>
        </div>
        <!-- /.row -->
        <div class="row">
            <div class="col-lg-12">
                <div id="map" style="width: 1100px; height: 400px;"></div>

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
            zoom: 12,
            center: {
                lat: 19.48863754,
                lng: -70.71721584
            },
            mapTypeId: 'terrain'
        });


        $.getJSON("/api/ruta/${id_ruta}", function (data) {
            var coor = [];
            $.each(data["coordenadas"], function (k, v) {

                var obj = {lat: v.latitude, lng: v.longitud}
                coor.push(obj)

            })
            var flightPath = new google.maps.Polyline({
                path: coor,
                geodesic: true,
                strokeColor: '#088A08',
                strokeOpacity: 1.0,
                strokeWeight: 5
            });

            flightPath.setMap(map);

        });


        setMarkers(map);

    }

    function setMarkers(map) {
        // Adds markers to the map.

        // Marker sizes are expressed as a Size of X,Y where the origin of the image
        // (0,0) is located in the top left of the image.

        // Origins, anchor positions and coordinates of the marker increase in the X
        // direction to the right and in the Y direction down.
        var image = {
            url: '/images/paradas.png',
            // This marker is 20 pixels wide by 32 pixels high.
            size: new google.maps.Size(20, 20),
            // The origin for this image is (0, 0).
            origin: new google.maps.Point(0, 0),
            // The anchor for this image is the base of the flagpole at (0, 32).
            anchor: new google.maps.Point(0, 20)
        };
        // Shapes define the clickable region of the icon. The type defines an HTML
        // <area> element 'poly' which traces out a polygon as a series of X,Y points.
        // The final coordinate closes the poly by connecting to the first coordinate.
        var shape = {
            coords: [1, 1, 1, 20, 18, 20, 18, 1],
            type: 'poly'
        };


        $.getJSON("/api/paradas/ruta/${id_ruta}", function (data) {

            $.each(data, function (key, parada) {

                var marker = new google.maps.Marker({
                    position: {
                        lat: parada.coordenada.latitude,
                        lng: parada.coordenada.longitud
                    },
                    map: map,
                    icon: image,
                    shape: shape,
                    title: parada["nombre"],
                    zIndex: 1
                });
            });
        });
    }
</script>
<script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyABtO1OiHaJnWDo29kaUUOm06HBU6GjAUA&callback=initMap">
</script>

<script src="/js/dirPagination.js"></script>
<script src="/js/appTable.js"></script>
<script src="/js/tableControllers/paradaTableController.js"></script>


</body>

</html>
