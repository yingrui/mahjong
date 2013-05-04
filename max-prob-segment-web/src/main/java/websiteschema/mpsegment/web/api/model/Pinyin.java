package websiteschema.mpsegment.web.api.model;

import javax.persistence.*;

@Entity
@Table(
        name = "Pinyins"
)
public class Pinyin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pinyin_id_seq")
    @SequenceGenerator(name = "pinyin_id_seq", sequenceName = "pinyin_id_seq")
    private int id;

    @Column(name = "Name")
    private String name;

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
}
