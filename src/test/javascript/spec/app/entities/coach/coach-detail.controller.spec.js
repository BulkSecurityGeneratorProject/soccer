'use strict';

describe('Controller Tests', function() {

    describe('Coach Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCoach, MockDict, MockAssociation, MockTeam;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCoach = jasmine.createSpy('MockCoach');
            MockDict = jasmine.createSpy('MockDict');
            MockAssociation = jasmine.createSpy('MockAssociation');
            MockTeam = jasmine.createSpy('MockTeam');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Coach': MockCoach,
                'Dict': MockDict,
                'Association': MockAssociation,
                'Team': MockTeam
            };
            createController = function() {
                $injector.get('$controller')("CoachDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'soccerApp:coachUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
