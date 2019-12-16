angular.module("omsaTracker").controller("paradaController", function ($scope,$http) {
     $scope.getParadas= function(id) {

        $http.get("/api/paradas/ruta/"+id).then(function (response) {
            $scope.paradas= response.data;
            console.log($scope.paradas[0]["id"])
        }, function (response) {
            $scope.paradas=[];
            console.log("there was an error", response.data)
        })
    }



});