package websiteschema.mpsegment.web.api.model;

import websiteschema.mpsegment.web.api.model.dto.ConceptDto;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "Concepts"
)
public class Concept {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "concepts_id_seq")
    @SequenceGenerator(name = "concepts_id_seq", sequenceName = "concepts_id_seq")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ParentId")
    private Concept parent;

    @Column(name = "Name")
    private String name;

    @Column(name = "Note")
    private String note;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PartOfSpeech")
    private PartOfSpeech partOfSpeech;

    @Column(name = "CreateAt")
    private Date createAt;

    public int getId() {
        return id;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Concept getParent() {
        return parent;
    }

    public void setParent(Concept parent) {
        this.parent = parent;
    }

    public ConceptDto toDto() {
        ConceptDto conceptDto = new ConceptDto();
        conceptDto.id = id;
        conceptDto.name = name;
        conceptDto.note = note;
        if (null != parent) {
            conceptDto.parentId = parent.getId();
        }
        if (null != partOfSpeech) {
            conceptDto.partOfSpeech = partOfSpeech.toDto();
        }

        return conceptDto;
    }
}
