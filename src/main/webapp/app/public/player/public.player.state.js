(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('public.player', {
            parent: 'public',
            url: '/public/player/{id}',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/public/player/player.html',
                    controller: 'PublicPlayerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        });
    }

})();
