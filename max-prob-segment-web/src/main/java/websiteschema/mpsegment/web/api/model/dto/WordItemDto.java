package websiteschema.mpsegment.web.api.model.dto;

import websiteschema.mpsegment.web.ui.model.UserDto;

import java.util.HashSet;
import java.util.Set;

public class WordItemDto {
    public String word;
    public String type;
    public int id;
    public String createAt;
    public UserDto user;
    public Set<String> pinyinSet = new HashSet<String>();
    public Set<ConceptDto> conceptSet = new HashSet<ConceptDto>();
    public Set<WordFreqDto> partOfSpeeches = new HashSet<WordFreqDto>();

    public WordItemDto(String word) {
        this.word = word;
    }
}
