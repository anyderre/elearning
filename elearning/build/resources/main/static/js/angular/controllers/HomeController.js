'use strict';

app.controller('HomeController', '$scope','$http',['$window',function ($window,$scope,$http) {
    // $scope.greeting = data;
    var self = this;
    self.user = angular.fromJson($window.localStorage.getItem('user'));
}])