'use strict';

angular.module('cari.services').factory('CariMapService', ['$http', function($http) {
    var mapObject;

    var initMapObject = function(geoJson, callback) {
        // init map object
        var mapOptions = {
            //zoom: 5,
            mapTypeControlOptions: {
                mapTypeIds: [google.maps.MapTypeId.TERRAIN]
            }
        };

        mapObject = new google.maps.Map(document.getElementById('map'), mapOptions);

        // load data
        mapObject.data.addGeoJson(geoJson);

        /* Listeners */
        var infoWindow;

        // double click to zoom into area
        //mapObject.addListener('dblclick', function(event){
        //    if(mapObject.getZoom() < 8) {
        //        mapObject.setZoom(14);
        //    };
        //    console.log(mapObject.getZoom());
        //});

        // wait till map loads then center it
        google.maps.event.addListenerOnce(mapObject, 'idle', function() {
            setCenter();
        });

        // for each marker add tooltip message
        mapObject.data.addListener('mouseover', function(event){
            var content = getTootlTipContent(event.feature);

            infoWindow = new google.maps.InfoWindow(
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

        // remove window info when mouse rolls out
        mapObject.data.addListener('mouseout', function(event) {
            if(angular.isDefined(infoWindow)) {
                infoWindow.close();
            };
        });

        // display detail report about the poi
        mapObject.data.addListener('click', function(event) {
            callback(event.feature.getProperty('events'));
        });


        /* Functions */
        function getTootlTipContent(feature) {
            var content = '<div>' +
                feature.getProperty('events')[0]['SAMPLE_SUBMATRIX'] +
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

    var setCustomStyle = function() {
        mapObject.data.setStyle(function(feature) {
            console.log(feature.getProperty('summary')['icon']);
            return ({icon: feature.getProperty('summary')['icon']});
        });
    };

    var getMapObject = function() {
        return mapObject;
    };

    return {
        initMapObject: initMapObject,
        setCustomStyle: setCustomStyle,
        getMapObject: getMapObject
    };
}]);
