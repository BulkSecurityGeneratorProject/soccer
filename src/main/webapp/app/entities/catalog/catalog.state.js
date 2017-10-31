(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('catalog', {
            parent: 'entity',
            url: '/catalog',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Catalogs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/catalog/catalogs.html',
                    controller: 'CatalogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('catalog-detail', {
            parent: 'entity',
            url: '/catalog/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Catalog'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/catalog/catalog-detail.html',
                    controller: 'CatalogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Catalog', function($stateParams, Catalog) {
                    return Catalog.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'catalog',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('catalog-detail.edit', {
            parent: 'catalog-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/catalog/catalog-dialog.html',
                    controller: 'CatalogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Catalog', function(Catalog) {
                            return Catalog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('catalog.new', {
            parent: 'catalog',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/catalog/catalog-dialog.html',
                    controller: 'CatalogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                type: null,
                                openComment: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('catalog', null, { reload: 'catalog' });
                }, function() {
                    $state.go('catalog');
                });
            }]
        })
        .state('catalog.edit', {
            parent: 'catalog',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/catalog/catalog-dialog.html',
                    controller: 'CatalogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Catalog', function(Catalog) {
                            return Catalog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('catalog', null, { reload: 'catalog' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('catalog.delete', {
            parent: 'catalog',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/catalog/catalog-delete-dialog.html',
                    controller: 'CatalogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Catalog', function(Catalog) {
                            return Catalog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('catalog', null, { reload: 'catalog' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
