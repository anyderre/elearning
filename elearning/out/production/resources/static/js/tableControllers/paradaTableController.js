app.controller("paradaTableController", function ($http, $scope) {
    $scope.paradas = [];
    $scope.pageno = 1;
    $scope.total_count = 0;
    $scope.itemsPerPage= 10;

    $scope.getData = function (pageno, id) {
        $scope.start= pageno*$scope.itemsPerPage-$scope.itemsPerPage;
        $scope.paradas=[];
                $http.get("/api/paradas/buscar/"+(pageno-1)+"/"+$scope.itemsPerPage+"/ruta/"+id).then(
                    function (response) {

                    $scope.paradas = response.data;

                    $scope.total_count= response.count;
                }, function (response) {
                    $scope.paradas=response.data
                })
    };

});