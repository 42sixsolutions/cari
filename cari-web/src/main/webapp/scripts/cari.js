'use strict';

angular.module('cari.controllers', []);
angular.module('cari.services', []);
angular.module('cari.directives', []);

// Declare app level module which depends on views, and components
angular.module('cari', [
  'ngRoute',
  'cari.controllers',
  'cari.directives',
  'cari.mockREST',
  'cari.services'
]).
config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/', { templateUrl: 'partials/home.html', controller: 'CariCtrl' });
    $routeProvider.otherwise({redirectTo: '/'});
}]);
