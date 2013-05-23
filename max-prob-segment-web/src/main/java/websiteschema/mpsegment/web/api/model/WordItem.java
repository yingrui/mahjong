package websiteschema.mpsegment.web.api.model;

import websiteschema.mpsegment.web.api.model.dto.WordItemDto;
import websiteschema.mpsegment.web.ui.model.User;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static websiteschema.mpsegment.web.util.DateUtil.toDateString;

@Entity
@Table(
        name = "WordItems",
        uniqueConstraints = {@UniqueConstraint(name = "word_unique", columnNames = "name")}
)
public class WordItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "words_id_seq")
    @SequenceGenerator(name = "words_id_seq", sequenceName = "words_id_seq")
    private int id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreateBy")
    private User user;

    @Column(name = "CreateAt")
    private Date createAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "WordId")
    private Set<Pinyin> pinyinSet;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "WordId")
    private Set<WordFreq> wordFreqSet;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "WordConcept", joinColumns = @JoinColumn(name = "WordId"), inverseJoinColumns = @JoinColumn(name = "ConceptId"))
    private Set<Concept> conceptSet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Set<Pinyin> getPinyinSet() {
        if (null == pinyinSet) {
            pinyinSet = new HashSet<Pinyin>();
        }
        return pinyinSet;
    }

    public Set<WordFreq> getWordFreqSet() {
        if (null == wordFreqSet) {
            wordFreqSet = new HashSet<WordFreq>();
        }
        return wordFreqSet;
    }

    public Set<Concept> getConceptSet() {
        if (null == conceptSet) {
            conceptSet = new HashSet<Concept>();
        }
        return conceptSet;
    }

    public WordItemDto toDto() {
        WordItemDto wordItemDto = new WordItemDto(name);
        wordItemDto.type = type;
        wordItemDto.id = id;

        wordItemDto.createAt = toDateString(createAt);

        wordItemDto.user = user.toDto();

        for(Pinyin pinyin : pinyinSet) {
            wordItemDto.pinyinSet.add(pinyin.getName());
        }

        for(WordFreq wordFreq : wordFreqSet) {
            wordItemDto.partOfSpeeches.add(wordFreq.toDto());
        }

        for(Concept concept : conceptSet) {
            wordItemDto.conceptSet.add(concept.toDto());
        }

        return wordItemDto;
    }
}
