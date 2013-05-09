package websiteschema.mpsegment.web.api.model.dto;

import java.util.List;

public class ConceptDto {
    public int id;
    public String parent;
    public String name;
    public String note;
    public PartOfSpeechDto partOfSpeech;
    public List<ConceptDto> children;
}
