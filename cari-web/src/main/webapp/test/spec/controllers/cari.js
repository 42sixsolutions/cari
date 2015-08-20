'use strict';

describe('routes', function() {
    beforeEach(module('cari'));

    it('should map routes to controllers', function() {
        inject(function($route) {

            expect($route.routes['/'].controller).toBe('CariCtrl');
            expect($route.routes['/'].templateUrl).toBe('partials/home.html');

            // otherwise redirect to
            expect($route.routes[null].redirectTo).toBe('/')
        });
    });    
});

describe('controllers', function() {
    beforeEach(module('cari'));

    var $controller = null;
    beforeEach(inject(function(_$controller_) {
        $controller = _$controller_;
    }));

    describe('CariController', function() {
        var $scope, ctrl;
        beforeEach(inject(function($rootScope) {
            $scope = $rootScope.$new();
            ctrl = $controller("CariCtrl", { $scope: $scope });
        }));

        it('should exist', function() {
            expect(!!ctrl).toBe(true);
        });

        describe('when created', function() {
            it('should define an options property', function() {
                expect($scope.options instanceof Object).toBe(true);
            });
        });
    });
});
