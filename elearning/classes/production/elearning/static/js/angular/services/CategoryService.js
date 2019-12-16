'use strict'; 
app.factory('CategoryService',['$http', '$q', function($http, $q){
    return {
        fetchAllCategories: function () { // fetching all Categories from backend
            var deferred = $q.defer();
            $http.get('/api/category/')
                .then(function (response) {
                        deferred.resolve(response.data);
                    }, function (error) {
                        console.error(error);
                        deferred.reject(error);
                    }
                );
            return deferred.promise;
        },



        CreateCategoryAndTopics: function createCategoryAndTopics(topics) {
        var deferred = $q.defer();
        $http.post("/api/category/topic", topics)
            .then(
                function (response) {
                    deferred.resolve(response.data)
                }, function (errResponse) {
                    console.error(errResponse);
                    deferred.reject(errResponse);
                });
        return deferred.promise;
    },

        updateCategoryAndTopics: function updateCategoryAndTopics(topics) {
        var deferred = $q.defer();
        $http.put("/api/category/" + id, category).then(
            function (response) {
                deferred.resolve(response.data)
            }, function (errResponse) {
                console.error(errResponse);
                deferred.reject(errResponse);
            });
        return deferred.promise;
    },

    deleteCategory: function deleteCategory(id) {
        var deferred = $q.defer();
        $http.delete("/api/category/" + id)
            .then(function (response) {
                deferred.resolve(response.data)
            }, function (errResponse) {
                console.error(errResponse);
                deferred.reject(errResponse);
            });
        return deferred.promise;
    },
    fetchTopics: function fetchTopics(id) {
        var deferred = $q.defer();
        $http.get("/api/category/topics/" + id)
            .then(function (response) {
                deferred.resolve(response.data)
            }, function (errResponse) {
                console.error(errResponse);
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }
}
}]);