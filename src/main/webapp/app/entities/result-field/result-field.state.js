(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('result-field', {
            parent: 'entity',
            url: '/result-field',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ResultFields'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/result-field/result-fields.html',
                    controller: 'ResultFieldController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('result-field-detail', {
            parent: 'entity',
            url: '/result-field/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ResultField'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/result-field/result-field-detail.html',
                    controller: 'ResultFieldDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ResultField', function($stateParams, ResultField) {
                    return ResultField.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'result-field',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('result-field-detail.edit', {
            parent: 'result-field-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/result-field/result-field-dialog.html',
                    controller: 'ResultFieldDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ResultField', function(ResultField) {
                            return ResultField.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('result-field.new', {
            parent: 'result-field',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/result-field/result-field-dialog.html',
                    controller: 'ResultFieldDialogController',
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
                    $state.go('result-field', null, { reload: 'result-field' });
                }, function() {
                    $state.go('result-field');
                });
            }]
        })
        .state('result-field.edit', {
            parent: 'result-field',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/result-field/result-field-dialog.html',
                    controller: 'ResultFieldDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ResultField', function(ResultField) {
                            return ResultField.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('result-field', null, { reload: 'result-field' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('result-field.delete', {
            parent: 'result-field',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/result-field/result-field-delete-dialog.html',
                    controller: 'ResultFieldDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ResultField', function(ResultField) {
                            return ResultField.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('result-field', null, { reload: 'result-field' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
