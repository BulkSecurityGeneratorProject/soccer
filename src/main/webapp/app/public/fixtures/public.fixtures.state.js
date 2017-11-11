(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('public.fixtures', {
            parent: 'public',
            url: '/public/fixtures',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/public/fixtures/fixtures.html',
                    controller: 'PublicFixturesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                
            }
        });
    }

})();
