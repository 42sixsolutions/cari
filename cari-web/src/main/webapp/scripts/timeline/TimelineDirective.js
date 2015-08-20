'use strict';

angular.module('timeline')
  .directive('timeline', [
           'timelineConfig',
  function (timelineConfig) {
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
          });

        }
      }
  }])