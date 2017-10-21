(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        
    	$stateProvider.state('division-event.generate-games', {
            parent: 'entity',
            url: '/division-event/{id}/generate-games',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/ext/division-event/division-event-generate-games.html',
                    controller: 'DivisionEventGameGenerateController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        }).state('division-event.games', {
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
        }).state('association-division-event-gameedit', {
            parent: 'division-event.games',
            url: '/{gid}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/ext/division-event/division-event-game-dialog.html',
                    controller: 'DivisionEventGameDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Game', function(Game) {
                            return Game.get({id : $stateParams.gid}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: true});
                }, function() {
                    $state.go('^');
                });
            }]
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
