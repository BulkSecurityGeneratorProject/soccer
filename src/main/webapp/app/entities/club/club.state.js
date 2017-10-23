(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('club', {
            parent: 'entity',
            url: '/club',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Clubs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/club/clubs.html',
                    controller: 'ClubController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('club-detail', {
            parent: 'entity',
            url: '/club/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Club'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/club/club-detail.html',
                    controller: 'ClubDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Club', function($stateParams, Club) {
                    return Club.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'club',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('club-detail.edit', {
            parent: 'club-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/club/club-dialog.html',
                    controller: 'ClubDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Club', function(Club) {
                            return Club.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('club.new', {
            parent: 'club',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/club/club-dialog.html',
                    controller: 'ClubDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createAt: null,
                                name: null,
                                shortName: null,
                                picture: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('club', null, { reload: 'club' });
                }, function() {
                    $state.go('club');
                });
            }]
        })
        .state('club.edit', {
            parent: 'club',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/club/club-dialog.html',
                    controller: 'ClubDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Club', function(Club) {
                            return Club.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('club', null, { reload: 'club' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('club.delete', {
            parent: 'club',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/club/club-delete-dialog.html',
                    controller: 'ClubDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Club', function(Club) {
                            return Club.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('club', null, { reload: 'club' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
