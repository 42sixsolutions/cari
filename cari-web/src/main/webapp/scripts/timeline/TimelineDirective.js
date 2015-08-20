'use strict';

/**
 * The `timeline` directive is a timeline that allows a windowed selection
 * of time. It is basically an Angular directive wrapper for the JQRangeSlider
 * library (http://ghusse.github.io/jQRangeSlider/index.html).
 *
 * When the timeline window changes, the `timelineChanged` event is
 * broadcast on the $rootScope, this contains all the window's start and end
 * times.
 */

angular.module('timeline')
  .directive('timeline', [
           '$rootScope', 'timelineConfig',
  function ($rootScope,   timelineConfig) {
      return {
        restrict: 'E',
        template: '<div></div>',
        replace: true,
        link: function($scope , element) {

          var Months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

          // Initialize slider...
          $(element[0]).dateRangeSlider({
            bounds: {
              min: timelineConfig.minBoundDate,
              max: timelineConfig.maxBoundDate
            },
            defaultValues: {
              min: timelineConfig.minDefaultDate,
              max: timelineConfig.maxDefaultDate
            },
            scales: [{
              next: function(val){
                var next = new Date(val);
                return new Date(next.setMonth(next.getMonth() + 1));
              },
              label: function(val){
                return Months[val.getMonth()] + ' ' + val.getFullYear();
              }
            }]
          })
          .bind('valuesChanged', function(e, data) {
            $rootScope.$broadcast('timelineChanged', data);
          });

        }
      }
  }])