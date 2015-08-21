'use strict';

angular.module('cari.controllers').controller('CariCtrl', ["$scope", "$timeout", "CariMapService", "Query",
        function($scope, $timeout, CariMapService, Query) {

    $scope.chartOptions = {};
    $scope.chartData = [];
    Query.list().then(function(response) {
        $scope.chartOptions = {
            xmin: response.data.firstDate,
            xmax: response.data.lastDate,
            ymin: 0,
            ymax: 50
        };
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

    $scope.update = function(data) {
        $scope.selected.data = data;
        console.log(data);

        var values = [];
        for (var i = 0; i < data.length; i++) {
            values.push([new Date(data[i]["SAMPLE_DATE_TIME"]).getTime(), data[i]["FINAL_RESULT"]]);
        }

        var tmpChartData = [];
        tmpChartData.push({
            data: values,
            points: { show: true, radius: 6, lineWidth: 0, fill: true, fillColor: "rgba(255,0,205,0.5)" },
            lines: { show: false }
        });
        $scope.chartData = tmpChartData;
        $scope.$apply();
    };

    $scope.apply = function() {
        Query.postQuery($scope.options).then(function(response) {
            CariMapService.initMapObject(response.data, $scope.update);
            CariMapService.setCustomStyle();
        });
    };

    $timeout(function() {
        $scope.apply();
    });
}]);
