(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('public.division', {
            parent: 'public',
            url: '/public/division/{id}',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/public/division/division.html',
                    controller: 'PublicDivisionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        });
    }

})();
