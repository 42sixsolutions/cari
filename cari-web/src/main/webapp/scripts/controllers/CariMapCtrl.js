/* Map Controller */
'use strict';

angular.module('cari.controllers')
    .controller('CariMapCtrl', ['$scope', 'CariMapService', function($scope, CariMapService){

        $scope.$on('$viewContentLoaded', function(){
                CariMapService.initMapObjct();
        });

    }]);