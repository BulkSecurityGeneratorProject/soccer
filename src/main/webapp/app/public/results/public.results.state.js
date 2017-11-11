(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('public.results', {
            parent: 'public',
            url: '/public/results',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/public/results/results.html',
                    controller: 'PublicResultsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {

            }
        });
    }

})();
