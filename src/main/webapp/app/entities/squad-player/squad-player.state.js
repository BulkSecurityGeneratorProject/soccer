(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('squad-player', {
            parent: 'entity',
            url: '/squad-player',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SquadPlayers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/squad-player/squad-players.html',
                    controller: 'SquadPlayerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('squad-player-detail', {
            parent: 'entity',
            url: '/squad-player/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SquadPlayer'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/squad-player/squad-player-detail.html',
                    controller: 'SquadPlayerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'SquadPlayer', function($stateParams, SquadPlayer) {
                    return SquadPlayer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'squad-player',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('squad-player-detail.edit', {
            parent: 'squad-player-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/squad-player/squad-player-dialog.html',
                    controller: 'SquadPlayerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SquadPlayer', function(SquadPlayer) {
                            return SquadPlayer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('squad-player.new', {
            parent: 'squad-player',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/squad-player/squad-player-dialog.html',
                    controller: 'SquadPlayerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                playerNumber: null,
                                isSubstitute: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('squad-player', null, { reload: 'squad-player' });
                }, function() {
                    $state.go('squad-player');
                });
            }]
        })
        .state('squad-player.edit', {
            parent: 'squad-player',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/squad-player/squad-player-dialog.html',
                    controller: 'SquadPlayerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SquadPlayer', function(SquadPlayer) {
                            return SquadPlayer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('squad-player', null, { reload: 'squad-player' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('squad-player.delete', {
            parent: 'squad-player',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/squad-player/squad-player-delete-dialog.html',
                    controller: 'SquadPlayerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SquadPlayer', function(SquadPlayer) {
                            return SquadPlayer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('squad-player', null, { reload: 'squad-player' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
