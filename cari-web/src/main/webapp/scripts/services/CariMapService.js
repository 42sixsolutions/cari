'use strict';

angular.module('cari.services').factory('CariMapService', ['$http', function($http) {
    var mapObject;

    var initMapObject = function(geoJson, callback) {
        // init map object
        mapObject = new google.maps.Map(document.getElementById('map'), {
            mapTypeId:  [google.maps.MapTypeId.TERRAIN, 'map_style']
        });

        // load data
        mapObject.data.addGeoJson(geoJson);

        /* Listeners */
        // wait till map loads then center it
        google.maps.event.addListenerOnce(mapObject, 'idle', function() {
            setCenter();
        });

        var infoWindow;

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

        mapObject.data.addListener('mouseout', function(event) {
            if(angular.isDefined(infoWindow)) {
                infoWindow.close();
            };
        });

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
        var styledMap = style();

        mapObject.mapTypes.set('map_style', styledMap);
        mapObject.setMapTypeId('map_style');

        function style() {
            // Create an array of styles.
            var styles = [
                {
                    stylers: [
                        { hue: "#00ffe6" },
                        { saturation: -20 }
                    ]
                },{
                    featureType: "road",
                    elementType: "geometry",
                    stylers: [
                        { lightness: 100 },
                        { visibility: "simplified" }
                    ]
                },{
                    featureType: "road",
                    elementType: "labels",
                    stylers: [
                        { visibility: "off" }
                    ]
                }
            ];

            return new google.maps.StyledMapType(styles,
                {name: "Styled Map"});

        };
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
