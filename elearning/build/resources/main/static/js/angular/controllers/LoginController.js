/**
 * Created by Dany on 08/07/2018.
 */
'use strict';

app.controller('LoginController', function ($cookies, $http, $location, $rootScope, $q, $resource, $scope, Cookies, Login) {
    var self = this;
    self.greetings = {
        open: {
            getResult: '',
            postValue: 'some value'
        },
        secure: {
            getResult: '',
            postValue: 'some secure value'
        }
    };

    self.credentials = {
        username: '',
        password: ''
    };

    var openResources = $resource('http://localhost:3000/rest/open', {}, {
        get: {method: 'GET', cache: false, isArray: false},
        post: {method: 'POST', isArray: false}
    });

    self.getOpenGreetings = function() {
        self.greetings.open.getResult = '';

        openResources.get().$promise.then(function (response) {
            console.log('GET /rest/open returned: ', response);
            self.greetings.open.getResult = response.greetings;
        });
    };

    self.postOpenGreetings = function() {
        openResources.post({greetings: self.greetings.open.postValue}).$promise.then(function(response) {
            console.log('POST /rest/open returned: ', response);
            console.info('You might want to check the server logs to see that the POST has been handled!');
        });
    };

    self.login = function () {
        Login.login(self.credentials.username, self.credentials.password, function (data, status, headers, config) {
            // Success handler
            console.info('The user has been successfully logged in! ', data, status, headers, config);
            Login.getUsers( function () {
                console.log($rootScope)
                if ($rootScope.authenticated) {
                    $location.path("/home");
                    self.error = false;
                } else {
                    $location.path("/login");
                    self.error = true;
                }
            });

        }, function(data, status, headers, config) {
            // Failure handler
            console.error('Something went wrong while trying to login... ', data, status, headers, config);
        });
    };

    self.logout = function() {
        Login.logout(function (data, status, headers, config) {
            // Success handler
            self.credentials = {username: '', password: ''};
            delete $cookies['JSESSIONID'];
            console.info('The user has been logged out!');

            $location.url('/');

        }, function(data, status, headers, config) {
            // Failure handler
            console.error('Something went wrong while trying to logout... ', data, status, headers, config);
        });
    };

    var secureResources = function (headers) {
        if (headers !== undefined) {
            return $resource('http://localhost:3000/rest/secure', {}, {
                post: {method: 'POST', headers: headers, isArray: false}
            });
        } else {
            return $resource('http://localhost:3000/rest/secure', {}, {
                get: {method: 'GET', cache: false, isArray: false},
                options: {method: 'OPTIONS', cache: false}
            });
        }
    };

    self.getSecureGreetings = function() {
        self.greetings.secure.getResult = '';

        secureResources().get().$promise.then(function (response) {
            console.log('GET /rest/secure returned: ', response);
            self.greetings.secure.getResult = response.greetings;

        }).catch(function(response) {
            handleError(response);
        });
    };

    // self.postSecureGreetings = function () {
    //     Csrf.addResourcesCsrfToHeaders(secureResources().options, $http.defaults.headers.post).then(function (headers) {
    //         secureResources(headers).post({greetings: self.greetings.secure.postValue}).$promise.then(function (response) {
    //             console.log('POST /rest/secure returned: ', response);
    //             console.info('You might want to check the server logs to see that the POST has been handled!');
    //
    //         }).catch(function(response) {
    //             handleError(response);
    //         });
    //     });
    // };

    var handleError = function(response) {

        if (response.status === 401) {
            console.error('You need to login first!');

        } else {
            console.error('Something went wrong...', response);
        }
    };
});
