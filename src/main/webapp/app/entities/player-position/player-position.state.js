(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('player-position', {
            parent: 'entity',
            url: '/player-position',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PlayerPositions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/player-position/player-positions.html',
                    controller: 'PlayerPositionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('player-position-detail', {
            parent: 'entity',
            url: '/player-position/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PlayerPosition'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/player-position/player-position-detail.html',
                    controller: 'PlayerPositionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PlayerPosition', function($stateParams, PlayerPosition) {
                    return PlayerPosition.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'player-position',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('player-position-detail.edit', {
            parent: 'player-position-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/player-position/player-position-dialog.html',
                    controller: 'PlayerPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PlayerPosition', function(PlayerPosition) {
                            return PlayerPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('player-position.new', {
            parent: 'player-position',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/player-position/player-position-dialog.html',
                    controller: 'PlayerPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('player-position', null, { reload: 'player-position' });
                }, function() {
                    $state.go('player-position');
                });
            }]
        })
        .state('player-position.edit', {
            parent: 'player-position',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/player-position/player-position-dialog.html',
                    controller: 'PlayerPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PlayerPosition', function(PlayerPosition) {
                            return PlayerPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('player-position', null, { reload: 'player-position' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('player-position.delete', {
            parent: 'player-position',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/player-position/player-position-delete-dialog.html',
                    controller: 'PlayerPositionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PlayerPosition', function(PlayerPosition) {
                            return PlayerPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('player-position', null, { reload: 'player-position' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
