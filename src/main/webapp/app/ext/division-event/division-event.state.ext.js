(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        
    	$stateProvider.state('division-event.games', {
            parent: 'entity',
            url: '/division-event/{id}/games',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/ext/division-event/division-event-games.html',
                    controller: 'DivisionEventGameController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        }).state('division-event-signup', {
            parent: 'entity',
            url: '/division-event/{id}/signup',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/ext/division-event/division-event-signup.html',
                    controller: 'DivisionEventSignupController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DivisionEvent', function($stateParams, DivisionEvent) {
                    return DivisionEvent.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'division-event',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        });
    }

})();
