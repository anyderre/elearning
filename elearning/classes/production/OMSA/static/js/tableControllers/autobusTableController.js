app.controller("autobusTableController", function ($http, $scope) {
    $scope.autobuses = [];
    $scope.pageno = 1;
    $scope.total_count = 0;
    $scope.itemsPerPage= 10;

    $scope.isTrue = function (val){
        return val===true
    }

    $scope.isNull = function (val){
        return val===null
    }
    $scope.getData = function (pageno, id_ruta) {
        $scope.autobuses=[];
        $scope.start= pageno*$scope.itemsPerPage-$scope.itemsPerPage;
        if(id_ruta===null){
            id_ruta=1;
        }
        $http.get("/api/autobus/buscar/ruta/size/"+id_ruta).then(function (response) {
            $scope.total_count=response.data
        });

        $http.get("/api/autobus/buscar/"+(pageno-1)+"/"+$scope.itemsPerPage+"/corredor/"+id_ruta).then(
            function (response) {
                $scope.autobuses = response.data;


            }, function (response) {
                $scope.autobuses=response.data
            })
    };

});