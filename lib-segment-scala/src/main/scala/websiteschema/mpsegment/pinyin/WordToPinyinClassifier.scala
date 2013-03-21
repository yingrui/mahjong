//package websiteschema.mpsegment.pinyin;
//
//import websiteschema.mpsegment.core.SegmentResult;
//import websiteschema.mpsegment.hmm.Node;
//import websiteschema.mpsegment.hmm.ObserveListException;
//import websiteschema.mpsegment.util.CharCheckUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
//class WordToPinyinClassifier {
//
//    var model: WordToPinyinModel = null
//
//    def setModel(model: WordToPinyinModel) {
//        this.model = model;
//    }
//
//    def classify(result: SegmentResult) {
//        try {
//            var originalStr = result.toOriginalString()
//            var pinyinList = classify(originalStr)
//            var pos = 0
//            for (Int i = 0; i < result.length(); i++) {
//                var wordLength = result.getWord(i).length()
//                var pinyin = join(pinyinList.subList(pos, pos += wordLength), "'", result.getWord(i))
//                result.setPinyin(i, pinyin);
//            }
//        } catch {
//            System.err.println(ex);
//        }
//    }
//
//    private def join(list: List[String], sep: String, word: String) : String = {
//        var stringBuilder = new StringBuilder()
//        for (Int i = 0; i < list.size(); i++) {
//            var str = list.get(i)
//            stringBuilder.append(str);
//            if (null != sep && i < list.size() - 1 && CharCheckUtil.isChinese(word.charAt(i+1))) {
//                stringBuilder.append(sep);
//            }
//        }
//        return stringBuilder.toString();
//    }
//
//    def classify(o: String) : List[String] = throws ObserveListException {
//        var observeList = new ArrayList[String](o.length())
//        for (Int i = 0; i < o.length(); i++) {
//            observeList.add(String.valueOf(o.charAt(i)));
//        }
//        return classify(observeList);
//    }
//
//    private def classify(o: List[String]) : List[String] = throws ObserveListException {
//        assert (null != o && o.size() > 0);
//        var sections = findSectionsByUnknownCharacter(o)
//        return classifySectionList(sections);
//    }
//
//    private def classifySectionList(sections: List[Section]) : List[String] = throws ObserveListException {
//        var result = new ArrayList[String]()
//        for (section <- sections) {
//            if (section.hasKnownCharacters()) {
//                var observeCharacters = section.characters
//                result.addAll(convert(classifyOberveList(observeCharacters)));
//            }
//            if (section.hasUnknwonCharacter()) {
//                result.add(section.unknownChar);
//            }
//        }
//        return result;
//    }
//
//    private def classifyOberveList(observeCharacters: List[String]) : List[Node] = throws ObserveListException {
//        return model.getViterbi().calculateWithLog(observeCharacters);
//    }
//
//    private def findSectionsByUnknownCharacter(o: List[String]) : List[Section] = {
//        var lastSectorPos = 0
//        var sections = new ArrayList[Section]()
//        for (Int i = 0; i < o.size(); i++) {
//            var ch = o.get(i)
//            var knownObserveNode = model.knwonObserve(ch)
//            if (!knownObserveNode) {
//                var section = new Section(o, lastSectorPos, i)
//                sections.add(section);
//                lastSectorPos = i + 1;
//            }
//        }
//        var sec = new Section(o, lastSectorPos)
//        sections.add(sec);
//        return sections;
//    }
//
//    private def convert(nodeList: List[Node]) : List[String] = {
//        var result = new ArrayList[String](nodeList.size())
//        for (node <- nodeList) {
//            result.add(node.getName());
//        }
//        return result;
//    }
//
//    class Section {
//        var characters: List[String] = null
//        var unknownChar: String = null
//
//        Section(List[String] o, Int start, Int end) {
//            if (end > start) {
//                characters = new ArrayList[String]();
//                for (Int i = 0; i < end - start; i++) {
//                    characters.add(o.get(i + start));
//                }
//                unknownChar = o.get(end);
//            } else if (end == start) {
//                unknownChar = o.get(end);
//            }
//        }
//
//        Section(List[String] o, Int start) {
//            if (o.size() > start) {
//                characters = new ArrayList[String]();
//                for (Int i = start; i < o.size(); i++) {
//                    characters.add(o.get(i));
//                }
//            }
//        }
//
//        Boolean hasKnownCharacters() {
//            return null != characters;
//        }
//
//        Boolean hasUnknwonCharacter() {
//            return null != unknownChar;
//        }
//    }
//
//}
//
