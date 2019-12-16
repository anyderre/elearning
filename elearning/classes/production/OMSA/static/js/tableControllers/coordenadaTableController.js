app.controller("coordenadaTableController", function ($http, $scope) {
    $scope.coordenadas = [];
    $scope.pageno = 1;
    $scope.total_count = 0;
    $scope.itemsPerPage= 10;

    $scope.getData = function (pageno, id) {
        $scope.coordenadas=[];

        $scope.start= pageno*$scope.itemsPerPage-$scope.itemsPerPage;
        $http.get("/api/ruta/"+id+"/buscar/coordenada/"+$scope.start+"/"+($scope.start+$scope.itemsPerPage)).then(
            function (response) {

                $scope.coordenadas = response.data;

                $scope.total_count= response.count;
            }, function (response) {
                $scope.coordenadas=[]
            })
    };

});