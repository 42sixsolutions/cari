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
            ymax: 100
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

        var contaminants = [];
        for (var i = 0; i < data.length; i++) {
            var contaminant = data[i]["ANALYTE_NAME"];
            if ($.inArray(contaminant, contaminants) === -1) {
                contaminants.push(contaminant);
            }
        }
        $scope.chartOptions.ymax = contaminants.length;

        var values = [];
        for (var i = 0; i < data.length; i++) {
            var pointDate = new Date(data[i]["SAMPLE_DATE_TIME"]).getTime();
            var pointContaminant = data[i]["ANALYTE_NAME"];
            var shouldAddPoint = true;
            // If the same contaminant was tested more than once at the same date/time, take the highest contamination
            for (var j = i + 1; j < data.length; j++) {
                if (i === j) {
                    continue;
                }
                if (pointContaminant === data[j]["ANALYTE_NAME"] && data[i]["FINAL_RESULT"] <= data[j]["FINAL_RESULT"]) {
                    shouldAddPoint = false;
                    break;
                }
            }
            if (shouldAddPoint) {
                var dataPoint = [pointDate, $.inArray(pointContaminant, contaminants), { "data": data[i]["ANALYTE_NAME"], "value": data[i]["FINAL_RESULT"] }];
                var contaminationLevel = Math.floor((data[i]["weightedContaminationValue"] + 0.1) * 10);
                for (var j = 0; j < contaminationLevel; j++) {
                    values.push(dataPoint);
                }
            }
        }

        var index = 0;
        var tmpChartData = [];
        tmpChartData.push({
            data: values,
            points: { show: true, radius: 6, lineWidth: 0, fill: true, fillColor: "rgba(255,0,205,0.1)" },
            lines: { show: false }
        });
        $scope.chartData = tmpChartData;
        $scope.$apply();
    };

    $scope.apply = function() {
        Query.postQuery($scope.options).then(function(response) {
            CariMapService.initMapObject(response.data, $scope.update);
        });
    };

    $timeout(function() {
        $scope.apply();
    });
}]);
