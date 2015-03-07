'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('Max Prob Segment Project', function () {

    it('should redirect / to /#/', function () {
        browser().navigateTo('../../');
        expect(browser().location().url()).toBe('/');
    });
});
