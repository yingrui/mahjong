'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('Corpus Editor App', function () {

    it('should redirect index.html to index.html#/article', function () {
        browser().navigateTo('../../app/index.html');
        expect(browser().location().url()).toBe('/article');
    });

    describe('Article list view', function () {

        beforeEach(function () {
            browser().navigateTo('../../app/#/article');
        });


        it('should filter the article list as user types into the search box', function () {
            expect(repeater('.articles li').count()).toBe(2);

            input('query').enter('first');
            expect(repeater('.articles li').count()).toBe(1);

            input('query').enter('abc');
            expect(repeater('.articles li').count()).toBe(0);
        });


        it('should be possible to control article order via the drop down select box', function () {

            expect(repeater('.articles li').column('article.name')).
                toEqual(["The Second Article",
                "First Article"]);

            select('orderProp').option('Alphabetical');

            expect(repeater('.articles li').column('article.name')).
                toEqual(["First Article",
                "The Second Article"]);
        });


        it('should render article specific links', function () {
            input('query').enter('first');
            element('.articles li a').click();
            expect(browser().location().url()).toBe('/article/article1');
        });
    });


    describe('Article detail view', function () {

        beforeEach(function () {
            browser().navigateTo('../../app/index.html#/article/article1');
        });

        it('should display article page', function () {
            expect(repeater('.sentences p').count()).toBe(2);
            expect(repeater('.word span').count()).toBe(6);
            expect(repeater('.word input').count()).toBe(6);
        });

        it('should display corresponding input element when click word', function () {
            element('.word span:first').click();
            input('word.name').enter('abc');
            expect(element('.word span:first').text()).toBe('abc');
            expect(element('.word span:eq(2)').text()).toBe('abc');
        });

        it('should reset all input fields when click refresh button', function () {
            element('.btn').click();
            expect(element('.word span:first').text()).toBe('自然');
            expect(element('.word span:eq(1)').text()).toBe('语言');
            expect(element('.word span:eq(2)').text()).toBe('处理');
        });
    });
});
