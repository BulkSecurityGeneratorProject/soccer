(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicPlayerController', PublicPlayerController)
        .filter('ageFilter', function() {
            function calculateAge(birthday) { // birthday is a date
                var ageDifMs = Date.now() - birthday.getTime();
                var ageDate = new Date(ageDifMs); // miliseconds from epoch
                return Math.abs(ageDate.getUTCFullYear() - 1970);
            }
            return function(birthdate) {
                  return calculateAge(new Date(birthdate));
            }; 
       });

    PublicPlayerController.$inject = ['$scope','$state','Player','PlayerExt'];

    function PublicPlayerController ($scope, $state,Player,PlayerExt) {
        var vm = this;
        $scope.now = new Date();
        
        $scope.isBefore = function(dStr) {
            return $scope.now.getTime() > new Date(dStr).getTime();
        }
        
        // 1. get player basic information
        vm.player = Player.get({id:$state.params.id});
        
        // 2. fetch career of player, Career format: Season,Team,Division,Time,Goal,Assias,Yellow,Red
        vm.careerData = PlayerExt.queryCareer({id:$state.params.id});
        // 3. fetch glory of player, glory format: Season,Division
    }
})();
