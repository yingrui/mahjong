'use strict';

/* Services */

corpusEditorServices.
    factory('i18n', function () {
        var i18n_cn = {
            SegmentInputPlaceholderText : '您可以在这里输入一些句子短语，试一试',
            SegmentIntroduceText : '最大概率分词',
            SegmentButtonText : '分词',
            NavSegmentText : '中文分词'
        }

        var i18n = {
            SegmentInputPlaceholderText : 'You could input some Chinese Words and try',
            SegmentIntroduceText : 'Maximum Probability Segment',
            SegmentButtonText : 'Segment',
            NavSegmentText : 'Chinese Breaking'
        }

        var lang = {
            'zh-CN' : i18n_cn
        }

        return function(key) {
            var dict = lang[navigator.language] || i18n;
            return dict[key];
        };

    });
