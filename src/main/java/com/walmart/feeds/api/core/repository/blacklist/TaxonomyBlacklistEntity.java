package com.walmart.feeds.api.core.repository.blacklist;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Entity
@Table(name = "taxonomy_blacklist")
public class TaxonomyBlacklistEntity extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "taxonomy_bkl_mapping_uuid_generator")
    @GenericGenerator(name = "taxonomy_bkl_mapping_uuid_generator", strategy = "uuid2")
    @ApiModelProperty(hidden = true)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slug;

    @ManyToMany
    @JoinTable(name = "taxonomy_blacklist_taxonomy_mapping",
            joinColumns = {
                @JoinColumn(name = "taxonomy_blacklist_id", referencedColumnName = "id", table = "taxonomy_blacklist",
                        foreignKey = @ForeignKey(name = "fk_taxonomy")),
                @JoinColumn(name = "taxonomy_blacklist_mapping_id", referencedColumnName = "id", table = "taxonomy_blacklist_mapping",
                        foreignKey = @ForeignKey(name = "fk_mapping"))
            })
    private List<TaxonomyBlacklistMapping> list;

}
