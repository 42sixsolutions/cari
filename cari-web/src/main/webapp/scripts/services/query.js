'use strict';

angular.module('cari.services').factory('Query', ['$http', function($http) {
    var postQuery = function(options) {
        return $http.post('api/query/', options);
    };

    return {
        postQuery: postQuery
    };
}]);