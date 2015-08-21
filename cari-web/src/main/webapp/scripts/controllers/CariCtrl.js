'use strict';

angular.module('cari.controllers').controller('CariCtrl', ["$scope", "$timeout", "CariMapService", "Query",
        function($scope, $timeout, CariMapService, Query) {

    $scope.lists = {
        "contaminants": [],
        "locations": []
    };

    $scope.chartOptions = {};
    $scope.chartData = [];
    Query.list().then(function(response) {
        $scope.chartOptions = {
            xmin: response.data.firstDate,
            xmax: response.data.lastDate,
            ymin: 0,
            ymax: 100
        };
        $scope.lists.locations = response.data.locationZones;
        $scope.lists.contaminants = [];
        for (var key in response.data.maxFinalResult) {
            $scope.lists.contaminants.push(key);
        }
    });

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
            values.push([new Date(data[i]["SAMPLE_DATE_TIME"]).getTime(), $.inArray(data[i]["ANALYTE_NAME"], contaminants), { "data": data[i]["ANALYTE_NAME"] + data[i]["FINAL_RESULT"] }]);
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
