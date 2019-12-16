<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<title>Ver comentarios</title>
<#include "header.ftl">
<link rel="stylesheet" href="/css/style.css"/>
<style>
    .fa-star{
        color: lightgray;
    }
    .checked {
        color: orange;
    }
    .timeline-entry:hover{
        transform: scale(1.03, 1.03);
        box-shadow: 10px 10px 5px lightgray;
    }
</style>
<body>

<div id="wrapper">
<#include "nav.ftl">

    <div id="page-wrapper">

        <div class="container-fluid">

            <!-- Page Heading -->
            <div class="row">
                <div class="col-lg-12">
                    <h3 class="page-header">
                        Ver Comentarios
                    </h3>
                    <ol class="breadcrumb">
                        <li>
                            <i class="fa fa-dashboard"></i> <a href="/home">Home</a>
                        </li>
                        <li class="active">
                            <i class="fa fa-comments"></i> Comentarios
                        </li>
                    </ol>
                </div>
            </div>
            <!-- /.row -->

            <div class="row">
                <div class="col-md-12">
                    <div class="panel panel-success">
                        <div class="panel-heading">
                            <h1> Comentarios</h1>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="panel panel-success">
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><i class="fa fa-comments"></i>Comentario de
                                                usuarios</h3>
                                        </div>
                                        <div class="panel-body" id="comentarios"
                                             style="max-height: 580px; overflow-y: scroll;">
                                            <div class="timeline-centered">

                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="panel panel-success">
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><i class="fa fa-area-chart"></i>Grafos de
                                                Comentarios</h3>
                                        </div>
                                        <div class="panel-body">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <canvas id="ChartCircle" width="100" height="100"></canvas>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>

                </div>

            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- /#page-wrapper -->
    </div>
    <!-- /#wrapper -->
</div>
<!-- jQuery -->
<script src="/js/jquery.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="/js/bootstrap.min.js"></script>

<scripttype
= "text/javascript" >
$('.selectpicker').selectpicker();
</script>
<link rel = "stylesheet" href = "https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/css/bootstrap-select.min.css" >
        <!-- Latest compiled and minified JavaScript -->
        <script src = "https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/js/bootstrap-select.min.js" ></script>

<script src="/js/Chart.js" type="text/javascript"></script>
<script type="text/javascript">
    var ctxCircle = document.getElementById("ChartCircle");

    var chartCircle = null;

    var elementsRating = [];
    var ratings={}
    $.get("/api/rating/comentarios/", function (data, status) {

        var label = [], arr = [];
        var pieOptions = [{
            segmentShowStroke: false,
            animateScale: true
        }];

        data.forEach(function (doc) {

            arr.push(doc.numeroDePuntuacion);
            var counts = {};
            for (var i = 0; i < arr.length; i++) {
                var num = arr[i];
                counts[num] = counts[num] ? counts[num] + 1 : 1;
            }
            elementsRating.push(counts[1], counts[2], counts[3], counts[4], counts[5]);
            label = [1, 2, 3, 4, 5];

            chartCircle = new Chart(ctxCircle, {
                type: 'pie',
                data: data = {
                    datasets: [{
                        data: [counts[1], counts[2], counts[3], counts[4], counts[5]],
                        backgroundColor: ['#878BB6', '#4ACAB4', '#FF8153', '#FFEA88', '#FA0A88']
                    }],

                    labels: [
                        '1 Star',
                        '2 Stars',
                        '3 Stars',
                        '4 Stars',
                        '5 Stars'
                    ]
                },
                options: pieOptions
            });

        });
    });
    var lastScrollTop = 0;
    var st = null;
    var page = 0;

    var cont =0;
    $.get("/api/rating/comentarios/" + page + "/" + 7 + "/", function (data, status) {

        data.forEach(function (doc) {
            ratings["demo"+cont]=doc["numeroDePuntuacion"];
            var fecha = new Date(doc["fechaPublicada"] * 1000);
            var fechaRes = fecha.toLocaleDateString();
            var tiempoRes = fecha.toLocaleTimeString();
            var containtString = '<article class="timeline-entry">' +

                    '<div class="timeline-entry-inner">' +

                    '<div class="timeline-icon bg-success">' +
                    '<i class="entypo-feather" style="font-size:9px;">' +
                    '</i>' +
                    ' </div>' +
                    '<div class="timeline-label">' +
                    '<h2>Anonimo</h2>' +
                    '<hr>' +
                    '<p>' + doc["comentario"] + '</p>' +
                    '<hr>' +
                    '<div class="row"><div class="col-md-12">'+
                    '<div class="col-md-8" style="font-style: italic"> '+fechaRes+' </div>'+
                    '<div class="col-md-4" id="demo'+cont+'"></div>' +
                    '</div></div>'+
            '</div>' +
            '</div>' +
            '</article>';

            $(".timeline-centered").append(containtString)

            if(doc["numeroDePuntuacion"]===1){
                        var text =
                        '<span class="fa fa-star checked"></span>'+
                        '<span class="fa fa-star"></span>'+
                        '<span class="fa fa-star"></span>'+
                        '<span class="fa fa-star"></span>'+
                        '<span class="fa fa-star"></span>'
                $('#demo'+cont).append(text);
            }
            else if(doc["numeroDePuntuacion"]===2){
            var text =
                    '<span class="fa fa-star checked"></span>'+
                    '<span class="fa fa-star checked"></span>'+
                    '<span class="fa fa-star"></span>'+
                    '<span class="fa fa-star"></span>'+
                    '<span class="fa fa-star"></span>'
                $('#demo'+cont).append(text);
             }
            else if(doc["numeroDePuntuacion"]===3)
             {
                 var text =
                         '<span class="fa fa-star checked"></span>'+
                         '<span class="fa fa-star checked"></span>'+
                         '<span class="fa fa-star checked"></span>'+
                         '<span class="fa fa-star"></span>'+
                         '<span class="fa fa-star"></span>'
                 $('#demo'+cont).append(text);
             }
            else if(doc["numeroDePuntuacion"]===4){
                var text =
                        '<span class="fa fa-star checked"></span>'+
                        '<span class="fa fa-star checked"></span>'+
                        '<span class="fa fa-star checked"></span>'+
                        '<span class="fa fa-star checked"></span>'+
                        '<span class="fa fa-star"></span>';
                        $('#demo'+cont).append(text);
            }
            else if (doc["numeroDePuntuacion"]===5){
                    var text =
                            '<span class="fa fa-star checked"></span>'+
                            '<span class="fa fa-star checked"></span>'+
                            '<span class="fa fa-star checked"></span>'+
                            '<span class="fa fa-star checked"></span>'+
                            '<span class="fa fa-star checked"></span>';
                $('#demo'+cont).append(text);

            }

            cont+=1;
        })
    });

    var page1 = 0;
    $('#comentarios').on('scroll', function (e) {
        st = $(this).scrollTop();

        if (st >= lastScrollTop) {

            if (st % 100 === 0) {
                page1 = page1 + 1;

                $.get("/api/rating/comentarios/" + page1 + "/" + 7 + "/", function (data, status) {
                    if (data.length === 0) {
                        var fin = '<article class="timeline-entry begin"><div class="timeline-entry-inner">' +
                                '<div class="timeline-icon"style="-webkit-transform: rotate(-90deg); -moz-transform: rotate(-90deg);">' +
                                '<i class="entypo-flight"></i> + </div>' +
                                '</div>' +
                                ' </div></article>';
                        if ($('.begin').length) {
                            $(".begin").replaceWith(fin);
                        } else {
                            $(".timeline-centered").append(fin);
                        }

                    }
                    data.forEach(function (doc) {
                        var fecha = new Date(doc["fechaPublicada"] * 1000)
                        var fechaRes = fecha.toLocaleDateString();
                        var tiempoRes = fecha.toLocaleTimeString();

                        var containtString = '<article class="timeline-entry">' +

                                '<div class="timeline-entry-inner">' +

                                '<div class="timeline-icon bg-success">' +
                                '<i class="entypo-feather" style="font-size:9px;">' +

                                '</i>' +
                                ' </div>' +

                                '<div class="timeline-label">' +
                                '<h2>Anonimo</h2>' +
                                '<hr>' +
                                '<p>' + doc["comentario"] + '</p>' +
                                '<hr>' +
                                '<div class="row"><div class="col-md-12"><div class="col-md-8" style="font-style: italic">' + fechaRes
                                +'</div><div class="col-md-4" id="demo'+cont+'">' +
                                '</div></div></div>'+
                                '</div>' +
                                '</div>' +
                                '</article>';

                        $(".timeline-centered").append(containtString)

                        if(doc["numeroDePuntuacion"]===1){
                            var text =
                                    '<span class="fa fa-star checked"></span>'+
                                    '<span class="fa fa-star"></span>'+
                                    '<span class="fa fa-star"></span>'+
                                    '<span class="fa fa-star"></span>'+
                                    '<span class="fa fa-star"></span>'
                            $('#demo'+cont).append(text);
                        }
                        else if(doc["numeroDePuntuacion"]===2){
                        var text =
                                '<span class="fa fa-star checked"></span>'+
                                '<span class="fa fa-star checked"></span>'+
                                '<span class="fa fa-star"></span>'+
                                '<span class="fa fa-star"></span>'+
                                '<span class="fa fa-star"></span>'
                        $('#demo'+cont).append(text);
                    }
                    else if(doc["numeroDePuntuacion"]===3)
                    {
                    var text =
                            '<span class="fa fa-star checked"></span>'+
                            '<span class="fa fa-star checked"></span>'+
                            '<span class="fa fa-star checked"></span>'+
                            '<span class="fa fa-star"></span>'+
                            '<span class="fa fa-star"></span>'
                    $('#demo'+cont).append(text);
                     }
                   else if(doc["numeroDePuntuacion"]===4){
                    var text =
                            '<span class="fa fa-star checked"></span>'+
                            '<span class="fa fa-star checked"></span>'+
                            '<span class="fa fa-star checked"></span>'+
                            '<span class="fa fa-star checked"></span>'+
                            '<span class="fa fa-star"></span>'
                    $('#demo'+cont).append(text);
                }
                else if (doc["numeroDePuntuacion"]===5){
        var text =
                '<span class="fa fa-star checked"></span>'+
                '<span class="fa fa-star checked"></span>'+
                '<span class="fa fa-star checked"></span>'+
                '<span class="fa fa-star checked"></span>'+
                '<span class="fa fa-star checked"></span>'
        $('#demo'+cont).append(text);

    }

    cont+=1;

                    });
                });
            }
        }
        lastScrollTop = st;
    });


</script>

</body>

</html>