'use strict';

describe('Controller Tests', function() {

    describe('Event Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEvent, MockAssociation, MockDivisionEvent, MockGame, MockClub, MockTeam, MockPlayer, MockReferee, MockCoach;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEvent = jasmine.createSpy('MockEvent');
            MockAssociation = jasmine.createSpy('MockAssociation');
            MockDivisionEvent = jasmine.createSpy('MockDivisionEvent');
            MockGame = jasmine.createSpy('MockGame');
            MockClub = jasmine.createSpy('MockClub');
            MockTeam = jasmine.createSpy('MockTeam');
            MockPlayer = jasmine.createSpy('MockPlayer');
            MockReferee = jasmine.createSpy('MockReferee');
            MockCoach = jasmine.createSpy('MockCoach');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Event': MockEvent,
                'Association': MockAssociation,
                'DivisionEvent': MockDivisionEvent,
                'Game': MockGame,
                'Club': MockClub,
                'Team': MockTeam,
                'Player': MockPlayer,
                'Referee': MockReferee,
                'Coach': MockCoach
            };
            createController = function() {
                $injector.get('$controller')("EventDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'soccerApp:eventUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
