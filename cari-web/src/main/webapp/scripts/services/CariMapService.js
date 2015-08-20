'use strict';

angular.module('cari.services').factory('CariMapService', ['$http', function($http) {

    var mapObject;

    var initMapObjct = function() {
        var uluru = {lat: -25.363, lng: 131.044};

        mapObject = new google.maps.Map(document.getElementById('map'), {
            zoom: 4,
            center: uluru
        });

        mapObject.data.loadGeoJson('../WEB-INF/classes/json/sampleGeoJSON.json');

        var contentString = 'Hello World Content';

        var infowindow = new google.maps.InfoWindow({
            content: contentString
        });

        var marker = new google.maps.Marker({
            position: uluru,
            setMap: map,
            title: 'Uluru (Ayers Rock)'
        });

        marker.addListener('click', function() {
            infowindow.open(map, marker);

            mapObject.data.forEach(function(feature){
                console.log('here');
                console.log(feature);
            });

        });
    };

    var getMapObject = function() {
        return mapObject;
    };

    return {
        initMapObjct: initMapObjct,
        getMapObject: getMapObject
    };
}]);