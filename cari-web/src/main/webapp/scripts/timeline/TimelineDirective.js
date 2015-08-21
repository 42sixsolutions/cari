'use strict';

/**
 * The `timeline` directive provides a selector to select a point of time.
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
        template: '<div class="timeline"></div>',
        replace: true,
        link: function($scope , element) {

          d3.select(window).on('resize', resize);

          var margin = { top: 200, right: 0, bottom: 200, left: 0 };
          var width = element.width() - margin.left - margin.right;
          var height = 500 - margin.top - margin.bottom;

          var x = d3.time.scale()
              .domain([timelineConfig.startDate, timelineConfig.endDate])
              .range([0, width])
              .clamp(true);

          var brush = d3.svg.brush()
              .x(x)
              .extent([0, 0])
              .on('brush', brushed);

          var svg = d3.select(element[0]).append('svg')
              .attr('width', width + margin.left + margin.right)
              .attr('height', height + margin.top + margin.bottom)
            .append('g')
              .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');

          svg.append('rect')
              .attr('class', 'grid-background')
              .attr('width', width)
              .attr('height', height);

          svg.append('g')
              .attr('class', 'x grid')
              .attr('transform', 'translate(0,' + height + ')')
              .call(d3.svg.axis()
                  .scale(x)
                  .orient('bottom')
                  .ticks(d3.time.months)
                  .tickSize(-height)
                  .tickFormat(''))
            .selectAll('.tick')
              .classed('minor', function(d) { return d.getMonth(); });

          svg.append('g')
              .attr('class', 'x axis')
              .attr('transform', 'translate(0,' + height + ')')
              .call(d3.svg.axis()
                .scale(x)
                .orient('bottom')
                .ticks(d3.time.months)
                .tickFormat(d3.time.format("%Y"))
                .tickPadding(0))
            .selectAll('text')
              .attr('x', 6)
              .style('text-anchor', null);

          var slider = svg.append('g')
              .attr('class', 'brush')
              .call(brush);

          slider.selectAll('.extent,.resize')
            .remove();

          slider.selectAll('rect')
              .attr('height', height);

          var handle = slider.append('rect')
            .attr('class', 'handle')
            .attr('transform', 'translate(0, -4)')
            .attr('height', height + 8)
            .attr('width', 8);

          function brushed() {
            var value = brush.extent()[0];

            if (d3.event.sourceEvent) { // not a programmatic event
              value = x.invert(d3.mouse(this)[0]);
              brush.extent([value, value]);
            }

            console.log(value);
            $rootScope.$broadcast('timelineChanged', value);
            handle.attr("x", x(value) - 4);
          }

          function resize() {
            console.log('Resize...');
          }
        }
      }
  }])