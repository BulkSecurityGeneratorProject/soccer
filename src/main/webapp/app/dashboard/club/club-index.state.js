(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('club.dash', {
            parent: 'club',
            url: '/{id}/dashboard',
            data: {
                authorities: ['ROLE_USER'],
            },
           views: {
                'content@': {
                    templateUrl: 'app/dashboard/club/club-index.html',
                    controller: 'ClubDashboardController',
                    controllerAs: 'vm'
                }
            },
            resolve: {

            }
        }).state('club.dashedit', {
            parent: 'club.dash',
            url: '/edit',
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
                    $state.go('club.dash', null, { reload: 'club.dash' });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('club-team',{
        	parent: 'club',
            url: '/{id}/team',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/dashboard/club/teams.html',
                    controller: 'ClubTeamController',
                    controllerAs: 'vm'
                }
            },
            resolve: {

            }
        })
        .state('club-team-detail', {
            parent: 'club-team',
            url: '/{tid}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Team'
            },
            views: {
                'content@': {
                    templateUrl: 'app/dashboard/club/team-detail.html',
                    controller: 'TeamDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Team', function($stateParams, Team) {
                    return Team.get({id : $stateParams.tid}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'team',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('club-team-new', {
            parent: 'club-team',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/dashboard/club/team-dialog.html',
                    controller: 'ClubTeamDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('club-team', null, { reload: 'club-team' });
                }, function() {
                    $state.go('club-team');
                });
            }]
        })
        .state('club-team-edit', {
            parent: 'club-team',
            url: '/{tid}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team/team-dialog.html',
                    controller: 'TeamDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Team', function(Team) {
                            return Team.get({id : $stateParams.tid}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('club-team', null, { reload: 'club-team' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('club-team-delete', {
            parent: 'club-team',
            url: '/{tid}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team/team-delete-dialog.html',
                    controller: 'TeamDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Team', function(Team) {
                            return Team.get({id : $stateParams.tid}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('club-team', null, { reload: 'club-team' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('club.players',{
        	parent: 'club',
            url: '/{id}/players',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/player/players.html',
                    controller: 'ClubPlayerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {

            }
        });
    }

})();
