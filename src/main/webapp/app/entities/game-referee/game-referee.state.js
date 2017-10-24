(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('game-referee', {
            parent: 'entity',
            url: '/game-referee?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'GameReferees'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/game-referee/game-referees.html',
                    controller: 'GameRefereeController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('game-referee-detail', {
            parent: 'entity',
            url: '/game-referee/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'GameReferee'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/game-referee/game-referee-detail.html',
                    controller: 'GameRefereeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'GameReferee', function($stateParams, GameReferee) {
                    return GameReferee.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'game-referee',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('game-referee-detail.edit', {
            parent: 'game-referee-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game-referee/game-referee-dialog.html',
                    controller: 'GameRefereeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GameReferee', function(GameReferee) {
                            return GameReferee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('game-referee.new', {
            parent: 'game-referee',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game-referee/game-referee-dialog.html',
                    controller: 'GameRefereeDialogController',
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
                    $state.go('game-referee', null, { reload: 'game-referee' });
                }, function() {
                    $state.go('game-referee');
                });
            }]
        })
        .state('game-referee.edit', {
            parent: 'game-referee',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game-referee/game-referee-dialog.html',
                    controller: 'GameRefereeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GameReferee', function(GameReferee) {
                            return GameReferee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('game-referee', null, { reload: 'game-referee' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('game-referee.delete', {
            parent: 'game-referee',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game-referee/game-referee-delete-dialog.html',
                    controller: 'GameRefereeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GameReferee', function(GameReferee) {
                            return GameReferee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('game-referee', null, { reload: 'game-referee' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
