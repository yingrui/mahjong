'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */
describe('Dictionary Page', function () {

    beforeEach(function () {
        browser().navigateTo('../../#/dictionary');
    });


    it('should show some words start with pinyin a', function () {
        expect(repeater('.word-head').count()).toBe(4);
    });

});
