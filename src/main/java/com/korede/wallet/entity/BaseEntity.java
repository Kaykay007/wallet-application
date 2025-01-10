package com.korede.wallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Slf4j
@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode
public abstract class BaseEntity implements  Serializable {

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

    @PrePersist
    protected void onCreate() {
        createDate = getCurrentDateTime();
        lastModified = createDate;
    }

    @PreUpdate
    protected void onUpdate() {
        lastModified = getCurrentDateTime();
    }


    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }
}
