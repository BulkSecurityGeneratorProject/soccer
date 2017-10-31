(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('article-comment', {
            parent: 'entity',
            url: '/article-comment',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ArticleComments'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/article-comment/article-comments.html',
                    controller: 'ArticleCommentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('article-comment-detail', {
            parent: 'entity',
            url: '/article-comment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ArticleComment'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/article-comment/article-comment-detail.html',
                    controller: 'ArticleCommentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ArticleComment', function($stateParams, ArticleComment) {
                    return ArticleComment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'article-comment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('article-comment-detail.edit', {
            parent: 'article-comment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/article-comment/article-comment-dialog.html',
                    controller: 'ArticleCommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ArticleComment', function(ArticleComment) {
                            return ArticleComment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('article-comment.new', {
            parent: 'article-comment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/article-comment/article-comment-dialog.html',
                    controller: 'ArticleCommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                author: null,
                                createAt: null,
                                content: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('article-comment', null, { reload: 'article-comment' });
                }, function() {
                    $state.go('article-comment');
                });
            }]
        })
        .state('article-comment.edit', {
            parent: 'article-comment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/article-comment/article-comment-dialog.html',
                    controller: 'ArticleCommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ArticleComment', function(ArticleComment) {
                            return ArticleComment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('article-comment', null, { reload: 'article-comment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('article-comment.delete', {
            parent: 'article-comment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/article-comment/article-comment-delete-dialog.html',
                    controller: 'ArticleCommentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ArticleComment', function(ArticleComment) {
                            return ArticleComment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('article-comment', null, { reload: 'article-comment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
