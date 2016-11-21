(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('public.club', {
            parent: 'public',
            url: '/public/club/{id}',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/public/club/club.html',
                    controller: 'PublicClubController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        });
    }

})();
