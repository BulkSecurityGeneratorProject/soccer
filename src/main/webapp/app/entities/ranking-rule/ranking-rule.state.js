(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ranking-rule', {
            parent: 'entity',
            url: '/ranking-rule',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RankingRules'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ranking-rule/ranking-rules.html',
                    controller: 'RankingRuleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('ranking-rule-detail', {
            parent: 'entity',
            url: '/ranking-rule/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RankingRule'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ranking-rule/ranking-rule-detail.html',
                    controller: 'RankingRuleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'RankingRule', function($stateParams, RankingRule) {
                    return RankingRule.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ranking-rule',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ranking-rule-detail.edit', {
            parent: 'ranking-rule-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ranking-rule/ranking-rule-dialog.html',
                    controller: 'RankingRuleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RankingRule', function(RankingRule) {
                            return RankingRule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ranking-rule.new', {
            parent: 'ranking-rule',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ranking-rule/ranking-rule-dialog.html',
                    controller: 'RankingRuleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                win: null,
                                loss: null,
                                draw: null,
                                scoreFor: null,
                                scoreAgain: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ranking-rule', null, { reload: 'ranking-rule' });
                }, function() {
                    $state.go('ranking-rule');
                });
            }]
        })
        .state('ranking-rule.edit', {
            parent: 'ranking-rule',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ranking-rule/ranking-rule-dialog.html',
                    controller: 'RankingRuleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RankingRule', function(RankingRule) {
                            return RankingRule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ranking-rule', null, { reload: 'ranking-rule' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ranking-rule.delete', {
            parent: 'ranking-rule',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ranking-rule/ranking-rule-delete-dialog.html',
                    controller: 'RankingRuleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RankingRule', function(RankingRule) {
                            return RankingRule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ranking-rule', null, { reload: 'ranking-rule' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
