package websiteschema.mpsegment.pinyin;

import websiteschema.mpsegment.hmm.Node;
import websiteschema.mpsegment.hmm.ObserveListException;

import java.util.ArrayList;
import java.util.List;

public class WordToPinyinClassifier {

    WordToPinyinModel model;

    public void setModel(WordToPinyinModel model) {
        this.model = model;
    }

    public List<String> classify(String o) throws ObserveListException {
        List<String> observeList = new ArrayList<String>(o.length());
        for(int i = 0; i < o.length(); i++) {
            observeList.add(String.valueOf(o.charAt(i)));
        }
        return classify(observeList);
    }

    public List<String> classify(List<String> o) throws ObserveListException {
        assert (null != o && o.size() > 0);
        int lastSectorPos = 0;
        List<Sector> sectors = new ArrayList<Sector>();
        for (int i = 0; i < o.size(); i++) {
            String ch = o.get(i);
            boolean knownObserveNode = model.knwonObserve(ch);
            if (!knownObserveNode) {
                Sector sector = new Sector(o, lastSectorPos, i);
                sectors.add(sector);
                lastSectorPos = i + 1;
            }
        }
        Sector sec = new Sector(o, lastSectorPos);
        sectors.add(sec);

        List<String> result = new ArrayList<String>();
        for (Sector sector : sectors) {
            if (sector.hasKnownCharacters()) {
                result.addAll(convert(model.getViterbi().calculateWithLog(sector.characters)));
            }
            if (sector.hasUnknwonCharacter()) {
                result.add(sector.unknownChar);
            }
        }

        return result;
    }

    private List<String> convert(List<Node> nodeList) {
        List<String> result = new ArrayList<String>(nodeList.size());
        for (Node node : nodeList) {
            result.add(node.getName());
        }
        return result;
    }

    class Sector {
        List<String> characters;
        String unknownChar;

        Sector(List<String> o, int start, int end) {
            if (end - 1 > start) {
                characters = new ArrayList<String>();
                for (int i = 0; i < end - start; i++) {
                    characters.add(o.get(i + start));
                }
                unknownChar = o.get(end);
            } else if (end == start) {
                unknownChar = o.get(end);
            }
        }

        Sector(List<String> o, int start) {
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

