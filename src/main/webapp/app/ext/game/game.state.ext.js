(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('game.homesquad',{
        	parent:'game',
        	url:'/{id}/homesquad',
        	data:{
        		authorities: ['ROLE_USER'],
        		homeTeam:true
        	},
        	views:{
        		'content@': {
                    templateUrl: 'app/ext/game/game-squad.html',
                    controller: 'GameSquadController',
                    controllerAs: 'vm'
                }
        	},
        	resolve: {

            }
        })
        .state('game.roadsquad',{
        	parent:'game',
        	url:'/{id}/roadsquad',
        	data:{
        		authorities: ['ROLE_USER']
        	},
        	views:{
        		'content@': {
                    templateUrl: 'app/ext/game/game-squad.html',
                    controller: 'GameSquadController',
                    controllerAs: 'vm'
                }
        	},
        	resolve: {

            }
        })
       .state('game.result', {
            parent: 'game',
            url: '/{id}/result',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/ext/result-data/result-data.editgrid.html',
                    controller: 'ResultDataEditGridController',
                    controllerAs: 'vm'
                }
            },
            resolve: {

            }
        });
    }

})();
