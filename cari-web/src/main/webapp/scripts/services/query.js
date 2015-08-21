'use strict';

angular.module('cari.services').factory('Query', ['$http', function($http) {
    var postQuery = function(options) {
        return $http.post('api/query/', options);
    };

    var list = function() {
        return $http.get('api/list/');
    };

    return {
        postQuery: postQuery,
        list: list
    };
}]);