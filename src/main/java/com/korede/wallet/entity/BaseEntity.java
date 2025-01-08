package com.korede.wallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Persistable;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Slf4j
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Persistable<Long>, Serializable {

    @Serial
    private static final long serialVersionUID = 4390774380558885855L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Transient
    private boolean isNew = true;



    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate = getCurrentDateTime();

    @Column(name = "last_modified", nullable = false)
    private LocalDateTime lastModified = getCurrentDateTime();

    public boolean isNew() {
        return this.isNew;
    }



    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof BaseEntity other)) return false;
        if (other.getId() == null || this.getId() == null) return false;
        return Objects.equals(this.getId(), other.getId());
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = (result * PRIME) + (this.id == null ? 0 : this.id.hashCode());
        return result;
    }

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }
}
