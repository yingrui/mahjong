'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */
describe('Segment Page', function () {

    beforeEach(function () {
        browser().navigateTo('../../#/segment');
    });


    it('should show checkboxes about segment parameters', function () {
        expect(input('isSupportEnglish').val()).toBeTruthy();
        expect(input('isEnglishStemming').val()).toBe('on');
        expect(input('isRecognizePinyin').val()).toBeTruthy();
        expect(input('isSegmentMin').val()).toBeTruthy();
    });


    it('should show segment result when click segment button', function () {
        input("inputText").enter('Hello friend')
        element('#btn-segment').click();
        sleep(3);
        expect(repeater('.word').count()).toBe(2);
        expect(repeater('.word').row(0)).toEqual(['Hello', 'X', 'Hello', 'N/A']);
        expect(repeater('.word').row(1)).toEqual(['friend', 'N', 'friend', 'N/A']);
    });

});
