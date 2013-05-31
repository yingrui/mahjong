
var ALL_PINYIN = ["a","ai","an","ang","ao","ba","bai","ban","bang","bao","bei","ben","beng","bi","bian","biao","bie","bin","bing","bo","bu","ca","cai","can","cang","cao","ce","cen","ceng","cha","chai","chan","chang","chao","che","chen","cheng","chi","chong","chou","chu","chuai","chuan","chuang","chui","chun","chuo","ci","cong","cou","cu","cuan","cui","cun","cuo","da","dai","dan","dang","dao","de","deng","di","dia","dian","diao","die","ding","diu","dong","dou","du","duan","dui","dun","duo","e","en","er","fa","fan","fang","fei","fen","feng","fo","fou","fu","ga","gai","gan","gang","gao","ge","gei","gen","geng","gong","gou","gu","gua","guai","guan","guang","gui","gun","guo","ha","hai","han","hang","hao","he","hei","hen","heng","hong","hou","hu","hua","huai","huan","huang","hui","hun","huo","ji","jia","jian","jiang","jiao","jie","jin","jing","jiong","jiu","ju","juan","jue","jun","ka","kai","kan","kang","kao","ke","ken","keng","kong","kou","ku","kua","kuai","kuan","kuang","kui","kun","kuo","la","lai","lan","lang","lao","le","lei","leng","li","lian","liang","liao","lie","lin","ling","liu","long","lou","lu","luan","lue","lun","luo","luu","ma","mai","man","mang","mao","me","mei","men","meng","mi","mian","miao","mie","min","ming","miu","mo","mou","mu","na","nai","nan","nang","nao","ne","nei","nen","neng","ni","nian","niang","niao","nie","nin","ning","niu","nong","nou","nu","nuan","nue","nuo","nuu","ou","pa","pai","pan","pang","pao","pei","pen","peng","pi","pian","piao","pie","pin","ping","po","pu","qi","qia","qian","qiang","qiao","qie","qin","qing","qiong","qiu","qu","quan","que","qun","ran","rang","rao","re","ren","reng","ri","rong","rou","ru","ruan","rui","run","ruo","sa","sai","san","sang","sao","se","sen","seng","sha","shai","shan","shang","shao","she","shei","shen","sheng","shi","shou","shu","shua","shuai","shuan","shuang","shui","shun","shuo","si","song","sou","su","suan","sui","sun","suo","ta","tai","tan","tang","tao","te","teng","ti","tian","tiao","tie","ting","tong","tou","tu","tuan","tui","tun","tuo","uuan","wa","wai","wan","wang","wei","wen","weng","wo","wu","xi","xia","xian","xiang","xiao","xie","xin","xing","xiong","xiu","xu","xuan","xue","xun","ya","yan","yang","yao","ye","yi","yin","ying","yo","yong","you","yu","yuan","yue","yun","za","zai","zan","zang","zao","ze","zei","zen","zeng","zha","zhai","zhan","zhang","zhao","zhe","zhen","zheng","zhi","zhong","zhou","zhu","zhua","zhuai","zhuan","zhuang","zhui","zhun","zhuo","zi","zong","zou","zu","zuan","zui","zun","zuo"];

function CoreDictionaryCtrl($scope, dictionaryService, partOfSpeechRepository, conceptRepository) {

    $scope.wordIndexes = ["A","B","C","D","E","F","G","H","J","K","L","M","N","O","P","Q","R","S","T","U","W","X","Y","Z"];
    $scope.pinyinIndexes = getRelatedPinyinList("A")
    $scope.wordHeads = [];
    $scope.wordItems = [];
    $scope.selectedIndex = $scope.wordIndexes[0];
    $scope.selectedPinyin = $scope.pinyinIndexes[0];
    $scope.selectedHead = null;
    $scope.allPartOfSpeeches = partOfSpeechRepository.getAll();

    $scope.switchIndex = function(wordIndex) {
        $scope.pinyinIndexes = getRelatedPinyinList(wordIndex);
        $scope.selectedPinyin = $scope.pinyinIndexes[0];
        $scope.switchPinyin($scope.pinyinIndexes[0])
    };

    $scope.switchPinyin = function(pinyin) {
        dictionaryService.getWordHeads('core', pinyin, function(result) {
            $scope.wordHeads = result;
            getWords(result[0]);
        });
    };

    $scope.switchPinyin($scope.selectedPinyin);

    $scope.switchHead = function(wordHead) {
        getWords(wordHead);
    };

    conceptRepository.getAll(function(data) {
        $scope.allConcepts = data;
    });

    function getWords(wordHead) {
        $scope.selectedHead = wordHead;
        dictionaryService.getWords('core', wordHead, function(result) {
            $scope.wordItems = result;
        });
    };

    function getRelatedPinyinList(alphabetical) {
        return _.filter(ALL_PINYIN, function(pinyin) {
            return alphabetical.toLowerCase().charAt(0) == pinyin.charAt(0)
        });
    }
}

CoreDictionaryCtrl.$inject = ['$scope', 'DictionaryService', 'PartOfSpeechRepository', 'ConceptRepository'];
