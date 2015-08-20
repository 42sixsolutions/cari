'use strict';

angular.module('cari.services').factory('CariMapService', ['$http', function($http) {
    var mapObject;

    var initMapObjct = function() {
        // init map object
        mapObject = new google.maps.Map(document.getElementById('map'), {
            mapTypeId:  google.maps.MapTypeId.TERRAIN
        });

        // load data
        mapObject.data.loadGeoJson('../WEB-INF/classes/json/SampleData-FirstCut-gjson.json');


        /* Listeners */
        // wait till map loads then center it
        google.maps.event.addListenerOnce(mapObject, 'idle', function() {
            setCenter();
        });

        // for each marker add tooltip message
        mapObject.data.addListener('click', function(event){
            var content = getTootlTipContent(event.feature);

            var infoWindow = new google.maps.InfoWindow(
                { content: content }
            );

            infoWindow.setPosition(event.feature.getGeometry().get());
            infoWindow.setOptions({
                pixelOffset: new google.maps.Size(0, -36)
            });

            infoWindow.open(mapObject);

            google.maps.event.addListenerOnce(infoWindow, 'domready', function() {
                infoWindow.close();
                infoWindow.open(mapObject);
            });
        });


        /* Functions */
        function getTootlTipContent(feature) {
            var content = '<div>'+
                feature.getProperty('SAMPLE_SUBMATRIX') +

                '</div>'

            return content;
        };

        // center map around all markers
        // call it after everything loads
        function setCenter() {
            var bounds = new google.maps.LatLngBounds();
            mapObject.data.forEach(function(feature){
                bounds.extend(
                    feature.getGeometry().get()
                );
            });
            
            mapObject.fitBounds(bounds);
        };
    };

    var getMapObject = function() {
        return mapObject;
    };

    return {
        initMapObjct: initMapObjct,
        getMapObject: getMapObject
    };
}]);