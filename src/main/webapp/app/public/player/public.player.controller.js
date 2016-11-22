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

    PublicPlayerController.$inject = ['$scope','$state'];

    function PublicClubController ($scope, $state) {
    	var playerId = 1;
    	
        var vm = this;
        
        $scope.now = new Date();
        
        $scope.isBefore = function(dStr) {
            return $scope.now.getTime() > new Date(dStr).getTime();
        }
        
    }
})();
