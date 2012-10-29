package websiteschema.mpsegment.concept;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConceptLoader {

    ConceptTree conceptTree;

    public ConceptLoader(String resource) {
        loadConcept(getClass().getClassLoader().getResourceAsStream(resource));
    }

    public ConceptLoader(InputStream resource) {
        loadConcept(resource);
    }

    private void loadConcept(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            loadConcept(reader);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadConcept(BufferedReader reader) throws IOException {
        conceptTree = new ConceptTree();
        String line = reader.readLine();
        while (null != line) {
            try {
                parseThenAdd(line);
            } catch (DuplicateConceptException ex) {
                ex.printStackTrace();
            }
            line = reader.readLine();
        }
    }

    private void parseThenAdd(String conceptStr) throws DuplicateConceptException {
        String[] elements = conceptStr.split("\\s+");
        if (null == elements || elements.length != 3)
            return;
        long id = parseId(elements[0]);
        String name = elements[2];
        Concept concept = new Concept(id, name);
        long parentId = parseParentId(elements[0]);
        Concept parent = conceptTree.getConceptById(parentId);
        if (null != parent) {
            conceptTree.addConcept(concept, parent);
            return;
        }

        conceptTree.addConcept(concept);
    }

    private long parseId(String idStr) {
        return parseId(idStr, 0);
    }

    private long parseParentId(String idStr) {
        return parseId(idStr, 1);
    }

    private long parseId(String idStr, int rank) {
        String[] idStrings = idStr.split("\\.");
        if (idStrings.length <= rank) {
            return 0;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < idStrings.length - rank; i++) {
            String part = idStrings[i];
            if (part.length() == 1) {
                part = "0" + part;
            }
            stringBuilder.append(part);
        }
        return Long.parseLong(stringBuilder.toString());
    }

    public ConceptTree getConceptTree() {
        return conceptTree;
    }
}
