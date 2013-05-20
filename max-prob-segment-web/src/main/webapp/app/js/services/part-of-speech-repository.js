corpusEditorServices.
    factory('PartOfSpeechRepository', function ($http) {

        var allPartOfSpeeches = [
            {id:1, name:'N', note:'名词', $$hashKey:1},
            {id:2, name:'T', note:'时间词', $$hashKey:2},
            {id:3, name:'S', note:'处所词', $$hashKey:3},
            {id:4, name:'F', note:'方位词', $$hashKey:4},
            {id:5, name:'M', note:'数词', $$hashKey:5},
            {id:6, name:'Q', note:'量词', $$hashKey:6},
            {id:7, name:'B', note:'区别词', $$hashKey:7},
            {id:8, name:'R', note:'代词', $$hashKey:8},
            {id:9, name:'V', note:'动词', $$hashKey:9},
            {id:10, name:'A', note:'形容词', $$hashKey:10},
            {id:11, name:'Z', note:'状态词', $$hashKey:11},
            {id:12, name:'D', note:'副词', $$hashKey:12},
            {id:13, name:'P', note:'介词', $$hashKey:13},
            {id:14, name:'C', note:'连词', $$hashKey:14},
            {id:15, name:'U', note:'其他助词', $$hashKey:15},
            {id:16, name:'Y', note:'语气词', $$hashKey:16},
            {id:17, name:'E', note:'叹词', $$hashKey:17},
            {id:18, name:'O', note:'拟声词', $$hashKey:18},
            {id:19, name:'I', note:'成语', $$hashKey:19},
            {id:20, name:'L', note:'习用语', $$hashKey:20},
            {id:21, name:'J', note:'简称略语', $$hashKey:21},
            {id:22, name:'H', note:'前接成分', $$hashKey:22},
            {id:23, name:'K', note:'后接成分', $$hashKey:23},
            {id:24, name:'G', note:'语素', $$hashKey:24},
            {id:25, name:'X', note:'非语素字', $$hashKey:25},
            {id:26, name:'W', note:'其他标点符号', $$hashKey:26},
            {id:27, name:'NR', note:'人名', $$hashKey:27},
            {id:28, name:'NS', note:'地名', $$hashKey:28},
            {id:29, name:'NT', note:'机构团体', $$hashKey:29},
            {id:30, name:'NZ', note:'其他专名', $$hashKey:30},
            {id:31, name:'NX', note:'外文词', $$hashKey:31},
            {id:33, name:'NG', note:'名语素', $$hashKey:33},
            {id:34, name:'VG', note:'动语素', $$hashKey:34},
            {id:35, name:'AG', note:'形语素', $$hashKey:35},
            {id:36, name:'TG', note:'时语素', $$hashKey:36},
            {id:37, name:'DG', note:'副语素', $$hashKey:37},
            {id:38, name:'OG', note:'拟声语素', $$hashKey:38},
            {id:39, name:'AUX', note:'虚词', $$hashKey:39},
            {id:40, name:'VN', note:'名动词', $$hashKey:40},
            {id:41, name:'AN', note:'名形词', $$hashKey:41},
            {id:42, name:'VD', note:'副动词', $$hashKey:42},
            {id:43, name:'AD', note:'副形词', $$hashKey:43},
            {id:44, name:'UN', note:'未登录词', $$hashKey:44}
        ];

        return {
            getAll: function() {
                return allPartOfSpeeches;
            }
        };
    });
