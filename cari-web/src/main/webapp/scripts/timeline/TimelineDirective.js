'use strict';

angular.module('timeline')
  .directive('timeline', [function () { // TODO - timelineService
      return {
        restrict: 'E',
        scope: {
          defaultStartDate: '=?',
          defaultEndDate: '=?'
        },
        template: '<div></div>',
        replace: true,
        link: function($scope , element) {

          var Months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

          // Initialize slider...
          $(element[0]).dateRangeSlider({
            bounds: {min: new Date(2012, 0, 1), max: new Date(2012, 11, 31, 12, 59, 59)},
            defaultValues: {min: new Date(2012, 1, 10), max: new Date(2012, 4, 22)},
            scales: [{
              next: function(val){
                var next = new Date(val);
                return new Date(next.setMonth(next.getMonth() + 1));
              },
              label: function(val){
                return Months[val.getMonth()];
              }
            }]
          });

        }
      }
  }])