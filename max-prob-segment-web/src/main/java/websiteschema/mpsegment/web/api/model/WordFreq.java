package websiteschema.mpsegment.web.api.model;

import websiteschema.mpsegment.web.api.model.dto.WordFreqDto;

import javax.persistence.*;

@Entity
@Table(
        name = "WordFreq"
)
public class WordFreq {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "word_freq_id_seq")
    @SequenceGenerator(name = "word_freq_id_seq", sequenceName = "word_freq_id_seq")
    private int id;

    @Column(name = "Freq")
    private int freq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PartOfSpeechId")
    private PartOfSpeech partOfSpeech;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public WordFreqDto toDto() {
        WordFreqDto wordFreqDto = new WordFreqDto();
        wordFreqDto.id = id;
        wordFreqDto.freq = freq;
        wordFreqDto.partOfSpeech = partOfSpeech.toDto();
        return wordFreqDto;
    }
}
