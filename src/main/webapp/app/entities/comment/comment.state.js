(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('comment', {
            parent: 'entity',
            url: '/comment?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Comments'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/comment/comments.html',
                    controller: 'CommentController',
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
        .state('comment-detail', {
            parent: 'entity',
            url: '/comment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Comment'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/comment/comment-detail.html',
                    controller: 'CommentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Comment', function($stateParams, Comment) {
                    return Comment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'comment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('comment-detail.edit', {
            parent: 'comment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comment/comment-dialog.html',
                    controller: 'CommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Comment', function(Comment) {
                            return Comment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('comment.new', {
            parent: 'comment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comment/comment-dialog.html',
                    controller: 'CommentDialogController',
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
                    $state.go('comment', null, { reload: 'comment' });
                }, function() {
                    $state.go('comment');
                });
            }]
        })
        .state('comment.edit', {
            parent: 'comment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comment/comment-dialog.html',
                    controller: 'CommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Comment', function(Comment) {
                            return Comment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('comment', null, { reload: 'comment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('comment.delete', {
            parent: 'comment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comment/comment-delete-dialog.html',
                    controller: 'CommentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Comment', function(Comment) {
                            return Comment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('comment', null, { reload: 'comment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
