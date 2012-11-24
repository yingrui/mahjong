package websiteschema.mpsegment.pinyin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.hmm.Node;
import websiteschema.mpsegment.hmm.ObserveListException;

import java.util.ArrayList;
import java.util.List;

public class WordToPinyinClassifier {

    WordToPinyinModel model;
    final static Log l = LogFactory.getLog("segment");

    public void setModel(WordToPinyinModel model) {
        this.model = model;
    }

    public void classify(SegmentResult result) {
        try {
            String originalStr = result.toOriginalString();
            List<String> pinyinList = classify(originalStr);
            int pos = 0;
            for (int i = 0; i < result.length(); i++) {
                int wordLength = result.getWord(i).length();
                String pinyin = join(pinyinList.subList(pos, pos += wordLength), "'");
                result.setPinyin(i, pinyin);
            }
        } catch (ObserveListException ex) {
            l.error(ex);
        }
    }

    private String join(List<String> list, String sep) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            stringBuilder.append(str);
            if (null != sep && i < list.size() - 1) {
                stringBuilder.append(sep);
            }
        }
        return stringBuilder.toString();
    }

    public List<String> classify(String o) throws ObserveListException {
        List<String> observeList = new ArrayList<String>(o.length());
        for (int i = 0; i < o.length(); i++) {
            observeList.add(String.valueOf(o.charAt(i)));
        }
        return classify(observeList);
    }

    private List<String> classify(List<String> o) throws ObserveListException {
        assert (null != o && o.size() > 0);
        List<Section> sections = findSectionsByUnknownCharacter(o);
        return classifySectionList(sections);
    }

    private List<String> classifySectionList(List<Section> sections) throws ObserveListException {
        List<String> result = new ArrayList<String>();
        for (Section section : sections) {
            if (section.hasKnownCharacters()) {
                List<String> observeCharacters = section.characters;
                result.addAll(convert(classifyOberveList(observeCharacters)));
            }
            if (section.hasUnknwonCharacter()) {
                result.add(section.unknownChar);
            }
        }
        return result;
    }

    private List<Node> classifyOberveList(List<String> observeCharacters) throws ObserveListException {
        return model.getViterbi().calculateWithLog(observeCharacters);
    }

    private List<Section> findSectionsByUnknownCharacter(List<String> o) {
        int lastSectorPos = 0;
        List<Section> sections = new ArrayList<Section>();
        for (int i = 0; i < o.size(); i++) {
            String ch = o.get(i);
            boolean knownObserveNode = model.knwonObserve(ch);
            if (!knownObserveNode) {
                Section section = new Section(o, lastSectorPos, i);
                sections.add(section);
                lastSectorPos = i + 1;
            }
        }
        Section sec = new Section(o, lastSectorPos);
        sections.add(sec);
        return sections;
    }

    private List<String> convert(List<Node> nodeList) {
        List<String> result = new ArrayList<String>(nodeList.size());
        for (Node node : nodeList) {
            result.add(node.getName());
        }
        return result;
    }

    class Section {
        List<String> characters;
        String unknownChar;

        Section(List<String> o, int start, int end) {
            if (end > start) {
                characters = new ArrayList<String>();
                for (int i = 0; i < end - start; i++) {
                    characters.add(o.get(i + start));
                }
                unknownChar = o.get(end);
            } else if (end == start) {
                unknownChar = o.get(end);
            }
        }

        Section(List<String> o, int start) {
            if (o.size() > start) {
                characters = new ArrayList<String>();
                for (int i = start; i < o.size(); i++) {
                    characters.add(o.get(i));
                }
            }
        }

        boolean hasKnownCharacters() {
            return null != characters;
        }

        boolean hasUnknwonCharacter() {
            return null != unknownChar;
        }
    }

}

