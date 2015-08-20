'use strict';

angular.module('cari.controllers').controller('CariCtrl', ["$scope", "$timeout", "CariMapService", "Query",
        function($scope, $timeout, CariMapService, Query) {

    $scope.lists = {
        "contaminants": [
            { "name": "Arsenic", "value": "arsenic" },
            { "name": "Cadmium", "value": "cadmium" },
            { "name": "Calcium", "value": "calcium" },
            { "name": "Copper", "value": "copper" },
            { "name": "Lead", "value": "lead" },
            { "name": "Magnesium", "value": "magnesium" },
            { "name": "Mercury", "value": "mercury" },
            { "name": "Phosphorus", "value": "phosphorus" },
            { "name": "Zinc", "value": "zinc" }
        ],
        "locations": [
            { "name": "Other-Ground Water", "value": "other-ground-water" },
            { "name": "River/Stream", "value": "river-stream" },
            { "name": "Well", "value": "well" }
        ]
    };

    $scope.options = {
        "toDate": (new Date()).getTime(),
        "viewType": "latest"
    };

    $scope.data = {};

    var now = new Date().getTime();
    var before = new Date();
    before = before.setDate(before.getDate() - 2);

    $scope.chartOptions = {
        xmin: before,
        xmax: now,
        ymin: 0,
        ymax: 50
    };
    $scope.chartData = [];

    var tmpChartData = [];
    tmpChartData.push({
        data: [[now, 12], [before, 36]],
        points: { show: true, radius: 6, lineWidth: 0, fill: true, fillColor: "rgba(255,0,205,0.5)" },
        lines: { show: false }
    });
    $scope.chartData = tmpChartData;

    $scope.apply = function() {
        Query.postQuery($scope.options).then(function(response) {
            console.log(response.data);
        });
    };

    $timeout(function() {
        Query.postQuery($scope.options).then(function(response) {
            $scope.data = response.data;
            CariMapService.initMapObject(response.data);
        });
    });
}]);
