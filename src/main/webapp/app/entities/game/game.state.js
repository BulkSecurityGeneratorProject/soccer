(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('game', {
            parent: 'entity',
            url: '/game',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Games'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/game/games.html',
                    controller: 'GameController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('game-detail', {
            parent: 'entity',
            url: '/game/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Game'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/game/game-detail.html',
                    controller: 'GameDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Game', function($stateParams, Game) {
                    return Game.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'game',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('game-detail.edit', {
            parent: 'game-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game/game-dialog.html',
                    controller: 'GameDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Game', function(Game) {
                            return Game.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('game.new', {
            parent: 'game',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game/game-dialog.html',
                    controller: 'GameDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startAt: null,
                                homeScore: null,
                                roadScore: null,
                                note: null,
                                homeScoreHalf: null,
                                roadScoreHalf: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('game', null, { reload: 'game' });
                }, function() {
                    $state.go('game');
                });
            }]
        })
        .state('game.edit', {
            parent: 'game',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game/game-dialog.html',
                    controller: 'GameDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Game', function(Game) {
                            return Game.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('game', null, { reload: 'game' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('game.delete', {
            parent: 'game',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game/game-delete-dialog.html',
                    controller: 'GameDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Game', function(Game) {
                            return Game.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('game', null, { reload: 'game' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('game.homesquad',{
        	parent:'game',
        	url:'/{id}/homesquad',
        	data:{
        		authorities: ['ROLE_USER'],
        		homeTeam:true
        	},
        	views:{
        		'content@': {
                    templateUrl: 'app/entities/game/game-squad.html',
                    controller: 'GameSquadController',
                    controllerAs: 'vm'
                }
        	},
        	resolve: {

            }
        })
        .state('game.roadsquad',{
        	parent:'game',
        	url:'/{id}/roadsquad',
        	data:{
        		authorities: ['ROLE_USER']
        	},
        	views:{
        		'content@': {
                    templateUrl: 'app/entities/game/game-squad.html',
                    controller: 'GameSquadController',
                    controllerAs: 'vm'
                }
        	},
        	resolve: {

            }
        })
       .state('game.result', {
            parent: 'game',
            url: '/{id}/result',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/ext/result-data/result-data.editgrid.html',
                    controller: 'ResultDataEditGridController',
                    controllerAs: 'vm'
                }
            },
            resolve: {

            }
        });
    }

})();
