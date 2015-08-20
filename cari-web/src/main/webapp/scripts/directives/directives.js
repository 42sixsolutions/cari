'use strict';

angular.module('cari.directives').
directive('trianglify', ["$window", function($window) {
    var getTrianglifyPattern = function() {
        var pattern = Trianglify({
            height: $(window).height(),
            width: $(window).width(),
            x_colors: ["#9aeedb","#8aead7","#74e3d2","#63ddd0","#5ed9d2","#52cfd7","#4dcbd9","#59cdde","#6ed1e4","#7bd4e8"],
            color_space: 'lab',
            cell_size: 200
        });

        return pattern;
    };

    return {
        restrict: "A",
        link: function(scope, element, attrs) {
            var pattern;

            if (!scope.isDetailsPage) {
                pattern = getTrianglifyPattern();
                $(element[0]).append(pattern.canvas());

                $(window).on("resize.doResize", function() {
                    element.find("canvas").remove();
                    pattern = getTrianglifyPattern();
                    $(element[0]).append(pattern.canvas());
                });

                scope.$on("$destroy", function() {
                    $(window).off("resize.doResize");
                });
            }
        }
    }
}]);
