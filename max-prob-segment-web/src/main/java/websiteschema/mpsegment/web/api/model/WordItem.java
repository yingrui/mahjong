package websiteschema.mpsegment.web.api.model;

import websiteschema.mpsegment.web.ui.model.User;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "WordItems",
        uniqueConstraints = {@UniqueConstraint(name = "word_unique", columnNames = "word")}
)
public class WordItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "words_id_seq")
    @SequenceGenerator(name = "words_id_seq", sequenceName = "words_id_seq")
    private int id;

    @Column(name = "Word")
    private String word;

    @Column(name = "Type")
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CreateBy")
    private User user;

    @Column(name = "CreateAt")
    private Date createAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "Pinyin", joinColumns = @JoinColumn(name = "WordId"))
    @Column(name = "Pinyin")
    private Set<String> pinyinSet;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "WordPartOfSpeech", joinColumns = @JoinColumn(name = "WordId"), inverseJoinColumns = @JoinColumn(name = "PartOfSpeechId"))
    private Set<PartOfSpeech> partOfSpeechSet;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "WordConcept", joinColumns = @JoinColumn(name = "WordId"), inverseJoinColumns = @JoinColumn(name = "ConceptId"))
    private Set<Concept> conceptSet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
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

    public Set<String> getPinyinSet() {
        if (null == pinyinSet) {
            pinyinSet = new HashSet<String>();
        }
        return pinyinSet;
    }

    public Set<PartOfSpeech> getPartOfSpeechSet() {
        if (null == partOfSpeechSet) {
            partOfSpeechSet = new HashSet<PartOfSpeech>();
        }
        return partOfSpeechSet;
    }

    public Set<Concept> getConceptSet() {
        if (null == conceptSet) {
            conceptSet = new HashSet<Concept>();
        }
        return conceptSet;
    }
}
