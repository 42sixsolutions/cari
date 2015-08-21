'use strict';

angular.module('cari.controllers').controller('CariCtrl', ["$scope", "$timeout", "CariMapService", "Query",
        function($scope, $timeout, CariMapService, Query) {

    $scope.list = {};
    Query.list().then(function(response) {
        $scope.list = response.data;
    });

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

    $scope.selected = {};

    $scope.chartOptions = {
        xmin: $scope.list.firstDate,
        xmax: $scope.list.lastDate,
        ymin: 0,
        ymax: 50
    };
    $scope.chartData = [];

    var tmpChartData = [];
    tmpChartData.push({
        data: [[$scope.chartOptions.xmin, 12], [$scope.chartOptions.xmax, 36]],
        points: { show: true, radius: 6, lineWidth: 0, fill: true, fillColor: "rgba(255,0,205,0.5)" },
        lines: { show: false }
    });
    $scope.chartData = tmpChartData;

    $scope.updateReport = function(data) {
        $scope.selected.data = data;
        $scope.$apply();
    };

    $scope.apply = function() {
        Query.postQuery($scope.options).then(function(response) {
            CariMapService.initMapObject(response.data, $scope.updateReport);
        });
    };

    $timeout(function() {
        $scope.apply();
    });
}]);
