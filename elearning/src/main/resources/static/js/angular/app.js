//'use strict'
var app = angular.module('cabAcademie', ['ngRoute', 'ngCookies','ngStorage', 'ngResource', 'ngMaterial','ngMessages', 'angularUtils.directives.dirPagination']);


// md-card configuration
app.controller('AppCtrl', function($scope) {
    $scope.imagePath = 'img/washedout.png';
});
app.config(['$mdThemingProvider', function($mdThemingProvider) {
    $mdThemingProvider.theme('dark-grey').backgroundPalette('grey').dark();
    $mdThemingProvider.theme('dark-orange').backgroundPalette('orange').dark();
    $mdThemingProvider.theme('dark-purple').backgroundPalette('deep-purple').dark();
    $mdThemingProvider.theme('dark-blue').backgroundPalette('blue').dark();
}]);

app.directive('numbersOnly', function () {
    return {
        require: 'ngModel',
        link: function (scope, element, attr, ngModelCtrl) {
            function fromUser(text) {
                if (text) {
                    var transformedInput = text.replace(/[^0-9]/g, '');

                    if (transformedInput !== text) {
                        ngModelCtrl.$setViewValue(transformedInput);
                        ngModelCtrl.$render();
                    }
                    return transformedInput;
                }
                return undefined;
            }
            ngModelCtrl.$parsers.push(fromUser);
        }
    };
});

app.directive('onlyNumbers', function () {
    return {
        require: 'ngModel',
        link: function (scope, element, attr, ngModelCtrl) {
            function fromUser(text) {
                if (text) {
                    var transformedInput = text.replace(/[^0-9\.]/g, '');

                    if (transformedInput !== text) {
                        ngModelCtrl.$setViewValue(transformedInput);
                        ngModelCtrl.$render();
                    }
                    return transformedInput;
                }
                return undefined;
            }
            ngModelCtrl.$parsers.push(fromUser);
        }
    };
});


app.directive('ngDecimal', function(){
    return {
        restrict: 'A',
        link: function($scope, $element, $attributes){
            var limit = $attributes.ngDecimal;
            function caret(node) {
                if(node.selectionStart) {
                    return node.selectionStart;
                }
                else if(!document.selection) {
                    return 0;
                }
                //node.focus();
                var c		= "\001";
                var sel	= document.selection.createRange();
                var txt	= sel.text;
                var dul	= sel.duplicate();
                var len	= 0;
                try{ dul.moveToElementText(node); }catch(e) { return 0; }
                sel.text	= txt + c;
                len		= (dul.text.indexOf(c));
                sel.moveStart('character',-1);
                sel.text	= "";
                return len;
            }
            $element.bind('keypress', function(event){
                var charCode = (event.which) ? event.which : event.keyCode;
                var elem=document.getElementById($element.attr("id"));
                if (charCode == 45){
                    var caretPosition=caret(elem);
                    if(caretPosition==0){
                        if($element.val().charAt(0)!="-" ){
                            if($element.val() <=limit){
                                $element.val("-"+$element.val());
                            }
                        }
                        if($element.val().indexOf("-")!=-1){
                            event.preventDefault();
                            return false;
                        }
                    }
                    else{
                        event.preventDefault();
                    }
                }
                if (charCode == 46){
                    if($element.val().length>limit-1){
                        event.preventDefault();
                        return false;
                    }
                    if ($element.val().indexOf('.') !=-1){
                        event.preventDefault();
                        return false;
                    }
                    return true;
                }
                if (charCode != 45 && charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57)){
                    event.preventDefault();
                    return false;
                }
                if($element.val().length>limit-1){
                    event.preventDefault();
                    return false;
                }
                return true;
            });
        }
    };
});