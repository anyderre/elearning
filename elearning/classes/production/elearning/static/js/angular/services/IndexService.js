'use strict'; 
app.factory('IndexService',['$http', '$q', function($http, $q){
    return {

    fetchUser: function fetchUser() {
        var deferred = $q.defer();
        console.log("Going to backend")
        $http.get("/api/user/loggedIn").then(
            function (response) {
                deferred.resolve(response.data)
            }, function (errResponse) {
                console.error(errResponse);
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }
}
}]);