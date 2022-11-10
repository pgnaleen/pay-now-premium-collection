package com.dbs.paynowpremiumcollection.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = EncryptionInfo.TABLE)
//@SQLDelete(sql = "UPDATE " + ApplicationAcademicBackground.TABLE + " SET " + BaseEntity.COL_DELETED + " = true WHERE id = ?")
//@Where(clause = BaseEntity.COMMON_DELETE_CLAUSE)
public class EncryptionInfo {
    public static final String TABLE = "encryption_info";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "encrypted_file", length = 10485760)
    private String encryptedFile;

}
