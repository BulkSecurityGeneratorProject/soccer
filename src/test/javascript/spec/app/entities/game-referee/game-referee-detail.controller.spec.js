'use strict';

describe('Controller Tests', function() {

    describe('GameReferee Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockGameReferee, MockGame, MockReferee, MockDict;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockGameReferee = jasmine.createSpy('MockGameReferee');
            MockGame = jasmine.createSpy('MockGame');
            MockReferee = jasmine.createSpy('MockReferee');
            MockDict = jasmine.createSpy('MockDict');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'GameReferee': MockGameReferee,
                'Game': MockGame,
                'Referee': MockReferee,
                'Dict': MockDict
            };
            createController = function() {
                $injector.get('$controller')("GameRefereeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'soccerApp:gameRefereeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
