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
        }).state('club-nextgame-squad',{
            parent: 'club',
            url: '/games/{id}/team/{tid}',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/ext/game/game-squad.html',
                    controller: 'GameSquadController',
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
        .state('club.player',{
        	parent: 'club',
            url: '/{id}/player',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/dashboard/club/players.html',
                    controller: 'ClubPlayerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
               
            }
        })
        .state('club-player-detail', {
            parent: 'club.player',
            url: '/{pid}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Player'
            },
            views: {
                'content@': {
                    templateUrl: 'app/dashboard/club/player-detail.html',
                    controller: 'PlayerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Player', function($stateParams, Player) {
                    return Player.get({id : $stateParams.pid}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'player',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('club-player-new', {
            parent: 'club.player',
            url: '/create',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/dashboard/club/player-dialog.html',
                    controller: 'ClubPlayerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                birth: null,
                                height: null,
                                weight: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('club.player', null, { reload: 'club.player' });
                }, function() {
                    $state.go('club.player');
                });
            }]
        })
        .state('club-player-edit', {
            parent: 'club.player',
            url: '/{pid}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/player/player-dialog.html',
                    controller: 'ClubPlayerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Player', function(Player) {
                            return Player.get({id : $stateParams.pid}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('club-player-delete', {
            parent: 'club.player',
            url: '/{pid}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/player/player-delete-dialog.html',
                    controller: 'PlayerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Player', function(Player) {
                            return Player.get({id : $stateParams.pid}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('player', null, { reload: 'player' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
         .state('club.division-events', {
            parent: 'club',
            url: '/{id}/events',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/dashboard/club/division-events.html',
                    controller: 'ClubDivisionEventController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
         .state('club.division-event-signup', {
            parent: 'club.division-events',
            url: '/{did}',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/dashboard/club/division-event-signup.html',
                    controller: 'ClubDivisionEventSignupController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DivisionEvent', function($stateParams, DivisionEvent) {
                    return DivisionEvent.get({id : $stateParams.did}).$promise;
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
