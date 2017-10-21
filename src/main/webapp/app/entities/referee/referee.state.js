(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('referee', {
            parent: 'entity',
            url: '/referee',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Referees'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/referee/referees.html',
                    controller: 'RefereeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('referee-detail', {
            parent: 'entity',
            url: '/referee/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Referee'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/referee/referee-detail.html',
                    controller: 'RefereeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Referee', function($stateParams, Referee) {
                    return Referee.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'referee',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('referee-detail.edit', {
            parent: 'referee-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/referee/referee-dialog.html',
                    controller: 'RefereeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Referee', function(Referee) {
                            return Referee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('referee.new', {
            parent: 'referee',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/referee/referee-dialog.html',
                    controller: 'RefereeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                picture: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('referee', null, { reload: 'referee' });
                }, function() {
                    $state.go('referee');
                });
            }]
        })
        .state('referee.edit', {
            parent: 'referee',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/referee/referee-dialog.html',
                    controller: 'RefereeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Referee', function(Referee) {
                            return Referee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('referee', null, { reload: 'referee' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('referee.delete', {
            parent: 'referee',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/referee/referee-delete-dialog.html',
                    controller: 'RefereeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Referee', function(Referee) {
                            return Referee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('referee', null, { reload: 'referee' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
