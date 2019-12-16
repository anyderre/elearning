app.controller("rutaTableControllers", function ($http, $scope) {
    $scope.rutas = [];
    $scope.pageno = 1;
    $scope.total_count = 0;
    $scope.itemsPerPage= 10;

    $scope.isTrue= function (val) {
        return val===true
    };
    $scope.getData = function (pageno) {
        $scope.rutas=[];
        $scope.start= pageno*$scope.itemsPerPage-$scope.itemsPerPage;
        $http.get("/api/ruta/buscar/pagina/"+(pageno-1)+"/item/"+$scope.itemsPerPage).then(
            function (response) {
                $scope.rutas = response.data;
                console.log($scope.rutas)
            }, function (response) {
                $scope.rutas=[]
            })
    };
});