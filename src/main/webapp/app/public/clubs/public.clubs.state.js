(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('public.clubs', {
            parent: 'public',
            url: '/public/clubs',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/public/clubs/clubs.html',
                    controller: 'PublicClubsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                
            }
        });
    }

})();
