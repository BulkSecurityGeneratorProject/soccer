package gl.linpeng.soccer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Dict.
 */
@Entity
@Table(name = "dict")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @ManyToOne
    private DictKind dictKind;

    @ManyToMany(mappedBy = "positions")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Player> players = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Dict name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public Dict code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DictKind getDictKind() {
        return dictKind;
    }

    public Dict dictKind(DictKind dictKind) {
        this.dictKind = dictKind;
        return this;
    }

    public void setDictKind(DictKind dictKind) {
        this.dictKind = dictKind;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Dict players(Set<Player> players) {
        this.players = players;
        return this;
    }

    public Dict addPlayer(Player player) {
        players.add(player);
        player.getPositions().add(this);
        return this;
    }

    public Dict removePlayer(Player player) {
        players.remove(player);
        player.getPositions().remove(this);
        return this;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dict dict = (Dict) o;
        if(dict.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dict.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Dict{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", code='" + code + "'" +
            '}';
    }
}
