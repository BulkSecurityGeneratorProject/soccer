(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('public.players', {
            parent: 'public',
            url: '/public/players',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/public/players/players.html',
                    controller: 'PublicPlayersController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                
            }
        });
    }

})();
