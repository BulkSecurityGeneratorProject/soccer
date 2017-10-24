(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lineup', {
            parent: 'entity',
            url: '/lineup?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Lineups'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lineup/lineups.html',
                    controller: 'LineupController',
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
        .state('lineup-detail', {
            parent: 'entity',
            url: '/lineup/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Lineup'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lineup/lineup-detail.html',
                    controller: 'LineupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Lineup', function($stateParams, Lineup) {
                    return Lineup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lineup',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lineup-detail.edit', {
            parent: 'lineup-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lineup/lineup-dialog.html',
                    controller: 'LineupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lineup', function(Lineup) {
                            return Lineup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lineup.new', {
            parent: 'lineup',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lineup/lineup-dialog.html',
                    controller: 'LineupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                playerNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lineup', null, { reload: 'lineup' });
                }, function() {
                    $state.go('lineup');
                });
            }]
        })
        .state('lineup.edit', {
            parent: 'lineup',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lineup/lineup-dialog.html',
                    controller: 'LineupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lineup', function(Lineup) {
                            return Lineup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lineup', null, { reload: 'lineup' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lineup.delete', {
            parent: 'lineup',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lineup/lineup-delete-dialog.html',
                    controller: 'LineupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lineup', function(Lineup) {
                            return Lineup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lineup', null, { reload: 'lineup' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
