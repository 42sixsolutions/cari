/* Map Controller */
'use strict';

angular.module('cari.controllers')
    .controller('CariMapCtrl', ['$scope', '$timeout', 'CariMapService', function($scope, $timeout, CariMapService){

        //$scope.initMap = function() {
        $timeout(function(){
            CariMapService.initMapObjct();
        });

        //}
        //$scope.$on('$viewContentLoaded', function(){
        //
        //});

    }]);