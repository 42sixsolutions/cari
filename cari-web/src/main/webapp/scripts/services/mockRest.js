
angular.module('cari.mockREST', ['ngMockE2E']).run(function($injector) {
    var $httpBackend = $injector.get('$httpBackend');

    $httpBackend.whenGET(/partials/).passThrough();

    /**
     * Query Info
     */
    $httpBackend.whenPOST(/api\/query/).respond(function(method, url, data, headers) {
        return [200, { "response": "values" }, {}];
    });
});
